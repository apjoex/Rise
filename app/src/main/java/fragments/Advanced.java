package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.rise.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import resuables.Utilities;

/**
 * Created by AKINDE-PETERS on 7/8/2016.
 */
public class Advanced extends Fragment {

    Context context;
    @InjectView(R.id.pc) TextView pc;
    @InjectView(R.id.sv) TextView sv;
    @InjectView(R.id.bbc) TextView bbc;
    @InjectView(R.id.inv) TextView inv;
    @InjectView(R.id.cont) TextView cont;
    @InjectView(R.id.demanding) TextView demand;
    @InjectView(R.id.array) TextView array;

    Double required_daily_energy_demand, average_peak_power, battery, controller_current, battery_efficiency, inverter_efficiency, controller_efficiency;
    Double total_dc_current, dod;
    int system_voltage, sun_hours, autonomy;
    double load_Demand;

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

        double fos = Double.parseDouble(preferences.getString("safety_factor", "1.25"));
        sun_hours = 4;
        system_voltage = 24;

        required_daily_energy_demand = load_Demand / battery_efficiency / inverter_efficiency / controller_efficiency ;
        average_peak_power = (required_daily_energy_demand / sun_hours);
        total_dc_current = average_peak_power / system_voltage;
        battery = (load_Demand * autonomy)/dod / 12 ;
//        controller_current = average_peak_power / system_voltage;
        controller_current = 8.12 * fos * (total_dc_current / 7.63);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_advanced,container,false);
        ButterKnife.inject(this, v);

        demand.setText(Utilities.roundUpTo2dp(required_daily_energy_demand)+" Wh/Day");
        pc.setText(Utilities.roundUpTo2dp(average_peak_power) +" W");
        sv.setText(system_voltage+" V");
        bbc.setText("Days of Autonomy: "+autonomy+" Days\nDepth of discharge: "+(dod*100)+"%\nCapacity: "+Utilities.roundUp(battery)+" Ah\nDC Voltage: 12 V\nEfficiency: "+(battery_efficiency*100)+"%");
        inv.setText("DC voltage: "+system_voltage+" V\nAC voltage: 220 / 230 V\nMaximum power: "+ Utilities.roundUpTo2dp(average_peak_power/0.8) +" W\nEfficiency: "+(inverter_efficiency*100)+"%");
        cont.setText("Required charge controller current: "+Utilities.roundUp(controller_current)+" A\nEfficiency: "+(controller_efficiency*100)+"%");
        array.setText("Total DC current: "+Utilities.roundUpTo2dp(total_dc_current)+" A");

        return  v;
    }
}
