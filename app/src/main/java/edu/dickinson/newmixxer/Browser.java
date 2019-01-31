package edu.dickinson.newmixxer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Zolboo Erdenebaatar 11/19/2018
 * This is the class for the browser displayed on the
 * screen once login or signup is clicked.
 *
 * Currently uses WebView Class but there
 * might be something else better 
 *
 */
public class Browser extends AppCompatActivity {
    WebView webView;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        Intent intent= getIntent();
        url= intent.getStringExtra("url");
        callUrl(url);
    }
    protected void callUrl(String url){
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                //Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                Toast.makeText(getApplicationContext(), "No Internet Connection. Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
