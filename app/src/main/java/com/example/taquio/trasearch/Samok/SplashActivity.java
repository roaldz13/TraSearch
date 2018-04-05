package com.example.taquio.trasearch.Samok;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.taquio.trasearch.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    ImageView splashImage;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
mAuth = FirebaseAuth.getInstance();
        refId();

//        animation
        Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        splashImage.startAnimation(mAnimation);

        //splash screen
//        Thread timer = new Thread() {
//            public void run() {
//                try {
//                    sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                }
//            }
//        };
//        timer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences("prefs", 0);
                boolean firstRun = settings.getBoolean("firstRun", false);
                if (!firstRun)//if running for first time
                //Splash will load for first time
                {
                    Log.d(TAG, "run: 1st");
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("firstRun", true);
                    editor.commit();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    Log.d(TAG, "run: False");
                    startActivity(new Intent(SplashActivity.this, GuestSearch.class));
                    finish();
                }
            }
        }, 3000);
    }


    public void refId(){
        splashImage = findViewById(R.id.splash_image);
    }
}
