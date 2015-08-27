package com.iom.bluelog;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class History extends Activity  implements OnItemClickListener {

    DatabaseManager database;
    ListView listNote;
    Cursor cr;
    EditText searchBox;
    SharedPreferences settings;
    String filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history);
        database = new DatabaseManager(this);
        database.open();
        searchBox = (EditText) findViewById(R.id.search_text);
        listNote = (ListView) findViewById(R.id.listView_history);
        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        filter = settings.getString("search", "1");
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                populateListViewFromDatabase();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

         populateListViewFromDatabase();
         listNote.setOnItemClickListener(this);

    }

    public void populateListViewFromDatabase() {
        if (filter.contains("1")) {
            cr = database.getFilterdResultByDevice(searchBox.getText().toString());
            searchBox.setHint("Search By Bluetooth Name");
        } // search filter is by title

        else {
            cr = database.getFilterdResultByFile(searchBox.getText().toString());
            searchBox.setHint("Search By Address Name");
        } // if search query is by content

        startManagingCursor(cr);

        // Create an array to specify the fields we want
        String[] from = new String[] {DatabaseManager.KEY_DEVICE_NAME, DatabaseManager.KEY_TIME, DatabaseManager.KEY_DATE };

        // and an array of the fields we want to bind in the view
        int[] to = new int[] { R.id.item_device_name, R.id.item_time , R.id.item_date};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,R.layout.sample_item, cr, from, to);
        listNote.setAdapter(cursorAdapter);
    }




    public void onClickFilter(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(History.this);
        builder.setTitle("Filter By");
        builder.setItems(
                new String[] { "Device Name", "File Sent/Received"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor editor = settings.edit();
                        // The 'which' argument contains the index position  of the selected item

                        switch (which) {
                            case 0:
                                editor.putString("search", "1");
                                break;
                            case 1:
                                editor.putString("search", "2");
                                break;
                        }
                        editor.commit();
                        Intent newClass = new Intent(History.this,History.class);
                        startActivity(newClass);
                        finish();
                    }
                });
        builder.create().show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//         this will be executed in respective of id
        Intent i = new Intent(History.this, Detail.class);
        Bundle sendid = new Bundle();
        sendid.putLong(database.KEY_ROWID, id);
        i.putExtras(sendid);
        startActivity(i);
    }


    public void onClickAboutUs(View view){
        Intent aboutIntent = new Intent(History.this, About.class);
        startActivity(aboutIntent);
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.list_note, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.settings) {
//            Intent intent = new Intent(ListNote.this, Settings.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }




}