package com.iom.bluelog;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class DashBoard extends Activity {
     SharedPreferences pref;
     String name, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dash_board);

        ImageView logoImage = (ImageView)findViewById(R.id.imageView1);
        //defining an animation to scroll the text

        TranslateAnimation mAnimation = new  TranslateAnimation(-360f,0,0,0);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setRepeatCount(Animation.ABSOLUTE);
        logoImage.setAnimation(mAnimation);

        // get info from the shared preferences manager
         pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
         name = pref.getString("password", "");
         passwordText = pref.getString("passwordRecovery", "");


        if(name==""){
            // create an EditText for the dialog
            final EditText enteredPassword = new EditText(this);
            enteredPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
            builder.setTitle("Create Password");
            builder.setView(enteredPassword);
            builder.setPositiveButton("Create", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("password",enteredPassword.getText().toString());
                    editor.commit();
                    dialog.cancel();
                    createPasswordRecovery();
                }
            });
            builder.create().show();
        }

}


    public  void onClickLogin(View view){

        {
            final EditText enteredPasswordConfirm = new EditText(this);
            enteredPasswordConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
            builder.setTitle("Enter Password");
            builder.setView(enteredPasswordConfirm);
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String passwordText = enteredPasswordConfirm.getText().toString();
                    if (passwordText.equalsIgnoreCase(name)) {
                        Intent nextActivity = new Intent(DashBoard.this, History.class );
                        startActivity(nextActivity);
                        finish();
                    } else {
                        Toast.makeText(DashBoard.this, "Wrong Password", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                }
            });
            builder.create().show();
        }



    }



    public  void onClickForgetPassword(View view){

        {
            final EditText enteredRecoveryText = new EditText(this);

            AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
            builder.setTitle("Enter Password Recovery Text");
            builder.setView(enteredRecoveryText);
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    String passwordRecoveryText_Text = enteredRecoveryText.getText().toString();
                    if (passwordRecoveryText_Text.equalsIgnoreCase(passwordText)) {

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("password", "");
                        editor.commit();
                        Intent intentReset = new Intent(DashBoard.this, DashBoard.class);
                        startActivity(intentReset);
                        finish();

                    } else {
                        Toast.makeText(DashBoard.this, "Wrong Password", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                }
            });
            builder.create().show();
        }



    }







    public void createPasswordRecovery(){

       final EditText passwordRecovery = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
        builder.setTitle("Set Password Recovery Text");
        builder.setView(passwordRecovery);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("passwordRecovery",passwordRecovery.getText().toString());
                editor.commit();
                dialog.cancel();
                Toast.makeText(DashBoard.this, "password and password key created successfully", Toast.LENGTH_LONG).show();
                Intent intentRecovery = new Intent(DashBoard.this, History.class);
                startActivity(intentRecovery);
                finish();}
        });
        builder.create().show();



    }












}