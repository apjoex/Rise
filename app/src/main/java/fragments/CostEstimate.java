package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.rise.R;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import resuables.Utilities;

/**
 * Created by AKINDE-PETERS on 7/8/2016.
 */
public class CostEstimate extends Fragment {

    Context context;
    @InjectView(R.id.battery_cost) TextView battery_cost;
    @InjectView(R.id.inverter_cost) TextView inverter_cost;
    @InjectView(R.id.controller_cost) TextView controller_cost;
    @InjectView(R.id.array_cost) TextView array_cost;
    @InjectView(R.id.total_cost) TextView total_cost;
    @InjectView(R.id.payback_btn) AppCompatButton payback_btn;

    Double required_daily_energy_demand, average_peak_power, battery, controller_current, battery_efficiency, inverter_efficiency, controller_efficiency;
    Double total_dc_current, dod;
    int system_voltage, sun_hours, autonomy;
    double load_Demand;
    double total = 0;
    int battery_amount = 0;
    int controller_amount = 0;
    int inverter_amount = 0;
    int array_amount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        load_Demand = getActivity().getIntent().getExtras().getDouble("load_Demand");
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
        total_dc_current = average_peak_power / system_voltage;
        battery = (load_Demand * autonomy)/dod / 12 ;
        controller_current = 8.12 * fos * (total_dc_current / 7.63);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_cost_estimate,container,false);
        ButterKnife.inject(this, v);
        DatabaseReference db =  FirebaseDatabase.getInstance().getReference();

        ColorStateList stateList =  ColorStateList.valueOf(Color.rgb(237,50,55));
        payback_btn.setSupportBackgroundTintList(stateList);
        payback_btn.setTextColor(Color.WHITE);
        payback_btn.setVisibility(View.INVISIBLE);

//        final int finalNumber_of_batt = Utilities.roundUpDivide(battery.longValue() , 200);

        //Calculation for battery
        int number_of_batt = Utilities.roundUpDivide(battery.longValue() , 200);
        int series_batt = Utilities.roundUp(system_voltage / 12);
        int parallel_batt = Utilities.roundUpDivide(number_of_batt , series_batt);
        number_of_batt = series_batt * parallel_batt;

//        int parallel_modules = Utilities.roundUpDivide(Utilities.roundUpDivide(Utilities.roundUp(pv_capacity), 24), (long) 7.45);
//        int series_modules = Utilities.roundUpDivide(system_voltage, 26);
//        final int modules_no = parallel_modules * series_modules;

        int parallel_modules = Utilities.roundUp(total_dc_current /7.63);
        int series_modules = Utilities.roundUpDivide(system_voltage, (long)26.2);
        final int modules_no = parallel_modules * series_modules;

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
                Toast.makeText(getActivity(), "Nothing for Firebase o", Toast.LENGTH_SHORT).show();
            }
        });

        db.child("prices/controller").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                controller_amount = Integer.valueOf(snapshot.getValue().toString()) * Utilities.roundUpDivide(Utilities.roundUp(controller_current),60);
                controller_cost.setText("≈ ₦ "+NumberFormat.getNumberInstance(Locale.US).format(controller_amount));
                calculateTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Nothing for Firebase o", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Nothing for Firebase o", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Nothing for Firebase o", Toast.LENGTH_SHORT).show();
            }

        });

        clickEvents();

        return v;    }

    private void clickEvents() {
        payback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayBack modalBottomSheet = new PayBack();
                Bundle bundle = new Bundle();
                bundle.putDouble("load_Demand",load_Demand);
                bundle.putDouble("total",total);
                bundle.putDouble("battery_price",battery_amount);
                modalBottomSheet.setArguments(bundle);
                modalBottomSheet.show(getActivity().getSupportFragmentManager(), "bottom sheet");
            }
        });
    }

    private void calculateTotal() {
        total = 0;
        total = total + battery_amount + array_amount + controller_amount + inverter_amount;
        total = (1.2 * total);
        total_cost.setText("≈ ₦ "+NumberFormat.getNumberInstance(Locale.US).format(total));
        total_cost.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/Montserrat-Light.otf"), Typeface.BOLD);
        payback_btn.setVisibility(View.VISIBLE);
    }



}
