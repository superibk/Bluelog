package com.iom.bluelog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import java.util.Random;

public class BluetoothReceiver extends BroadcastReceiver {
    Context ctx;
    DatabaseManager database;
    Long id;
    Random random;
    int randomInt;

    public BluetoothReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
          ctx = context;
          database  = new DatabaseManager(ctx);
          random  = new Random();
          database.open();
          String action = intent.getAction();
          BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {}
        else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            id =  database.createRecords(device.getName(),getCurrentDate(),getCurrentTime(), getCurrentTime(), device.getAddress());
             sendNotification(device);
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {/*Done searching*/ }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) { /*Device is about to disconnect*/}
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
             //Device has disconnected
              long idNew = database.getLastRowId();
              database.updateStopTime(idNew, getCurrentTime());

        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }


    private  void sendNotification(BluetoothDevice device) {

        NotificationManager mNotificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(ctx, DashBoard.class);
        intent.putExtra("devicename",device.getName());
        intent.putExtra("address", device.getAddress());
        randomInt = random.nextInt(200);
        intent.putExtra("random", randomInt);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent , 0);


        NotificationCompat.Builder noti =  new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.bluetooth)     // if you want to include an icon
                .setContentTitle("Bluetooth Connection")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(device.getName() + ": " + device.getAddress()))
                .setWhen(System.currentTimeMillis())
                .setTicker("By: BlUELog")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentText(device.getName() + ": " + device.getAddress());
        noti.setContentIntent(pendingIntent);
        mNotificationManager.notify(randomInt, noti.build());

    }


    public String getCurrentDate(){
        Time dtNow = new Time();
        dtNow.setToNow();
        return dtNow.format("%Y.%m.%d %H:%M");    // YYYYMMDDTHHMMSS
    }

    public String getCurrentTime(){
        Time dtNow = new Time();
        dtNow.setToNow();
        return dtNow.format("%H:%M");    // YYYYMMDDTHHMMSS
    }


}
