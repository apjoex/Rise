package com.project.rise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import connectors.Database;
import models.Appliance;
import models.Home;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Details extends AppCompatActivity {
    Context context;
    ArrayList<Appliance> appliances = new ArrayList<>();
    @InjectView(R.id.daily_demand_value) TextView daily_demand_value;
    @InjectView(R.id.chart_cover) LinearLayout chart_cover;
    @InjectView(R.id.calc_btn) Button calc_btn;
    @InjectView(R.id.location) TextView location;
    String state_selected = "";
    int load_demand;
    Database database;


    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context = this;
        ButterKnife.inject(this);
        database = new Database(context);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home Details");
        appliances = (ArrayList<Appliance>) getIntent().getSerializableExtra("appliances");
        state_selected = getIntent().getStringExtra("state");
        String[] NAME_LIST = new String[appliances.size()];
        double[] VALUES = new double[appliances.size()];
        int[] COLORS = new int[appliances.size()];

        location.setText(state_selected+" state");

        load_demand = 0;

        for (int i = 0; i < appliances.size(); i++) {
            int count = Integer.valueOf(appliances.get(i).getCount());
            int load = Integer.valueOf(appliances.get(i).getWattage());
            int duration = Integer.valueOf(appliances.get(i).getDuration());

            NAME_LIST[i] = appliances.get(i).getName();
            VALUES[i] = (count * load * duration);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(230), rnd.nextInt(230), rnd.nextInt(230));
            COLORS[i] = color;

            Log.d("count", "" + count);
            Log.d("load", "" + load);
            Log.d("duration", "" + duration);
            load_demand = load_demand + (count * load * duration);
        }
        showDemand();

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
//        mRenderer.setChartTitleTextSize(20);
//        mRenderer.setLabelsTextSize(15);
//        mRenderer.setLegendTextSize(15);
        mRenderer.setLabelsColor(Color.BLACK);
        mRenderer.setMargins(new int[]{10, 10, 10, 10});
        mRenderer.setScale(1.2f);
        mRenderer.setZoomEnabled(false);
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setShowLegend(true);
        mRenderer.setLegendTextSize(25);
        mRenderer.setPanEnabled(false);
        mRenderer.setStartAngle(90);
        mRenderer.setShowLabels(false);

        for (int i = 0; i < VALUES.length; i++) {
            mSeries.add(NAME_LIST[i], VALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }

        clickEvents();
    }

    private void clickEvents() {
        calc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder stateBuilder = new AlertDialog.Builder(context);
//                final CharSequence[] states = {"Abia","Adamawa","Akwa Ibom","Anambra","Bauchi","Bayelsa","Benue","Borno","Cross River","Delta","Ebonyi","Edo", "Ekiti","Enugu","FCT","Gombe","Imo","Jigawa","Kaduna","Kano","Katsina","Kebbi","Kogi","Kwara","Lagos","Nasarawa","Niger","Ogun","Ondo","Osun","Oyo","Plateau","Rivers","Sokoto","Taraba","Yobe","Zamfara"};
//                stateBuilder.setTitle("Choose Location")
//                        .setItems(states, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(Details.this, "You selected "+states[i]+" state.", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .create().show();

                Intent intent = new Intent(context, Result.class);
                intent.putExtra("load_Demand",load_demand);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            mRenderer.setSelectableBuffer(10);

            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();

                    if (seriesSelection != null) {
                        Toast.makeText(Details.this, ""+appliances.get(seriesSelection.getPointIndex()).getName()+ " ("+seriesSelection.getValue()+" Wh)", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            layout.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        }
        else {
            mChartView.repaint();
        }

        ViewTreeObserver vto = chart_cover.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                chart_cover.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int cx = chart_cover.getWidth() / 2;
                int cy = chart_cover.getHeight() / 2;

                // get the initial radius for the clipping circle
                float initialRadius = Math.max(chart_cover.getWidth(), chart_cover.getHeight());


                Log.d("ANIM", "" + cx + "and " + cy + " with radius" + initialRadius);

                Animator anim = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(chart_cover, cx, cy, initialRadius, 0);
                }else{
                    chart_cover.setVisibility(View.INVISIBLE);
                }

                if (anim != null) {
                    anim.setDuration(200);
                }

                if (anim != null) {
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            chart_cover.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                if (anim != null) {
                    anim.start();
                }

            }
        });
    }

    private void showDemand() {

        if(load_demand < 1000){
            daily_demand_value.setText(load_demand+" Wh");
        }else{
            daily_demand_value.setText(""+ new Double(load_demand) / 1000 + " kWh");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

        if (id == R.id.action_save){
            RelativeLayout linearLayout = new RelativeLayout(context);
            final EditText textEditor = new EditText(context);
            textEditor.setHint("Enter name");

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            linearLayout.setLayoutParams(params);
            linearLayout.setPadding(32,30,32,10);
            linearLayout.addView(textEditor, numPicerParams);

            AlertDialog.Builder editorBuilder = new AlertDialog.Builder(context);
            editorBuilder.setView(linearLayout)
                    .setCancelable(true)
                    .setTitle("Save Home")
                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        if(!state_selected.equals("") && !textEditor.getText().toString().equals("")){
                            Gson gson = new GsonBuilder().create();
                            JsonArray jsArray = gson.toJsonTree(appliances).getAsJsonArray();

                            ArrayList<Home> homes = new ArrayList<Home>();
                            homes.add(new Home(textEditor.getText().toString(), String.valueOf(jsArray) , state_selected));
                            database.saveCustomHome(homes);
                            database.close();

                            Toast.makeText(context,"Your home configuration has been saved",Toast.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(getWindow().getDecorView(),"Please choose a location and enter a name to save this home configuration", Snackbar.LENGTH_SHORT).show();
                        }

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog = editorBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
