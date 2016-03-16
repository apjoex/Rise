package com.project.rise;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CustomHome extends AppCompatActivity {

    Context context;
    @InjectView(R.id.appliance) TextView appliance;

    @InjectView(R.id.lightings_text) TextView lightings_text;
    @InjectView(R.id.count_1) TextView count_1;
    @InjectView(R.id.load_1) TextView load_1;
    @InjectView(R.id.cycle_1) TextView cycle_1;

    @InjectView(R.id.tv_text) TextView tv_text;
    @InjectView(R.id.count_2) TextView count_2;
    @InjectView(R.id.load_2) TextView load_2;
    @InjectView(R.id.cycle_2) TextView cycle_2;

    @InjectView(R.id.fridge_text) TextView fridge_text;
    @InjectView(R.id.count_3) TextView count_3;
    @InjectView(R.id.load_3) TextView load_3;
    @InjectView(R.id.cycle_3) TextView cycle_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        context = this;
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appliances");
        appliance.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf"));


        lightings_text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf"));
        count_1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf"));
        load_1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf"));
        cycle_1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Gotham-Medium.otf"));

        tv_text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf"));
        count_2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Gotham-Medium.otf"));
        load_2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Gotham-Medium.otf"));
        cycle_2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Gotham-Medium.otf"));

        fridge_text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf"));
        count_3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Gotham-Medium.otf"));
        load_3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Gotham-Medium.otf"));
        cycle_3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Gotham-Medium.otf"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == android.R.id.home){
            finish();
        }

        if(id == R.id.action_info){
            AlertDialog.Builder info_builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = getLayoutInflater();
            View infoView = inflater.inflate(R.layout.quick_info, null);
            TextView info = (TextView)infoView.findViewById(R.id.info_text);
            info.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf"));
            info_builder.setTitle("Quick Info")
                    .setView(infoView)
                    .create().show();
        }

        return super.onOptionsItemSelected(item);
    }
}
