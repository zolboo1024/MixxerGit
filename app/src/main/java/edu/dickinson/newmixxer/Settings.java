package edu.dickinson.newmixxer;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
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
    }

    @Override
    public void launchLogIn(View view) {
        super.launchLogIn(view);
    }

    public void launchAbout(View view) {
        launchURLFinal(url+"about-mixxer");
    }

    public void launchFAQ (View view) {
        launchURLFinal(url+"faq");
    }

    public void launchPrivacyPolicy(View view) {
        launchURLFinal(url+"privacy-policy");
    }

    public void backToMainActivity () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
