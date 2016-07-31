package com.project.rise;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import adapters.ResultPagerAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import resuables.Utilities;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResultActivity extends AppCompatActivity {

    Context context;
    @InjectView(R.id.tablayout) TabLayout tabs;
    @InjectView(R.id.viewpager) ViewPager pager;
    CharSequence Titles[]={"RECOMMENDATIONS","COST ESTIMATE","PAY BACK","ADVANCED INFO"};
    int Numboftabs = 4;
    ResultPagerAdapter adapter;


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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ResultActivity.this,
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

        // create a page description
        // PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(new Rect(0, 0, 100, 100), 1).create();
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
            Toast.makeText(ResultActivity.this, "Please install a PDF reader", Toast.LENGTH_SHORT).show();
            // Instruct the user to install a PDF reader here, or something
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
