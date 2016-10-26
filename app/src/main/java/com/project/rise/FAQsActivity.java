package com.project.rise;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import adapters.FAQsAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import resuables.Utilities;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FAQsActivity extends AppCompatActivity {

    Context context;
    @InjectView(R.id.tabs) TabLayout tabs;
    @InjectView(R.id.pager) ViewPager pager;
    CharSequence Titles[]={"FAQs","Tips"};
    int Numboftabs = 2;
    FAQsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        context = this;
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        adapter = setupAdapter();
        pager.setAdapter(adapter);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setTabGravity(TabLayout.MODE_SCROLLABLE);
        tabs.setupWithViewPager(pager);
        Utilities.changeTabsFont(context, tabs);

    }

    private FAQsAdapter setupAdapter() {
        return new FAQsAdapter(getSupportFragmentManager(),Titles,Numboftabs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_faq_tips, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        if(id == R.id.action_settings){
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage("Please note that the settings screen is intended for professionals only.")
                    .setPositiveButton("UNDERSTOOD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, Settings.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("TAKE ME BACK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            dialog.show();
        }

        if(id == R.id.action_about){
            Intent intent = new Intent(context, About.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
