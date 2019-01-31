package edu.dickinson.newmixxer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

/**
 * Created by Zolboo Erdenebaatar 11/19/2018
 *
 */
public class MainActivity extends AppCompatActivity {
    Button btnHit;
    ProgressDialog pd;
    Button signUp;
    Button logIn;
    public static String joReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new yourDataTask().execute();
        setContentView(R.layout.activity_main);
        if(joReturn!=null && joReturn.length()>70){
            Intent browserIntent = new Intent(getApplicationContext(), Browser.class);
            browserIntent.putExtra("url", "https://mixxertest.dickinson.edu/user-search");
            startActivity(browserIntent);
        }
        else {
            signUp = (Button) findViewById(R.id.signUp);
            logIn = (Button) findViewById(R.id.logIn);
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(getApplicationContext(), Browser.class);
                    browserIntent.putExtra("url", "https://mixxertestdev.dickinson.edu/user/register");
                    startActivity(browserIntent);
                }
            });
            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(getApplicationContext(), Browser.class);
                    browserIntent.putExtra("url", "https://mixxertestdev.dickinson.edu/user/login");
                    startActivity(browserIntent);
                }
            });
        }
    }
    private class yourDataTask extends AsyncTask<String, String, String>
    {
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://mixxertestdev.dickinson.edu/current_user?_format=json");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
                joReturn= buffer.toString();
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    }
}
