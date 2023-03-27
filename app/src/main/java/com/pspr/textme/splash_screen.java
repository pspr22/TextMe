package com.pspr.textme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity {


    ImageView simage;
    TextView stext;
    Animation i,t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        simage = findViewById(R.id.simage);
        stext = findViewById(R.id.stext);
        i = AnimationUtils.loadAnimation(this,R.anim.image);
        t = AnimationUtils.loadAnimation(this,R.anim.text);

        simage.setAnimation(i);
        stext.setAnimation(t);

        CountDownTimer count = new CountDownTimer(4000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent i = new Intent(splash_screen.this,SignIn.class);
                startActivity(i);
                finish();
            }
        }.start();




    }
}