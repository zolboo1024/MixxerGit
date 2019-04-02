package edu.dickinson.newmixxer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import edu.dickinson.newmixxer.MainActivity;
import edu.dickinson.newmixxer.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String SERVICE_LOGIN_INTENT = "loginIntent";
    public static final String CHANNEL_1_ID = "private message notifications";
    public static final String CHANNEL_2_ID = "other notifications";

    @Override
    public void onCreate() {
        Log.d("FBSErvice", "FBMessaging service instantiated. ");
        super.onCreate();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        //sendMyNotification(message.getNotification().getBody());
        Log.d("FireBase message", "From: " + message.getFrom());

        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Log.d("FireBase message", "Message data payload: " + message.getData());


        }
        if (message.getNotification() != null) {
            Log.d("Body Notif", "Message Notification Body: " + message.getNotification().getBody());
            Log.d("Message Received", "CALL SEND NOTIF");
            sendMyNotification(message);
        }
    }

    private void sendMyNotification(RemoteMessage message) {

//        //On click of notification it redirects to this Activity
        Log.d("MessageFromFB", "collapseKey: " +message.getCollapseKey());
        Log.d("MessageFromFB", "from: "+message.getFrom());
        Log.d("MessageFromFB", "messageId: "+message.getMessageId());
        Log.d("MessageFromFB", "messageType: "+message.getMessageType());
        Log.d("MessageFromFB", "to: "+message.getTo());
        Log.d("MessageFromFB", "getNotification.toString(): "+message.getNotification().toString());
        Log.d("MessageFromFB", "getData.toString(): "+message.getData().toString());
        Log.d("MessageFromFB", "getOriginalPriority: "+String.valueOf(message.getOriginalPriority()));
        Log.d("MessageFromFB", "getPriority: "+String.valueOf(message.getPriority()));
        Log.d("MessageFromFB", "getTtl: "+String.valueOf(message.getTtl()));
        Intent intent = new Intent(this, InboxFromNotif.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(SERVICE_LOGIN_INTENT, "ToInbox");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.notification_icon_mixxer)
                //.setContentTitle(message.getNotification().getTitle())
                .setContentTitle("The Mixxer")
                //.setContentText(message.getNotification().getBody())
                .setContentText("You have a new message")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); //try spitting everything out here.
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel messages = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Private message notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            messages.setDescription("You just got sent a private message notification");

            NotificationChannel other = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Other notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                    );
            other.setDescription("You just got sent another type of notification");

//      Olivia commented this out: trying to create multiple channels for different types of notifications

//            CharSequence name = "FireBase Notification";
//            String description = "whatever";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("Typical ID", name, importance);
//            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(messages);
            notificationManager.createNotificationChannel(other);
        }
    }
}
