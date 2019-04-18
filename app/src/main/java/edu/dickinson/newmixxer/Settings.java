package edu.dickinson.newmixxer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;

public class Settings extends MainActivity {

    String url = "https://www.language-exchanges.org/content/";
    ImageButton backButton;
    Button privacy_policy;
    Button faq;

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

        privacy_policy = findViewById(R.id.privacy_policy);
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPrivacyPolicy();
            }
        });

//        faq = findViewById(R.id.faq);
//        faq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                goToFAQ();
//            }
//        });
    }

    @Override
    public void launchLogIn(View view) {
        super.launchLogIn(view);
    }

    public void launchAbout(View view) {
        url += "about-mixxer";
        launchURLFinal(url);
    }

    public void launchFAQ (View view) {
        url += "faq";
        launchURLFinal(url);
    }

//    public void goToFAQ() {
//        Intent intent = new Intent(this, FAQ.class);
//        startActivity(intent);
//    }

    public void goToPrivacyPolicy () {
        Intent intent = new Intent(this, PrivacyPolicy.class);
        startActivity(intent);
    }

    public void backToMainActivity () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
