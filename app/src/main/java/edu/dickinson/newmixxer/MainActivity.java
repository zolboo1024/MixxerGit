package edu.dickinson.newmixxer;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    ProgressDialog pd; //opening up this Activity might take a while. So this is a dialog that spins while the app is loading
    Button signUp; //sign up Button, same as logIn
    Button logIn;
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    public static CustomTabsClient mCustomTabsClient;
    public static CustomTabsSession mCustomTabsSession;
    //public static String joReturn;    //was useful when we used the method "yourDataTask". No longer useful
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp = (Button) findViewById(R.id.signUp);//register the buttons
        logIn = (Button) findViewById(R.id.logIn);
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
    }
    public void launchLogIn(View view){
        String url = "https://www.language-exchanges.org/user/login";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.parseColor("#263038"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
    public void launchSignup(View view){
        String url = "https://www.language-exchanges.org/user/register";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
