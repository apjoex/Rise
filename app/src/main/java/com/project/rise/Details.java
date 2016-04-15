package com.project.rise;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import models.Appliance;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Details extends AppCompatActivity {
    Context context;
    ArrayList<Appliance> appliances = new ArrayList<>();
    @InjectView(R.id.daily_demand_value)
    TextView daily_demand_value;
    @InjectView(R.id.chart_cover)
    LinearLayout chart_cover;
    @InjectView(R.id.calc_btn)
    Button calc_btn;
    int load_demand;

    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        context = this;
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home Details");
        appliances = (ArrayList<Appliance>) getIntent().getSerializableExtra("appliances");
        String[] NAME_LIST = new String[appliances.size()];
        double[] VALUES = new double[appliances.size()];
        int[] COLORS = new int[appliances.size()];

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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                CharSequence[] options = {"Choose State", "Use GPS coordinates"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            AlertDialog.Builder stateBuilder = new AlertDialog.Builder(context);
                            final CharSequence[] states = {"Abia","Adamawa","Akwa Ibom","Anambra","Bauchi","Bayelsa","Benue","Borno","Cross River","Delta","Ebonyi","Edo", "Ekiti","Enugu","FCT","Gombe","Imo","Jigawa","Kaduna","Kano","Katsina","Kebbi","Kogi","Kwara","Lagos","Nasarawa","Niger","Ogun","Ondo","Osun","Oyo","Plateau","Rivers","Sokoto","Taraba","Yobe","Zamfara"};
                            stateBuilder.setTitle("Choose State")
                                    .setItems(states, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(Details.this, "You selected "+states[i]+" state.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .create().show();
                        } else {
                            getLocation();
                        }
                    }
                }).setTitle("Location").create().show();
            }
        });
    }

    private void getLocation() {
        if (checkLocationEnabled()) {
            final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            final Double[] myLatitude = new Double[1];
            final Double[] myLongitude = new Double[1];
            try {
                myLatitude[0] = location.getLatitude();
                myLongitude[0] = location.getLongitude();
                Toast.makeText(Details.this, "Latitude: " + myLatitude[0] + " and Longitude: " + myLongitude[0] + "", Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {
                Toast.makeText(Details.this, "Getting your location... Please try again in some seconds", Toast.LENGTH_SHORT).show();
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000 * 30 * 1,
                        10, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                myLatitude[0] = location.getLatitude();
                                myLongitude[0] = location.getLongitude();
                                Log.d("LOCATION LAT", myLatitude[0] + "");
                                Log.d("LOCATION LONG", myLongitude[0] + "");
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                locationManager.removeUpdates(new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {

                                    }

                                    @Override
                                    public void onStatusChanged(String s, int i, Bundle bundle) {

                                    }

                                    @Override
                                    public void onProviderEnabled(String s) {

                                    }

                                    @Override
                                    public void onProviderDisabled(String s) {

                                    }
                                });
                                Toast.makeText(Details.this, "Latitude: "+ myLatitude[0] +" and Longitude: "+ myLongitude[0] +"", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {

                            }

                            @Override
                            public void onProviderEnabled(String s) {

                            }

                            @Override
                            public void onProviderDisabled(String s) {

                            }
                        });
            }

        }else{
            Snackbar.make(getWindow().getDecorView(), "GPS is not enabled", Snackbar.LENGTH_LONG).setAction("TURN ON", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            }).show();
        }
    }

//    private void getLocation() {
//        final GPSTracker tracker = new GPSTracker(context);
//        if (checkLocationEnabled()) {
//
//            Double myLatitude, myLongitude;
//
//            if (!tracker.canGetLocation()) {
//                tracker.showSettingsAlert();
//            } else {
//                myLatitude = tracker.getLatitude();
//                myLongitude = tracker.getLongitude();
//
//                if (myLatitude != 0.0 && myLongitude != 0.0) {
//                    Toast.makeText(Details.this, "Latitude: "+myLatitude+" and Longitude: "+myLongitude+"", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(Details.this, "Latitude: "+myLatitude+" and Longitude: "+myLongitude+"", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }else {
//            Snackbar.make(getWindow().getDecorView(), "GPS is not enabled", LENGTH_LONG).setAction("TURN ON", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    tracker.showSettingsAlert();
//                }
//            }).show();
//        }
//    }

    private boolean checkLocationEnabled(){
        LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER) || service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return enabled;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
//            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);

            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();

                    if (seriesSelection == null) {
//                        Toast.makeText(context, "No chart element was clicked", Toast.LENGTH_SHORT).show();
                    }
                    else {
//                        Toast.makeText(context,"Chart element data point index "+  (seriesSelection.getPointIndex()+1) + " was clicked" + " point value="+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Details.this, ""+appliances.get(seriesSelection.getPointIndex()).getName()+ " ("+seriesSelection.getValue()+" Wh)", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            mChartView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
//                    if (seriesSelection == null) {
//                        Toast.makeText(context,"No chart element was long pressed", Toast.LENGTH_SHORT);
//                        return false;
//                    }
//                    else {
//                        Toast.makeText(context, "Chart element data point index " + seriesSelection.getPointIndex() + " was long pressed", Toast.LENGTH_SHORT);
//                        return true;
//                    }
//                }
//            });
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
