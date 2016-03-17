package com.project.rise;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.ApplianceAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import models.Appliance;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomHome extends AppCompatActivity {

    Context context;
    @InjectView(R.id.appliance_list) ListView appliance_list;
    ArrayList<Appliance> appliances = new ArrayList<>();
    ApplianceAdapter adapter;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        context = this;
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appliances");

        appliances.add(new Appliance("ENTER APPLIANCE NAME", "0", "0", "1"));

        adapter = new ApplianceAdapter(context, appliances);
        appliance_list.setAdapter(adapter);

        View footerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        appliance_list.addFooterView(footerView);
        add = (Button)footerView.findViewById(R.id.add_appliance);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAppliance();
            }
        });

    }

    private void addAppliance() {
        appliances.add(new Appliance("ENTER APPLIANCE NAME", "0", "0", "1"));
        adapter.notifyDataSetChanged();
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
            info_builder.setTitle("Quick Info")
                    .setView(infoView)
                    .create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
