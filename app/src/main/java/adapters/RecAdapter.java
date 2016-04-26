package adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.rise.R;

import java.text.DecimalFormat;

/**
 * Created by AKINDE-PETERS on 4/23/2016.
 */
public class RecAdapter extends BottomSheetDialogFragment {

    Context context;
    int battery, system_voltage, pv_capacity,controller_current;

//    public RecAdapter(int battery_capacity, int system_voltage, int pv_capacity)
//    {
//        battery = battery_capacity;
//        this.system_voltage = system_voltage;
//        this.pv_capacity = pv_capacity;
//    }

    public RecAdapter()
    {}

    static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        battery = getArguments().getInt("battery");
        system_voltage = getArguments().getInt("system_voltage");
        pv_capacity = getArguments().getInt("pv_capacity");
        controller_current = getArguments().getInt("controller_current");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.recommendation, container, false);

        final LinearLayout cost_body = (LinearLayout) v.findViewById(R.id.cost_body);
        final LinearLayout rec_body = (LinearLayout)v.findViewById(R.id.rec_body);
        AppCompatButton cost_btn = (AppCompatButton) v.findViewById(R.id.cost_btn);
        AppCompatButton rec_btn = (AppCompatButton) v.findViewById(R.id.rec_btn);
        cost_body.setVisibility(View.INVISIBLE);

        ColorStateList stateList =  ColorStateList.valueOf(Color.WHITE);
        cost_btn.setSupportBackgroundTintList(stateList);
        rec_btn.setSupportBackgroundTintList(stateList);

        cost_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        cost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCost();
            }

            private void showCost() {

//                ViewTreeObserver vto = cost_body.getViewTreeObserver();
//                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//                        cost_body.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//
//
//                    }
//                });
                int cx = cost_body.getRight();
                int cy = cost_body.getTop();

                Log.d("centers",cx+" and "+cy);

                // get the initial radius for the clipping circle
//                float initialRadius = Math.max(cost_body.getWidth(), cost_body.getHeight());
                float initialRadius = (float) Math.hypot(cost_body.getWidth(),cost_body.getHeight());


                Log.d("ANIM", "" + cx + "and " + cy + " with radius" + initialRadius);

                Animator anim = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(cost_body, cx, cy, 0, initialRadius);
                }else{
                    cost_body.setVisibility(View.VISIBLE);
                }

//                if (anim != null) {
//                    anim.setDuration(700);
//                }

//                if (anim != null) {
//                    anim.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            cost_body.setVisibility(View.VISIBLE);
//                        }
//                    });
//                }

                if (anim != null) {
//                    rec_body.setVisibility(View.INVISIBLE);
                    cost_body.setVisibility(View.VISIBLE);
                    anim.start();
                }

            }
        });

        rec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecommendations();
            }

            private void showRecommendations() {
                int cx = cost_body.getRight();
                int cy = cost_body.getTop();

                Log.d("centers",cx+" and "+cy);

                // get the initial radius for the clipping circle
//                float initialRadius = Math.max(cost_body.getWidth(), cost_body.getHeight());
                float initialRadius = (float) Math.hypot(cost_body.getWidth(),cost_body.getHeight());


                Log.d("ANIM", "" + cx + "and " + cy + " with radius" + initialRadius);

                Animator anim = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(cost_body, cx, cy, initialRadius, 0);
                }else{
                    cost_body.setVisibility(View.INVISIBLE);

                }

//                if (anim != null) {
//                    anim.setDuration(700);
//                }

                if (anim != null) {
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            cost_body.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                if (anim != null) {
                    anim.start();
                }
            }
        });


        //Calculate for batteries
        int number_of_batt = roundUpnew(battery , 200);
        int series_batt = roundUp(system_voltage / 12);
        int parallel_batt = roundUpnew(number_of_batt , series_batt);
        number_of_batt = series_batt * parallel_batt;
        TextView battery_no = (TextView)v.findViewById(R.id.batt_no);
        battery_no.setText(number_of_batt+" Batteries ("+parallel_batt+" parallel branches and "+series_batt+" series branches)");

        //Inverter calculations
        TextView inverter_name = (TextView)v.findViewById(R.id.inverter_name);

        if(pv_capacity > 0 && pv_capacity < 500){
            inverter_name.setText("Latronics LS-512 500W 12VDC Sine Wave Inverter");
        }else if(pv_capacity > 500 && pv_capacity < 1000){
            inverter_name.setText("Latronics LS-1012 1000W 12VDC Sine Wave Inverter");
        }else if(pv_capacity > 1000 && pv_capacity < 1500){
            inverter_name.setText("Latronics LS-1512 1500W 12VDC Sine Wave Inverter");
        }else if(pv_capacity > 1500 && pv_capacity < 2000){
            inverter_name.setText("Latronics LS-2012 2000W 12VDC Sine Wave Inverter");
        }else if(pv_capacity > 2000 && pv_capacity < 2500){
            inverter_name.setText("Latronics LS-2548 2500W 48VDC Sine Wave Inverter");
        }else if(pv_capacity > 2500 && pv_capacity < 3000){
            inverter_name.setText("Latronics LS-3024 3000W 24VDC Sine Wave Inverter");
        }else if(pv_capacity > 3000 && pv_capacity < 3500){
            inverter_name.setText("Latronics LS-3548 3500W 48VDC Sine Wave Inverter");
        }else if(pv_capacity > 3500 && pv_capacity < 4000){
            inverter_name.setText("Latronics LS-4024 4000W 24VDC Sine Wave Inverter");
        }else if(pv_capacity > 4000 && pv_capacity < 5000){
            inverter_name.setText("Latronics LS-5048 5000W 48VDC Sine Wave Inverter");
        }else{
            inverter_name.setText("Latronics LS-7048 7000W 48VDC Sine Wave Inverter");
        }


        //Solar Array calculations
        int parallel_modules = roundUpnew(roundUpnew(pv_capacity, 24), (long) 7.45);
        int series_modules = roundUpnew(system_voltage, 24);
        int modules_no = parallel_modules * series_modules;
        TextView array_modules = (TextView)v.findViewById(R.id.array_modules);
        array_modules.setText(""+modules_no+" panels");

        //Contoller calculations
        TextView conrol_no = (TextView)v.findViewById(R.id.conrol_no);
//        conrol_no.setText(""+roundUpnew((long) (8.03 * modules_no * 1.25),60));
        conrol_no.setText(""+roundUpnew(controller_current,60));

        return v;
    }

    public int roundUp(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Integer.valueOf(twoDForm.format(d));
    }

    public int roundUpnew(long num, long divisor)
    {
        return (int) ((num + divisor - 1) / divisor);
    }
}