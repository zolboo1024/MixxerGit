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

    public static final String CHANNEL_1_ID = "private message notifications";
    public static final String CHANNEL_2_ID = "other notifications";

    @Override
    public void onCreate() {
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
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(soundUri);
        notificationBuilder.setPriority(Notification.PRIORITY_MAX);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        notificationManager.notify(0, notificationBuilder.build());
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message) {

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
