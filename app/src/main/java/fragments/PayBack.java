package fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rise.R;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by AKINDE-PETERS on 8/4/2016.
 */
public class PayBack extends BottomSheetDialogFragment {

    Context context;
    @InjectView(R.id.paycalc_btn) AppCompatButton paycalc_btn;
    @InjectView(R.id.tariff) EditText tariff_edittext;
    @InjectView(R.id.result_container) RelativeLayout result_container;
//    @InjectView(R.id.payback_text) TextView payback_text;
    @InjectView(R.id.savings_text) TextView savings_text;
    @InjectView(R.id.result_proper) RelativeLayout result_proper;
    Double load_demand, total_price, battery_price, payback, savings, year_price;

    static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        load_demand = getArguments().getDouble("load_Demand");
        total_price = getArguments().getDouble("total");
        battery_price = getArguments().getDouble("battery_price");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.payback, container, false);
        ButterKnife.inject(this, v);
        ColorStateList stateList =  ColorStateList.valueOf(Color.rgb(237,50,55));
        paycalc_btn.setSupportBackgroundTintList(stateList);
        paycalc_btn.setTextColor(Color.WHITE);

        result_container.setVisibility(View.INVISIBLE);
        result_proper.setVisibility(View.INVISIBLE);

        clickEvents();
        return v;
    }

    private void clickEvents() {
        paycalc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tariff_edittext.getText().length() != 0){
                    calculatePaybackSavings();
//                    Toast.makeText(getActivity(), "You spend ₦"+ NumberFormat.getNumberInstance(Locale.US).format(year_price)+" on electricity yearly!", Toast.LENGTH_SHORT).show();
                    result_container.setVisibility(View.VISIBLE);
                    new CountDownTimer(2000,500){
                        @Override
                        public void onTick(long l) {

                        }
                        @Override
                        public void onFinish() { result_proper.setVisibility(View.VISIBLE); }
                    }.start();
                }else{
                    Toast.makeText(getActivity(), "Please enter your electricity tariff", Toast.LENGTH_SHORT).show();
                }
            }
        });

        result_proper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void calculatePaybackSavings() {

        //Calculate savings
//        year_price = 365 * (load_demand / 1000) * Double.parseDouble(tariff_edittext.getText().toString());
        double gross_price = total_price + (3 * battery_price);
        double long_year_price = Double.parseDouble(tariff_edittext.getText().toString()) * 24 * (load_demand / 1000) * 365 * 20;
        savings = long_year_price - gross_price;
        savings_text.setText("₦"+ NumberFormat.getNumberInstance(Locale.US).format(savings));

        //Calculate payback
//        payback = total_price / Double.parseDouble(tariff_edittext.getText().toString()) / (365 * load_demand);
//        payback_text.setText(Utilities.roundUpTo1dp(payback)+" years");

    }

}
