package com.kyros.technologies.fieldout.fcm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thirunavukkarasu on 19-11-2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG="FBMessagingService";
    private SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"From: "+remoteMessage.getFrom());
        if(remoteMessage.getData().size()>0){
            Log.d(TAG,"Message data: "+remoteMessage.getData());
        }
        if(remoteMessage.getNotification()!=null){
            Log.d(TAG,"mesage body: "+remoteMessage.getNotification().getBody());
            //below line is commented for cumulations code and this is kyros technology code for push notification
//            sendNotification(remoteMessage.getNotification().getBody());
        }
        Log.d("!!!!", "notification received");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyFirebaseMessagingService.this);
       // sendNotificationcumulation(remoteMessage);
    }

//    private void sendNotificationcumulation(RemoteMessage remoteMessage) {
//        Log.d(TAG," raw message : "+remoteMessage.toString());
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        StringBuilder builder = new StringBuilder();
//
//        for (String key : remoteMessage.getData().keySet()) {
//            builder.append(key + ":" + remoteMessage.getData().get(key));
//        }
//        builder.append(remoteMessage.getData().size());
//        sharedPreferences.edit().putString(Constants.notificationData, builder.toString()).commit();
//
//        Uri soundFile = null;
//        String contentTitle = "Milesmate";
//        String contentText = "New Message";
//        if (remoteMessage.getData().get("body") != null) {
//            JSONObject body = stringToJsonObject(remoteMessage.getData().get("body"));
//            try {
//                contentText = body.getString("message");
//                if (body.getString("notification_type") != null) {
//                    String notificationType = body.getString("notification_type");
//                    Log.d(TAG," notification type : "+notificationType);
//
//                    soundFile = (Uri.parse("android.resource://"
//                            + this.getPackageName() + "/"
//                            + R.raw.ring));
//                    /*switch (notificationType) {
//                        case "ring":
//                            playAlertSound();
//                            break;
//                        case "vibrate":
//                            vibrateMobile();
//                            break;
//                        case "both":
//                            playAlertAndVibrate();
//                            break;
//                        default:
//                            break;
//                    }*/
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        /*if (remoteMessage.getData().containsKey("title")) {
//            contentTitle = remoteMessage.getData().get("title");
//        }*/
//        if(contentText.equals("New Message")){
//            contentText=remoteMessage.getNotification().getBody();
//        }
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//            .setSmallIcon(R.mipmap.m)
//                .setContentTitle(contentTitle)
//                .setSound(soundUri) //This sets the sound to play
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
//                .setContentText(contentText)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
//        if (soundFile != null) {
//            notificationBuilder.setSound(soundFile);
//        }
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

    private JSONObject stringToJsonObject(String jsonString) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

//    private void playAlertSound() {
//      /*  Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();*/
//
//        MediaPlayer ring = MediaPlayer.create(MyFirebaseMessagingService.this, R.raw.ring);
//        ring.start();
//    }

//    private void vibrateMobile() {
//        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
//        // Vibrate for 1 minute
//        v.vibrate(60000);
//    }
//
//    private void playAlertAndVibrate() {
//        playAlertSound();
//        vibrateMobile();
//    }
//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(android.R.drawable.stat_notify_more)
//                .setContentTitle("FCM Message")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
}
