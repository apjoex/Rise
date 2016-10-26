package com.project.rise;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import adapters.ReportAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import models.Appliance;
import resuables.Utilities;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Export extends AppCompatActivity {

    Context context;
    TextView export_btn;
    TextView rise_logo_text,total_load;
    ArrayList<Appliance> appliances = new ArrayList<>();
    ListView appliance_list;
    ReportAdapter adapter;
    Double load_Demand;
    //
    CharSequence[] inverters = {"Latronics LS-512 500W 12VDC Sine Wave Inverter",
            "Latronics LS-1012 1000W 12VDC Sine Wave Inverter",
            "Latronics LS-1512 1500W 12VDC Sine Wave Inverter",
            "Latronics LS-2012 2000W 12VDC Sine Wave Inverter",
            "Latronics LS-2548 2500W 48VDC Sine Wave Inverter",
            "Latronics LS-3024 3000W 24VDC Sine Wave Inverter",
            "Latronics LS-3548 3500W 48VDC Sine Wave Inverter",
            "Latronics LS-4024 4000W 24VDC Sine Wave Inverter",
            "Latronics LS-5048 5000W 48VDC Sine Wave Inverter",
            "Latronics LS-7048 7000W 48VDC Sine Wave Inverter"};
    Double required_daily_energy_demand, average_peak_power,inverter_power, battery, controller_current, battery_efficiency, inverter_efficiency, controller_efficiency;
    Double total_dc_current, dod;
    int system_voltage, sun_hours, autonomy;
    //
    @InjectView(R.id.pc) TextView pc;
    @InjectView(R.id.sv) TextView sv;
    @InjectView(R.id.bbc) TextView bbc;
    @InjectView(R.id.inv) TextView inv;
    @InjectView(R.id.cont) TextView cont;
    @InjectView(R.id.demanding) TextView demand;
    @InjectView(R.id.array) TextView array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        context = this;
        ButterKnife.inject(this);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        appliances = (ArrayList<Appliance>) getIntent().getSerializableExtra("appliances");
        load_Demand = getIntent().getExtras().getDouble("load_Demand");
        autonomy = Integer.parseInt(preferences.getString("autonomy","3"));
        //Get battery efficiency
        switch (Integer.valueOf(preferences.getString("batt_eff", "4"))){
            case 1:
                battery_efficiency = 0.70;
                break;
            case 2:
                battery_efficiency = 0.75;
                break;
            case 3:
                battery_efficiency = 0.80;
                break;
            case 4:
                battery_efficiency = 0.85;
                break;
        }
        //Get inverter efficiency
        switch (Integer.valueOf(preferences.getString("inv_eff", "3"))){
            case 1:
                inverter_efficiency = 0.80;
                break;
            case 2:
                inverter_efficiency = 0.85;
                break;
            case 3:
                inverter_efficiency = 0.90;
                break;
            case 4:
                inverter_efficiency = 0.95;
                break;
        }
        //Get controller efficiency
        switch (Integer.valueOf(preferences.getString("cont_eff", "9"))){
            case 7:
                controller_efficiency = 0.80;
                break;
            case 8:
                controller_efficiency = 0.85;
                break;
            case 9:
                controller_efficiency = 0.90;
                break;
        }
        //Get depth of discharge
        switch (Integer.valueOf(preferences.getString("depth_of_discharge", "3"))){
            case 1:
                dod = 0.70;
                break;
            case 2:
                dod = 0.75;
                break;
            case 3:
                dod = 0.80;
                break;
            case 4:
                dod = 0.85;
                break;
        }
        //Get factor of safety
        double fos = Double.parseDouble(preferences.getString("safety_factor", "1.25"));
        sun_hours = 4;
        system_voltage = 24;

        required_daily_energy_demand = load_Demand / battery_efficiency / inverter_efficiency / controller_efficiency ;
        average_peak_power = (required_daily_energy_demand / sun_hours);
        inverter_power = (required_daily_energy_demand / sun_hours) / 0.8;
        total_dc_current = average_peak_power / system_voltage;
        battery = (load_Demand * autonomy)/dod / 12 ;
        controller_current = 8.12 * fos * (total_dc_current / 7.63);


        ///*****///
        appliance_list = (ListView)findViewById(R.id.appliance_list);
        export_btn = (TextView)findViewById(R.id.export_btn);
        total_load = (TextView)findViewById(R.id.total_load);
        rise_logo_text = (TextView)findViewById(R.id.rise_logo_text);
//        rise_logo_text.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/powerless.ttf"));

        //setting up list adapter
        adapter = new ReportAdapter(context, appliances);
        int totalHeight = 0;
        for(int i = 0; i < adapter.getCount(); i++){
            totalHeight += 50;
        }
        ViewGroup.LayoutParams params = appliance_list.getLayoutParams();
//        params.height = totalHeight + (appliance_list.getDividerHeight() *(adapter.getCount() - 1));
        params.height = totalHeight + 100;
        appliance_list.setLayoutParams(params);
        appliance_list.requestLayout();
        appliance_list.setAdapter(adapter);

        total_load.setText("Daily Energy Demand: "+load_Demand.intValue()+" W");

        ///******/////

        int number_of_batt = Utilities.roundUpDivide(battery.longValue() , 200);
        int series_batt = Utilities.roundUp(system_voltage / 12);
        int parallel_batt = Utilities.roundUpDivide(number_of_batt , series_batt);
        number_of_batt = series_batt * parallel_batt;
        TextView battery_no = (TextView)findViewById(R.id.batt_no);
        battery_no.setText(number_of_batt+" Batteries ["+parallel_batt+" in parallel and "+series_batt+" in series]");

        //Inverter calculations
        TextView inverter_name = (TextView)findViewById(R.id.inverter_name);
        if(inverter_power > 0 && inverter_power < 500){
            inverter_name.setText(inverters[0]);
        }else if(inverter_power > 500 && inverter_power < 1000){
            inverter_name.setText(inverters[1]);
        }else if(inverter_power > 1000 && inverter_power < 1500){
            inverter_name.setText(inverters[2]);
        }else if(inverter_power > 1500 && inverter_power < 2000){
            inverter_name.setText(inverters[3]);
        }else if(inverter_power > 2000 && inverter_power < 2500){
            inverter_name.setText(inverters[4]);
        }else if(inverter_power > 2500 && inverter_power < 3000){
            inverter_name.setText(inverters[5]);
        }else if(inverter_power > 3000 && inverter_power < 3500){
            inverter_name.setText(inverters[6]);
        }else if(inverter_power > 3500 && inverter_power < 4000){
            inverter_name.setText(inverters[7]);
        }else if(inverter_power > 4000 && inverter_power < 5000){
            inverter_name.setText(inverters[8]);
        }else{
            inverter_name.setText(inverters[9]);
        }


        int parallel_modules = Utilities.roundUp(total_dc_current /7.63);
        int series_modules = Utilities.roundUpDivide(system_voltage, (long)26.2);
        final int modules_no = parallel_modules * series_modules;
        TextView array_modules = (TextView)findViewById(R.id.array_modules);
        array_modules.setText(modules_no+" panels ["+series_modules+" Series Modules and "+parallel_modules+" Parallel Modules]");
        TextView conrol_no = (TextView)findViewById(R.id.conrol_no);
        conrol_no.setText(""+Utilities.roundUpDivide(Utilities.roundUp(controller_current),60));

        ///****////
        demand.setText(Utilities.roundUpTo2dp(required_daily_energy_demand)+" Wh/Day");
        pc.setText(Utilities.roundUpTo2dp(average_peak_power) +" W");
        sv.setText(system_voltage+" V");
        bbc.setText("Days of Autonomy: "+autonomy+" Days\nDepth of discharge: "+(dod*100)+"%\nCapacity: "+Utilities.roundUp(battery)+" Ah\nDC Voltage: 12 V\nEfficiency: "+(battery_efficiency*100)+"%");
        inv.setText("DC voltage: "+system_voltage+" V\nAC voltage: 220 / 230 V\nMaximum power: "+ Utilities.roundUpTo2dp(average_peak_power/0.8) +" W\nEfficiency: "+(inverter_efficiency*100)+"%");
        cont.setText("Required charge controller current: "+Utilities.roundUp(controller_current)+" A\nEfficiency: "+(controller_efficiency*100)+"%");
        array.setText("Total DC current: "+Utilities.roundUpTo2dp(total_dc_current)+" A");

        clickEvents();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    createPdf();
                } else {
                    Toast.makeText(context, "You have denied this app the permissions to generate PDF files",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void clickEvents() {
        export_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Export.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }else{
                createPdf();
            }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void createPdf() {

        // create a new document
        PdfDocument document = new PdfDocument();
        View content = findViewById(R.id.report);

        // create a page description
        // PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(new Rect(0, 0, 100, 100), 1).create();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(), content.getHeight(), 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page

        content.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);
        // add more pages
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        try {
//            document.writeTo(os);
//            document.close();
//            os.close();
//            Toast.makeText(Result.this, "good pdf time", Toast.LENGTH_SHORT).show();
//
//            Intent mShareIntent = new Intent();
//            mShareIntent.setAction(Intent.ACTION_SEND);
//            mShareIntent.setType("application/pdf");
//            // Assuming it may go via eMail:
//            mShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
//                    "Here is a PDF from PdfSend");
//            mShareIntent.putExtra(
//                    getClass().getPackage().getName() + "." + "SendPDF",
//                    os.toByteArray());
//            startActivity(mShareIntent);
//
//        } catch (IOException e) {
////            throw new RuntimeException("Error generating file", e);
//            Toast.makeText(Result.this, "no pdf time", Toast.LENGTH_SHORT).show();
//        }

        // close the document
//        document.close();
        try {
            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/RISE.pdf");
            FileOutputStream fos = new FileOutputStream(f);
            document.writeTo(fos);
            document.close();
            fos.close();
            showPdf();
        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
    }

    private void showPdf() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ "RISE.pdf");
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Export.this, "Please install a PDF reader", Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
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
