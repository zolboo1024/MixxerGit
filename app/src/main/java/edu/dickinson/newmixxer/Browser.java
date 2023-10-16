package edu.dickinson.newmixxer;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Zolboo Erdenebaatar 11/19/2018
 * This is a class that basically implements the 
 * core part of the application: The WebView component.
 */

public class Browser extends AppCompatActivity {
    WebView webView;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        // WebView is just like any other Android Components like buttons or imageview
        webView = findViewById(R.id.webView);
        // Must enable JS and Cache 
        webView.getSettings().setJavaScriptEnabled(true);
        //Todd commented out below
        //was getting error error: cannot find symbol
        //        webView.getSettings().setAppCacheEnabled(true);
        //                             ^
        //webView.getSettings().setAppCacheEnabled(true);
        Intent intent= getIntent();
        // Here, we get URL that is passed from the MainActivity 
        // as an extra string from the intent.
        url= intent.getStringExtra("url");
        callUrl(url);
    }

    // This method basically loads the URL and handles an exception. Once 
    // an exception is thrown it automatically goes back to the main 
    // starting point of the application
    protected void callUrl(String url){
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                   //Basically, when an error is thrown, displays a message and goes back to the Activity
                    // that called this Activity.
                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "No Internet Connection. Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
