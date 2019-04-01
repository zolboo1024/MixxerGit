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
 * Created by Zolboo Erdenebaatar 11/19/2018
 * The entry point of the application. This is the screen that
 * has the 2 buttons when the app is opened up.
 *
 */
public class MainActivity extends AppCompatActivity {
    AnimationDrawable stickyNotesAnimation;
    private SharedPreferences sharedPreferences;
    ProgressDialog pd; //opening up this Activity might take a while. So this is a dialog that spins while the app is loading
    Button signUp; //sign up Button, same as logIn
    Button logIn;
    Button getToken;
    TextView displayToken;
    PackageManager pm;
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    public static CustomTabsClient mCustomTabsClient;
    public static CustomTabsSession mCustomTabsSession;

    //public static String joReturn;    //was useful when we used the method "yourDataTask". No longer useful
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = findViewById(R.id.stickyNotes);
        img.setBackgroundResource(R.drawable.sticky_note_animation);
        AnimationDrawable stickyNotesAnimation = (AnimationDrawable) img.getBackground();
        pm= getPackageManager();
        stickyNotesAnimation.start();

        signUp = findViewById(R.id.signUp);//register the buttons
        logIn = findViewById(R.id.logIn);
//            signUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //when the button is clicked, kick them off to the web browser at the
//                    //specified url
////                    Intent browserIntent = new Intent(getApplicationContext(), Browser.class);
////                    browserIntent.putExtra("url", "https://www.language-exchanges.org/user/register");
////                    startActivity(browserIntent);
//                    String url = "https://www.language-exchanges.org/user/login";
//                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                    CustomTabsIntent customTabsIntent = builder.build();
//                    customTabsIntent.launchUrl(getParent(), Uri.parse(url));
//                }
//            });
//            logIn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent browserIntent = new Intent(getApplicationContext(), Browser.class);
//                    browserIntent.putExtra("url", "https://www.language-exchanges.org/user/login");
//                    startActivity(browserIntent);
//                }
//            });

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
        Intent intent = getIntent();
        if(intent != null && intent.getExtras()!=null){
            Log.d("Extras", intent.getExtras().toString());
        }
//        if(intent != null && intent.getStringExtra(MyFirebaseMessagingService.SERVICE_LOGIN_INTENT)!=null){
//            Log.d("FromService", intent.getStringExtra(MyFirebaseMessagingService.SERVICE_LOGIN_INTENT));
//        }
        else if(intent != null) {

        }
        if(intent.getExtras() != null){
            launchURLFinal("https://www.language-exchanges.org/private_messages");
        }
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        stickyNotesAnimation.start();
    }*/

    public void launchLogIn(View view){
        String url = "https://www.language-exchanges.org/user/login?token="+getToken(); // dev server login url
        launchURL(url);
        Log.d("Updated URL: ", url);
//        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//        mUser.getIdToken(true)
//                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                    public void onComplete(@NonNull Task<GetTokenResult> task) {
//                        if (task.isSuccessful()) {
//                            String idToken = task.getResult().getToken();
//                            // Send token to your backend via HTTPS
//                            // ...
//                        } else {
//                            // Handle error -> task.getException();
//                        }
//                    }
//                });
    }

    public void launchSignup(View view){
        String url = "https://www.language-exchanges.org/user/register";
        launchURL(url);
    }

    /*public void getToken(View view){
        displayToken = findViewById(R.id.displayToken);
        displayToken.setText(FirebaseInstanceId.getInstance().getToken());
        Log.d("Token: ", FirebaseInstanceId.getInstance().getToken());
    }*/

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
        //builder.setShowTitle(true);
        builder.setToolbarColor(Color.parseColor("#263038"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(CUSTOM_TAB_PACKAGE_NAME);
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


