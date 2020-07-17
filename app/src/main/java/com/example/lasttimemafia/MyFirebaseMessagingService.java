package com.example.lasttimemafia;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static boolean token = false;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("petrol", "TOKEN CREATED");
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
        token = true;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TAGDEFAULT", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAGDEFAULT", "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                //scheduleJob();
            } else {
                //Log.d("touchdown","Message:" + remoteMessage.getData().get("score"));
                //Intent in = null;
                //sendBroadcast(in);
                // Handle message within 10 seconds
                //handleNow();
                processReceivedMessage(remoteMessage);
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("TAGDEFAULT", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }
    public void processReceivedMessage(RemoteMessage remoteMessage){
        Log.d("gamernation","Message in Messaging Service");
        Log.d("gamernation","Message in Messaging Service:" + remoteMessage.getData().get("purpose"));
        Intent intent = null;
        if(remoteMessage.getData().get("purpose").equals("registername")){
            intent = new Intent("REGISTERNAME");
            intent.putExtra("name",remoteMessage.getData().get("name"));
            intent.putExtra("port",String.valueOf(MafiaNetworkCode.currentPort));
            MafiaNetworkCode.currentPort++;
            Log.d("gamernation","Register name processes finished");
        }else if(remoteMessage.getData().get("purpose").equals("message")){
            intent = new Intent("NEWMESSAGE");
            intent.putExtra("message",remoteMessage);
            Log.d("realer","purpose equals message");
        }else if(remoteMessage.getData().get("purpose").equals("messagetohost")){
            intent = new Intent("NEWMESSAGETOHOST");
        }
        sendBroadcast(intent);
        Log.d("gamernation","Broadcast sent");
    }
}