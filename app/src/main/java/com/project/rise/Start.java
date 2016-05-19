package com.project.rise;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import adapters.ViewPagerAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Start extends AppCompatActivity {

    Context context;
    @InjectView(R.id.tabs) TabLayout tabs;
    @InjectView(R.id.pager) ViewPager pager;
    @InjectView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
//    View bottomSheet;
//    BottomSheetBehavior behavior;

    CharSequence Titles[]={"PRESETS","CUSTOM"};
    int Numboftabs = 2;
    ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        context = this;
        ButterKnife.inject(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        getSupportActionBar().setTitle("Rise");
        getSupportActionBar().setElevation(0);

        adapter = setupAdapter();
        pager.setAdapter(adapter);

        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        tabs.setupWithViewPager(pager);
        changeTabsFont();

//        bottomSheet = coordinatorLayout.findViewById(R.id.design_bottom_sheet);
//        behavior = BottomSheetBehavior.from(bottomSheet);
//        behavior.setPeekHeight(10);

    }

    private ViewPagerAdapter setupAdapter() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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
            showExit();
        }

        if(id == R.id.action_about){
            Intent intent = new Intent(context, About.class);
            startActivity(intent);
        }

        if(id == R.id.action_tips){
            final CharSequence[] messages = {
                    "Turn off all lights, appliances and electronics not in use.",
                    "Change to new and improved energy-efficient light bulbs.",
                    "Remember to unplug mobile phones, laptops, tablets when they are fully charged."
            };
            final int min = 0;
            final int max = 2;
            final Random r = new Random();


            final AlertDialog.Builder tipsBuilder = new AlertDialog.Builder(context);
            tipsBuilder.setTitle("Tip")
                    .setMessage(messages[r.nextInt(max - min + 1) + min])
                    .setPositiveButton("OKAY, THANKS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }

        if(id == R.id.action_feedback){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "apjoex@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on RISE");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));
        }

//        if(id == R.id.action_credit){
////            AlertDialog.Builder credit_builder = new AlertDialog.Builder(context);
////            LayoutInflater inflater = getLayoutInflater();
////            View creditView = inflater.inflate(R.layout.credits, null);
////            TextView credits = (TextView)creditView.findViewById(R.id.credit_text);
////            credits.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf"));
////            credit_builder.setTitle("Credits")
////                    .setView(creditView)
////                    .create().show();
//
//
////            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//            CreditsSheet modalBottomSheet = new CreditsSheet();
//            modalBottomSheet.show(getSupportFragmentManager(), "bottom sheet");
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showExit();
    }

    private void showExit() {
        AlertDialog.Builder exit_builder = new AlertDialog.Builder(context);
        exit_builder.setTitle("Exit")
                .setMessage("Do you really want to exit this app?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf"));
                }
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
