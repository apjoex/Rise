package com.project.rise;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashScreen extends AppCompatActivity {

    Context context;
    @InjectView(R.id.app_label) TextView label;
    @InjectView(R.id.app_text) TextView text;
    @InjectView(R.id.start) Button start_btn;
    @InjectView(R.id.more_info)RelativeLayout more_info;

    Animation animFadein;

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
        text.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Gotham-Medium.otf"));
        start_btn.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/powerless.ttf"));

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Start.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
