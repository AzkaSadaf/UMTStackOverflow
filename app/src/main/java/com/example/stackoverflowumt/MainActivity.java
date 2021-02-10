package com.example.stackoverflowumt;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView textView;

    private static int SPLASH_SCREEN = 4000;
    public static final String SHARED_PREFERENCES_FILE = "OnBoardingFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.tvSetWelcome);

        imageView.setAnimation(topAnim);
        textView.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                startActivity(new Intent(MainActivity.this, LogIn.class));      /**CHANGE THIS TO ON BOARDING SCREEN**/
                startActivity(new Intent(MainActivity.this, OnBoarding.class));
                finish();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
                boolean isFirstTime = sharedPreferences.getBoolean("firstTime", false);

                if (isFirstTime) {

                    startActivity(new Intent(MainActivity.this, LogIn.class));
                    finish();
                }
                else {

                    SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE).edit();
                    editor.putBoolean("firstTime", true);
                    editor.commit();
                    startActivity(new Intent(MainActivity.this, OnBoarding.class));
                    finish();
                }

//                SharedPreferences settings = getSharedPreferences("PreferencesName", MODE_PRIVATE);           /**---Delete the preferences---**/
//                settings.edit().clear().commit();
            }
        }, SPLASH_SCREEN);
    }
}