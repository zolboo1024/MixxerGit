package edu.dickinson.newmixxer;

import android.content.ComponentName;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InboxFromNotif extends AppCompatActivity {

    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    public static CustomTabsClient mCustomTabsClient;
    public static CustomTabsSession mCustomTabsSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Notif", "Inbox notif opened");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_from_notif);
        CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {

            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mCustomTabsClient = client;
                boolean ok= mCustomTabsClient.warmup(0); //this warms up the URL trying to load
                mCustomTabsSession = mCustomTabsClient.newSession(null);
                List<URL> likelyList= new ArrayList<>();
                Uri uri= Uri.parse("https://www.language-exchanges.org/user/login"); //uri is the most URL that the
                //user is most likely to visit
                mCustomTabsSession.mayLaunchUrl(uri, null, null);// we are telling the custom tab
                // that the URL above may have the highest chance of being launched
                // so that chrome tab connects to the URL before the button is actually clicked.
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mCustomTabsClient = null;
            }
        };

        boolean ok = CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, connection);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        //builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#263038"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(CUSTOM_TAB_PACKAGE_NAME);
        customTabsIntent.launchUrl(this, Uri.parse("https://www.language-exchanges.org/private_messages"));
    }
}
