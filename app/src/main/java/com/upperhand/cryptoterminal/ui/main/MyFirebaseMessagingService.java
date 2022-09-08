package com.upperhand.cryptoterminal.ui.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.upperhand.cryptoterminal.MainActivity;
import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.Utils;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Uri defaultSoundUri;
    String topic;
    String channelId = "0";

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
        String link = "" + data.get("link");

        Log.e("see",title);
        Log.e("see",text);
        Log.e("see",topic);
        Log.e("see",link);

        url();

        sendNotification(title,text,link);
    }

    private void sendNotification(String title,String text, String link) {


        boolean alertsOn = Utils.getSharedPref("alerts", false, this);

        if(alertsOn) {

            //==========================  EXTRA INPUT

            boolean links = Utils.getSharedPref("links", false, this);
            Intent intent;

            if(!links) { //   INSIDE APP
                intent = new Intent(this, MainActivity.class);
                Utils.setSharedPref("topic", topic, this);
            }else {   //   OUTSIDE APP
                if( topic.equals("breaking")){
                    intent = new Intent(this, MainActivity.class);
                    Utils.setSharedPref("topic", topic, this);
                }else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link));
                }
            }

            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.icon)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pi);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel(channelId,
                        "title",
                        NotificationManager.IMPORTANCE_DEFAULT);

                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setSound(defaultSoundUri, audioAttributes);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
    }


    private void url (){

      if(topic.equals("breaking")) {

            channelId =  String.valueOf(Utils.getSharedPref("breaking_sound", 1, this));

            switch (channelId) {
                case "0":
                    defaultSoundUri = Uri.parse("android.resource://"
                            + this.getPackageName() + "/" + R.raw.empty);
                    break;
                case "1":
                    defaultSoundUri = Uri.parse("android.resource://"
                            + this.getPackageName() + "/" + R.raw.medium);
                    break;
                case "2":
                    defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    break;
            }
        }else if(topic.equals("alts")) {

            channelId =  String.valueOf(Utils.getSharedPref("alts_sound", 1, this));

            switch (channelId) {
                case "0":
                    defaultSoundUri = Uri.parse("android.resource://"
                            + this.getPackageName() + "/" + R.raw.empty);
                    break;
                case "1":
                    defaultSoundUri = Uri.parse("android.resource://"
                            + this.getPackageName() + "/" + R.raw.medium);
                    break;
                case "2":
                    defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    break;
            }
        }

    }

}