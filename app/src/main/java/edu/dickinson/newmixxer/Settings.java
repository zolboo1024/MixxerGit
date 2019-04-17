package edu.dickinson.newmixxer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Settings extends MainActivity {

    String url = "https://www.language-exchanges.org/content/";
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainActivity();
            }
        });
    }

    @Override
    public void launchLogIn(View view) {
        super.launchLogIn(view);
    }

    public void launchAbout(View view) {
        url += "about-mixxer";
        launchURL(url);
    }

    public void launchPrivacyPolicy(View view) {
        url += "privacy-policy";
        launchURL(url);
    }

    public void launchFAQ (View view) {
        url += "faq";
        launchURL(url);
    }

    public void backToMainActivity () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
