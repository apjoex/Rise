package com.project.rise;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Result extends AppCompatActivity {

    Context context;

//    @InjectView(R.id.recommend_btn) AppCompatButton recommend_btn;
//    @InjectView(R.id.cost_btn) AppCompatButton cost_btn;
//    @InjectView(R.id.rec_btn) AppCompatButton rec_btn;
    @InjectView(R.id.battery_cost) TextView battery_cost;
    @InjectView(R.id.inverter_cost) TextView inverter_cost;
    @InjectView(R.id.controller_cost) TextView controller_cost;
    @InjectView(R.id.array_cost) TextView array_cost;
    @InjectView(R.id.total_cost) TextView total_cost;

    Double energy_to_be_produced, pv_capacity, battery, controller_current;
    int system_voltage;
    int load_Demand;
    int total = 0;
    int battery_amount = 0;
    int controller_amount = 0;
    int inverter_amount = 0;
    int array_amount = 0;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recommendation);
        context = this;
        ButterKnife.inject(this);
//        Firebase.setAndroidContext(context);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        DatabaseReference db =  FirebaseDatabase.getInstance().getReference();

        load_Demand = getIntent().getExtras().getInt("load_Demand");

        energy_to_be_produced = (load_Demand * 1.5);
        pv_capacity = (energy_to_be_produced / 5);

        if(pv_capacity > 0 && pv_capacity<= 1200){
            system_voltage = 12;
        }else if(pv_capacity > 1200 && pv_capacity<= 2400){
            system_voltage = 24;
        }else if(pv_capacity > 2400 && pv_capacity <= 4800){
            system_voltage = 48;
        }else{
            system_voltage = 48;
        }

        battery = (load_Demand * 3)/(0.8 * system_voltage) ;
        controller_current = pv_capacity / system_voltage;

        final LinearLayout cost_body = (LinearLayout)findViewById(R.id.cost_body);

        cost_body.setVisibility(View.INVISIBLE);

        ColorStateList stateList =  ColorStateList.valueOf(Color.rgb(237,50,55));
//        recommend_btn.setSupportBackgroundTintList(stateList);
//        cost_btn.setSupportBackgroundTintList(stateList);
//        rec_btn.setSupportBackgroundTintList(stateList);
//
//        cost_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showCost();
//            }
//
//            private void showCost() {
//
//                int cx = cost_body.getRight();
//                int cy = cost_body.getTop();
//
//                Log.d("centers",cx+" and "+cy);
//
//                // get the initial radius for the clipping circle
////                float initialRadius = Math.max(cost_body.getWidth(), cost_body.getHeight());
//                float initialRadius = (float) Math.hypot(cost_body.getWidth(),cost_body.getHeight());
//
//
//                Log.d("ANIM", "" + cx + "and " + cy + " with radius" + initialRadius);
//
//                Animator anim = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(cost_body, cx, cy, 0, initialRadius);
//                }else{
//                    cost_body.setVisibility(View.VISIBLE);
//                }
//
//
//                if (anim != null) {
//                    cost_body.setVisibility(View.VISIBLE);
//                    anim.start();
//                }
//
//            }
//        });
//
//        rec_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showRecommendations();
//            }
//
//            private void showRecommendations() {
//                int cx = cost_body.getRight();
//                int cy = cost_body.getTop();
//
//                Log.d("centers",cx+" and "+cy);
//
//                // get the initial radius for the clipping circle
////                float initialRadius = Math.max(cost_body.getWidth(), cost_body.getHeight());
//                float initialRadius = (float) Math.hypot(cost_body.getWidth(),cost_body.getHeight());
//
//
//                Log.d("ANIM", "" + cx + "and " + cy + " with radius" + initialRadius);
//
//                Animator anim = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    anim = ViewAnimationUtils.createCircularReveal(cost_body, cx, cy, initialRadius, 0);
//                }else{
//                    cost_body.setVisibility(View.INVISIBLE);
//
//                }
//
////                if (anim != null) {
////                    anim.setDuration(700);
////                }
//
//                if (anim != null) {
//                    anim.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            cost_body.setVisibility(View.INVISIBLE);
//                        }
//                    });
//                }
//
//                if (anim != null) {
//                    anim.start();
//                }
//            }
//        });


        //Calculate for batteries
        int number_of_batt = roundUpnew(battery.longValue() , 200);
        int series_batt = roundUp(system_voltage / 12);
        int parallel_batt = roundUpnew(number_of_batt , series_batt);
        number_of_batt = series_batt * parallel_batt;
        TextView battery_no = (TextView)findViewById(R.id.batt_no);
        battery_no.setText(number_of_batt+" Batteries [ "+parallel_batt+" parallel branch(es) and "+series_batt+" series branch(es) ]");

        //Inverter calculations
        TextView inverter_name = (TextView)findViewById(R.id.inverter_name);
        if(pv_capacity > 0 && pv_capacity < 500){
            inverter_name.setText(inverters[0]);
        }else if(pv_capacity > 500 && pv_capacity < 1000){
            inverter_name.setText(inverters[1]);
        }else if(pv_capacity > 1000 && pv_capacity < 1500){
            inverter_name.setText(inverters[2]);
        }else if(pv_capacity > 1500 && pv_capacity < 2000){
            inverter_name.setText(inverters[3]);
        }else if(pv_capacity > 2000 && pv_capacity < 2500){
            inverter_name.setText(inverters[4]);
        }else if(pv_capacity > 2500 && pv_capacity < 3000){
            inverter_name.setText(inverters[5]);
        }else if(pv_capacity > 3000 && pv_capacity < 3500){
            inverter_name.setText(inverters[6]);
        }else if(pv_capacity > 3500 && pv_capacity < 4000){
            inverter_name.setText(inverters[7]);
        }else if(pv_capacity > 4000 && pv_capacity < 5000){
            inverter_name.setText(inverters[8]);
        }else{
            inverter_name.setText(inverters[9]);
        }


        //Solar Array calculations
        int parallel_modules = roundUpnew(roundUpnew(roundUp(pv_capacity), 24), (long) 7.45);
        int series_modules = roundUpnew(system_voltage, 24);
        final int modules_no = parallel_modules * series_modules;
        TextView array_modules = (TextView)findViewById(R.id.array_modules);
        array_modules.setText(""+modules_no+" panels");

        //Contoller calculations
        TextView conrol_no = (TextView)findViewById(R.id.conrol_no);
//        conrol_no.setText(""+roundUpDivide((long) (8.03 * modules_no * 1.25),60));
        conrol_no.setText(""+roundUpnew(roundUp(controller_current),60));

        clickEvents();

        final int finalNumber_of_batt = number_of_batt;

        db.child("prices/battery").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                battery_amount = Integer.valueOf(snapshot.getValue().toString()) * finalNumber_of_batt;
                battery_cost.setText("≈ ₦ "+ NumberFormat.getNumberInstance(Locale.US).format(battery_amount));
                calculateTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Result.this, "Nothing for Firebase o", Toast.LENGTH_SHORT).show();
            }
        });

        db.child("prices/controller").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                controller_amount = Integer.valueOf(snapshot.getValue().toString()) * roundUpnew(roundUp(controller_current),60);
                controller_cost.setText("≈ ₦ "+NumberFormat.getNumberInstance(Locale.US).format(controller_amount));
                calculateTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Result.this, "Nothing for Firebase o", Toast.LENGTH_SHORT).show();
            }
        });

        db.child("prices/inverter1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                inverter_amount =  Integer.valueOf(snapshot.getValue().toString());
                inverter_cost.setText("≈ ₦ "+NumberFormat.getNumberInstance(Locale.US).format(inverter_amount));
                calculateTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Result.this, "Nothing for Firebase o", Toast.LENGTH_SHORT).show();
            }
        });

        db.child("prices/solar array").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                array_amount =  Integer.valueOf(snapshot.getValue().toString()) * modules_no;
                array_cost.setText("≈ ₦ "+ NumberFormat.getNumberInstance(Locale.US).format(array_amount));
                calculateTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Result.this, "Nothing for Firebase o", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void calculateTotal() {
        total = 0;
        total = total + battery_amount + array_amount + controller_amount + inverter_amount;
        total_cost.setText("≈ ₦ "+NumberFormat.getNumberInstance(Locale.US).format(total) );
    }

    private void addTotal(int amount) {
        total = total + amount;

        total_cost.setText("≈ ₦ "+NumberFormat.getNumberInstance(Locale.US).format(total) );
    }

    private void clickEvents() {
//        recommend_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putDouble("battery",battery);
//                bundle.putInt("system_voltage",system_voltage);
//                bundle.putInt("pv_capacity",roundUp(pv_capacity));
//                bundle.putInt("controller_current",roundUp(controller_current));
//                bundle.putInt("load_demand",load_Demand);
//
//                AdvancedAdapter adapter = new AdvancedAdapter();
//                adapter.setArguments(bundle);
//                adapter.show(getSupportFragmentManager(), "bottom sheet");
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            menu.getItem(0).setVisible(true);
        }else{
            menu.getItem(0).setVisible(false);
        }

        return true;
    }

    public int roundUpnew(long num, long divisor)
    {
        return (int) ((num + divisor - 1) / divisor);
    }

    public int roundUp(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Integer.valueOf(twoDForm.format(d));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    createPdf();
                } else {
                    Toast.makeText(context, "You have denied this app the permissions to generate PDF files .",Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Result.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }else{
                createPdf();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void createPdf() {

        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(new Rect(0, 0, 100, 100), 1).create();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1500, 2000, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        View content = findViewById(R.id.rec_body);
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
            Toast.makeText(Result.this, "Please install a PDF reader", Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
