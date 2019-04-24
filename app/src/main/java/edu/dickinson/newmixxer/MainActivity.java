package edu.dickinson.newmixxer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zolboo Erdenebaatar and Olivia Voler.
 * The entry point of the application.
 * Has buttons to log in, sign up, and take the user to settings.
 */

public class MainActivity extends AppCompatActivity {

    AnimationDrawable stickyNotesAnimation;
    private SharedPreferences sharedPreferences;
    ProgressDialog pd; //opening up this Activity might take a while. So this is a dialog that spins while the app is loading
    Button signUp;
    Button logIn;
    ImageButton settingsButton;
    PackageManager pm;

    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    public static CustomTabsClient mCustomTabsClient;
    public static CustomTabsSession mCustomTabsSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = findViewById(R.id.stickyNotes);
        img.setBackgroundResource(R.drawable.sticky_note_animation);
        AnimationDrawable stickyNotesAnimation = (AnimationDrawable) img.getBackground();
        pm = getPackageManager();
        stickyNotesAnimation.start();

        signUp = findViewById(R.id.signUp);//register the buttons
        logIn = findViewById(R.id.logIn);

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                launchSettings();
            }
        });

        CustomTabsServiceConnection connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mCustomTabsClient = client;
                boolean ok= mCustomTabsClient.warmup(0); //this warms up the URL trying to load
                mCustomTabsSession = mCustomTabsClient.newSession(null);
                List<URL> likelyList= new ArrayList<>();

                //uri is the most URL that the user is most likely to visit
                Uri uri= Uri.parse("https://www.language-exchanges.org/user/login");

                // we are telling the custom tab that the URL above may have the highest chance of being
                // launched so that Chrome tab connects to the URL before the button is actually clicked.
                mCustomTabsSession.mayLaunchUrl(uri, null, null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mCustomTabsClient = null;
            }
        };

        boolean ok = CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, connection);
        Intent intent = getIntent();
//        if(intent.getExtras() != null){
//            launchURLFinal("https://www.language-exchanges.org/private_messages");
//        }
    }

    public void launchLogIn(View view){
        String url = "https://www.language-exchanges.org/user/login?token="+getToken(); // dev server login url
        launchURL(url);
    }

    public void launchSignup(View view){
        String url = "https://www.language-exchanges.org/user/register";
        launchURL(url);
    }

    public void launchSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public String getToken(){
        return FirebaseInstanceId.getInstance().getToken();
    }
    public void launchURL(String url){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean asked = preferences.getBoolean("skypeAsked", false);
        if(!isPackageInstalled("com.skype.raider", pm) && asked == false){
            showChoice(url);
        }
        else {
            launchURLFinal(url);
        }
    }

    public void launchURLFinal(String url){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.setToolbarColor(Color.parseColor("#263038"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(CUSTOM_TAB_PACKAGE_NAME);
        Log.d("URLLaunched: ", url);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }

        return found;
    }
    private void showChoice(String url) {
        final String finalURL= url;
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Mixxer users often use Skype. Would you like to install the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.skype.raider")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.skype.raider")));
                        }
                    }
                }).setNegativeButton("Don't ask again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        launchURLFinal(finalURL);
                    }
                });

        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean("skypeAsked", true);
        editor.apply();
        AlertDialog finalDialog= builder.create();
        finalDialog.show();
    }
}


