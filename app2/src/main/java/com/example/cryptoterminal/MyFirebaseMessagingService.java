package com.upperhand.cryptoterminal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Uri defaultSoundUri;
    String topic;
    String channelId = "0";
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Context context;


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("Refreshed token:",token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map data = remoteMessage.getData();
        String title = (String) data.get("title");
        String text = (String) data.get("text");
        topic = (String) data.get("topic");
        String link = "https://twitter.com/twitter/statuses/" + data.get("link");

        url();

        sendNotification(title,text,link);
    }

    private void sendNotification(String title,String text, String link) {


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);



        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.alert)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(defaultSoundUri, audioAttributes);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 , notificationBuilder.build());
    }


    private void url (){

        if(topic.equals("night")){

            preferences = this.getSharedPreferences("night_c", Context.MODE_PRIVATE);
            channelId = String.valueOf(preferences.getInt("night_c", 1));

            if (channelId.equals("1")){
                defaultSoundUri = Uri.parse("android.resource://"
                        + this.getPackageName() + "/" + R.raw.small);
            }else if (channelId.equals("2")){
                defaultSoundUri = Uri.parse("android.resource://"
                        + this.getPackageName() + "/" + R.raw.medium);
            }else if (channelId.equals("3")){
                defaultSoundUri = Uri.parse("android.resource://"
                        + this.getPackageName() + "/" + R.raw.pit);
            }

        }else if(topic.equals("price")) {
            channelId = "0";
            defaultSoundUri = Uri.parse("android.resource://"
                    + this.getPackageName() + "/" + R.raw.medium);
        }

    }

}