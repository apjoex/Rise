package com.project.rise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import adapters.ApplianceAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import models.Appliance;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Appliances extends AppCompatActivity {

    Context context;
    @InjectView(R.id.appliance_list) RecyclerView appliance_list;
    @InjectView(R.id.cover) RelativeLayout cover;
    @InjectView(R.id.no_list) RelativeLayout no_list;
    @InjectView(R.id.back_view) CoordinatorLayout back_view;
    @InjectView(R.id.add_appliance) FloatingActionButton add_appliance;
    ArrayList<Appliance> appliances = new ArrayList<>();
    ApplianceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        context = this;
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appliances");

        checkList();
        add_appliance.setVisibility(View.INVISIBLE);


        ViewTreeObserver vto = cover.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                cover.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int cx = cover.getRight() - 80;
                int cy = cover.getBottom() - 80;

                // get the initial radius for the clipping circle
                float initialRadius = Math.max(cover.getWidth(), cover.getHeight());


                Log.d("ANIM", "" + cx + "and " + cy + " with radius" + initialRadius);

                Animator anim = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(cover, cx, cy, initialRadius, 0);
                } else {
                    cover.setVisibility(View.INVISIBLE);
                    add_appliance.show();
                }

//                if (anim != null) {
////                    anim.setDuration(300);
//                }

                if (anim != null) {
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cover.setVisibility(View.INVISIBLE);
                            add_appliance.show();
                        }
                    });
                }

                if (anim != null) {
                    anim.start();
                }

            }
        });

        adapter = new ApplianceAdapter(context, appliances);
        appliance_list.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        appliance_list.setLayoutManager(layoutManager);
        appliance_list.setAdapter(adapter);

//        addAppliance("ENTER APPLIANCE NAME", "0");
//        appliance_list.setAdapter(new SlideInBottomAnimationAdapter(listingsAdapter));
//        View footerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
//        appliance_list.addFooterView(footerView);
//        add = (Button)footerView.findViewById(R.id.add_appliance);

        add_appliance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = getLayoutInflater();
                View applianceView = inflater.inflate(R.layout.common_appliances, null);
                builder.setView(applianceView);
                builder.setTitle("Add Appliance")
                        .setPositiveButton("NEW APPLIANCE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                addAppliance("ENTER APPLIANCE NAME", "0");
                            }
                        });

//                Button new_item = (Button)applianceView.findViewById(R.id.new_item);
                RelativeLayout tv_item = (RelativeLayout) applianceView.findViewById(R.id.tv_item);
                RelativeLayout light_item = (RelativeLayout) applianceView.findViewById(R.id.light_item);
                RelativeLayout fan_item = (RelativeLayout) applianceView.findViewById(R.id.fan_item);
                RelativeLayout fridge_item = (RelativeLayout) applianceView.findViewById(R.id.fridge_item);
                RelativeLayout sound_item = (RelativeLayout) applianceView.findViewById(R.id.sound_item);
                RelativeLayout ac_item = (RelativeLayout) applianceView.findViewById(R.id.ac_item);
                RelativeLayout iron_item = (RelativeLayout) applianceView.findViewById(R.id.iron_item);

                final Dialog dialog = builder.create();

//                new_item.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        addAppliance("ENTER APPLIANCE NAME", "0");
//                    }
//                });

                tv_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        addAppliance("TELEVISION", "150");
                    }
                });

                light_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        addAppliance("LIGHT BULB", "15");
                    }
                });

                fan_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        addAppliance("FAN", "85");
                    }
                });

                fridge_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        addAppliance("REFRIGERATOR", "150");
                    }
                });

                sound_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        addAppliance("HOME THEATRE", "150");
                    }
                });

                ac_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        addAppliance("AIR CONDITIONER", "764");
                    }
                });

                iron_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        addAppliance("PRESSING IRON", "1000");
                    }
                });


                dialog.show();

            }
        });

        appliance_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    add_appliance.show();
                }

                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    add_appliance.hide();
                }

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkList();
        registerReceiver(deleteReceiver, new IntentFilter("LISTENER"));
    }

    private BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String delete_action = intent.getExtras().getString("delete_action");
            if (delete_action != null && delete_action.equals("yes")) {
                checkList();
            }
        }
    };

    private void checkList() {
        if(appliances.size() == 0){
            no_list.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Appliances");
        }else{
            no_list.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("Appliances ("+appliances.size()+")");
            appliance_list.scrollToPosition(appliances.size() - 1);
        }
    }

    private void addAppliance(String name, String watts) {
        appliances.add(new Appliance(name, "0", watts, "1"));
        adapter.notifyDataSetChanged();
        checkList();
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
            info_builder.setView(infoView)
                    .create().show();
        }

        if(id == R.id.action_proceed){
            if(appliances.size() != 0 ){
                if(validateAppliances()){
                   chooseLocation();
                }else{
                    Snackbar.make(back_view,"Please fill all the fields for your appliances",Snackbar.LENGTH_SHORT).show();
                }

            }else{
                Snackbar.make(back_view,"You are yet to add an appliance.", Snackbar.LENGTH_SHORT).setAction("ADD APPLIANCE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addAppliance("ENTER APPLIANCE NAME","0");
                    }
                }).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void chooseLocation() {
        AlertDialog.Builder stateBuilder = new AlertDialog.Builder(context);
        final CharSequence[] states = {"Abia","Adamawa","Akwa Ibom","Anambra","Bauchi","Bayelsa","Benue","Borno","Cross River","Delta","Ebonyi","Edo", "Ekiti","Enugu","FCT","Gombe","Imo","Jigawa","Kaduna","Kano","Katsina","Kebbi","Kogi","Kwara","Lagos","Nasarawa","Niger","Ogun","Ondo","Osun","Oyo","Plateau","Rivers","Sokoto","Taraba","Yobe","Zamfara"};
        stateBuilder.setTitle("Choose Location")
                .setItems(states, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(Details.this, "You selected "+states[i]+" state.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Details.class);
                        intent.putParcelableArrayListExtra("appliances", appliances);
                        intent.putExtra("state", states[i].toString());
                        intent.putExtra("state_position", i);
                        startActivity(intent);
//                        state_selected = states[i].toString();
                    }
                })
                .create().show();


    }

    private boolean validateAppliances() {
        for(int i = 0; i < appliances.size(); i++){
           if(appliances.get(i).getName().contains("ENTER") || appliances.get(i).getName().length() == 0){
               return false;
           }

            if(appliances.get(i).getCount().equals("0")){
                return false;
            }


            if(appliances.get(i).getWattage().equals("0")){
                return false;
            }

        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
