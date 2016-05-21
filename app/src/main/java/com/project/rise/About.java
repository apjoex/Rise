package com.project.rise;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fragments.CreditsSheet;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class About extends AppCompatActivity {

    Context context;
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setElevation(0);

        version = (TextView)findViewById(R.id.version_placeholder);

        version.setText("Version: "+BuildConfig.VERSION_NAME);

        AppCompatButton credit_btn = (AppCompatButton)findViewById(R.id.credit_btn);
        ColorStateList stateList =  ColorStateList.valueOf(Color.WHITE);
        credit_btn.setSupportBackgroundTintList(stateList);

        credit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreditsSheet modalBottomSheet = new CreditsSheet();
                modalBottomSheet.show(getSupportFragmentManager(), "bottom sheet");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

            if(id == android.R.id.home){
                finish();
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
