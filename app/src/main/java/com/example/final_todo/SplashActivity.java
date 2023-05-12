package com.example.final_todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    Animation topicAnim, quotesAnim;
    TextView topictv, quotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        topictv = findViewById(R.id.splashtopic);
        quotes = findViewById(R.id.quotes);

        topicAnim = AnimationUtils.loadAnimation(this, R.anim.splashanim);
        quotesAnim = AnimationUtils.loadAnimation(this, R.anim.quoteanim);

        topictv.setAnimation(topicAnim);
        quotes.setAnimation(quotesAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                String login = sharedPreferences.getString("loginToken", null);
                Intent intent;
                if(Objects.isNull(login)) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },3000);
    }
}