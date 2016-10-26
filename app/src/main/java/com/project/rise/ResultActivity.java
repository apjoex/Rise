package com.project.rise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import adapters.ResultPagerAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import models.Appliance;
import resuables.Utilities;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResultActivity extends AppCompatActivity {

    Context context;
    @InjectView(R.id.tablayout) TabLayout tabs;
    @InjectView(R.id.viewpager) ViewPager pager;
    CharSequence Titles[]={"RECOMMENDATIONS","COST ESTIMATE","ADVANCED INFO"};
    int Numboftabs = 3;
    ResultPagerAdapter adapter;
    ArrayList<Appliance> appliances = new ArrayList<>();
    Double load_Demand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        context = this;
        ButterKnife.inject(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);

        appliances = (ArrayList<Appliance>) getIntent().getSerializableExtra("appliances");
        load_Demand = getIntent().getExtras().getDouble("load_Demand");


        adapter = setupAdapter();
        pager.setAdapter(adapter);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(pager);
        Utilities.changeTabsFont(context, tabs);

    }

    private ResultPagerAdapter setupAdapter() {
        return new ResultPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

        if(id == R.id.action_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I just used the RISE mobile app to size up a stand-alone solar power system for my home! Check it out!");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share"));
        }

        if(id == R.id.action_pdf){
            Intent intent = new Intent(ResultActivity.this, Export.class);
            intent.putExtra("load_Demand",load_Demand);
            intent.putParcelableArrayListExtra("appliances", appliances);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
