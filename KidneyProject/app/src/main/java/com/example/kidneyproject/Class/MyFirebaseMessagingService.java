package com.example.kidneyproject.Class;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.kidneyproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    @Override
    public void onNewToken(String s) {

        super.onNewToken(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        JSONObject object = null;
        String typeNotification = null;

        if(remoteMessage.getNotification()!=null)
        {
            String title = remoteMessage.getNotification().getTitle();
            String Message = remoteMessage.getNotification().getBody();

            Map<String, String> params = remoteMessage.getData();
             object = new JSONObject(params);
            try {
                typeNotification = object.getString("typeNotification").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }


           /* NotificationHelper.showNotification(title,Message,getApplicationContext(),

                    typeNotification  );*/
        }



        Log.e("JSON_OBJECT", object.toString());

        String NOTIFICATION_CHANNEL_ID = "Nilesh_channel";

        long pattern[] = {0, 1000, 500, 1000};

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
           // notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }
      int image= R.drawable.notification;
       if (typeNotification.equals("visit"))
            image=R.drawable.accept;
        if (typeNotification.equals("Wish"))
            image=R.drawable.wish;
        if (typeNotification.equals("Chat"))
            image=R.drawable.chat;
        if (typeNotification.equals("Talent"))
            image=R.drawable.heartlicke;


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis()+2)
                .setSmallIcon(image)
                .setAutoCancel(true);


        mNotificationManager.notify(1000, notificationBuilder.build());
    }
}