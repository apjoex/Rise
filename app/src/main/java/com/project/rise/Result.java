package com.project.rise;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import adapters.RecAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Result extends AppCompatActivity {

    Context context;
//    @InjectView(R.id.demanding) TextView ep;
    @InjectView(R.id.pc) TextView pc;
    @InjectView(R.id.sv) TextView sv;
    @InjectView(R.id.bbc) TextView bbc;
    @InjectView(R.id.inv) TextView inv;
    @InjectView(R.id.cont) TextView cont;
    @InjectView(R.id.recommend_btn) Button recommend_btn;
    Double energy_to_be_produced, pv_capacity, battery_bank_capacity, controller_current;
    int system_voltage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        context = this;
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Results");
        int load_Demand = getIntent().getExtras().getInt("load_Demand");

        energy_to_be_produced = (load_Demand * 1.5);
        pv_capacity = (energy_to_be_produced / 5);

//        ep.setText(""+energy_to_be_produced+" Wh");
        pc.setText(""+pv_capacity+" W");

        if(pv_capacity > 0 && pv_capacity<= 1200){
            system_voltage = 12;
            sv.setText(system_voltage+" V");
        }else if(pv_capacity > 1200 && pv_capacity<= 2400){
            system_voltage = 24;
            sv.setText(system_voltage+" V");
        }else if(pv_capacity > 2400 && pv_capacity <= 4800){
            system_voltage = 48;
            sv.setText(system_voltage+"V");
        }else{
            system_voltage = 48;
            sv.setText("> 48V");
        }

        battery_bank_capacity = (load_Demand * 3)/(0.8 * system_voltage) ;

        bbc.setText(roundUp(battery_bank_capacity)+" Ah");

        inv.setText("Input voltage: "+system_voltage+" V\nOutput voltage: 220 V or 230 V\nMaximum power: "+pv_capacity.toString()+" W");

        controller_current = pv_capacity / system_voltage;

        cont.setText("System voltage: "+system_voltage+" V\nInput and Output current: "+roundUp(controller_current)+" A");

//        Toast.makeText(Result.this, "The system voltage is "+roundTwoDecimals(pv_capacity / 1200), Toast.LENGTH_SHORT).show();
//
//        Method[] methods = CustomHome.class.getDeclaredMethods();
//        Toast.makeText(Result.this, "The first one is "+methods[0].getName(), Toast.LENGTH_SHORT).show();
        clickEvents();
    }

    private void clickEvents() {
        recommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("battery",roundUp(battery_bank_capacity));
                bundle.putInt("system_voltage",system_voltage);
                bundle.putInt("pv_capacity",roundUp(pv_capacity));
                bundle.putInt("controller_current",roundUp(controller_current));

                RecAdapter adapter = new RecAdapter();
                adapter.setArguments(bundle);

//                RecAdapter recommendationSheet = new RecAdapter(roundUp(battery_bank_capacity),system_voltage,roundUp(pv_capacity));
                adapter.show(getSupportFragmentManager(), "bottom sheet");
            }
        });
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

    public int roundUp(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Integer.valueOf(twoDForm.format(d));
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
            createPdf();
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void createPdf() {
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(FILE));
//            document.open();
//            addMetaData(document);
//            addTitlePage(document);
////            addContent(document);
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(new Rect(0, 0, 100, 100), 1).create();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1500, 2000, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        View content = findViewById(R.id.result_body);
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
