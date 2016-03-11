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

public class SplashScreen extends AppCompatActivity {

    Context context;
    TextView label, text;
    Button start_btn;
    RelativeLayout more_info;
    Animation animFadein;
    TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        label = (TextView)findViewById(R.id.app_label);
        text = (TextView)findViewById(R.id.app_text);
        start_btn = (Button)findViewById(R.id.start);
        more_info = (RelativeLayout)findViewById(R.id.more_info);
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
