package com.project.rise;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashScreen extends AppCompatActivity {
    Context context;
    @InjectView(R.id.app_label) TextView label;
    @InjectView(R.id.app_text) TextView text;
    @InjectView(R.id.more_info)RelativeLayout more_info;

    Animation animFadein;
    public static int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ButterKnife.inject(this);

        more_info.setVisibility(View.INVISIBLE);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        more_info.startAnimation(animFadein);
        more_info.setVisibility(View.VISIBLE);

        label.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/powerless.ttf"));
        text.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Light.otf"));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, Start.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }
}
