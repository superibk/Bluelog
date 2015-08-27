package com.iom.bluelog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Detail extends Activity {
    DatabaseManager database;
    Long id;
    TextView startTime, stopTime, deviceName, deviceAddress,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        id = getIntent().getExtras().getLong(database.KEY_ROWID);
//        font1 =  Typeface.createFromAsset(this.getAssets(), "Roboto-Bold.ttf");
//        font2 =  Typeface.createFromAsset(this.getAssets(), "Roboto-Condensed.ttf");
        setContentView(R.layout.activity_detail);
        startTime = (TextView)findViewById(R.id.item_time_start);
//        startTime.setTypeface(font2);
        stopTime = (TextView)findViewById(R.id.item_time_stop);
        deviceName = (TextView)findViewById(R.id.text_details_detail);
//        deviceName.setTypeface(font1);
        date = (TextView)findViewById(R.id.item_date_detail);
        deviceAddress = (TextView)findViewById(R.id.item_address_detail);
        database = new DatabaseManager(this);
        database.open();
        startTime.setText(database.getTime(id));
        stopTime.setText(database.getTimeStop(id));
        deviceName.setText(database.getDeviceName(id));
        deviceAddress.setText(database.getFile(id));
        date.setText(database.getDate(id));
        database.close();

}


    public void onClickEdit(View view){

        {
            final EditText editProfile = new EditText(this);
            editProfile.setText(deviceName.getText());
            AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
            builder.setTitle("Enter new detail");
            builder.setView(editProfile);
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    database.open();
                    String saveText = editProfile.getText().toString();
                    database.updateDeviceName(Detail.this.id, saveText);
                    Toast.makeText(Detail.this, "Save successfully", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    database.close();
                    finish();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }

            });
            builder.create().show();
        }

    }


    public void onCLickDelete(View view){


        // an alert box to confirm if the user really wants to delete the record

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete"+ " " + deviceName.getText().toString()  );
        builder.setTitle("Confirmation");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Long deleteRowID = Detail.this.id;
                database.open();
                database.deleteRecord(deleteRowID);
                database.close();
                Toast.makeText(Detail.this, "Deleted Succesfully", Toast.LENGTH_LONG).show();
                finish();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();


    }


}
