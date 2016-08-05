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

import resuables.Utilities;

/**
 * Created by AKINDE-PETERS on 7/8/2016.
 */
public class Recommendations extends Fragment {

    Context context;
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
        View v =inflater.inflate(R.layout.fragment_recommendation,container,false);

        int number_of_batt = Utilities.roundUpDivide(battery.longValue() , 200);
        int series_batt = Utilities.roundUp(system_voltage / 12);
        int parallel_batt = Utilities.roundUpDivide(number_of_batt , series_batt);
        number_of_batt = series_batt * parallel_batt;
        TextView battery_no = (TextView)v.findViewById(R.id.batt_no);
        battery_no.setText(number_of_batt+" Batteries ["+parallel_batt+" in parallel and "+series_batt+" in series]");

        //Inverter calculations
        TextView inverter_name = (TextView)v.findViewById(R.id.inverter_name);
        if(average_peak_power > 0 && average_peak_power < 500){
            inverter_name.setText(inverters[0]);
        }else if(average_peak_power > 500 && average_peak_power < 1000){
            inverter_name.setText(inverters[1]);
        }else if(average_peak_power > 1000 && average_peak_power < 1500){
            inverter_name.setText(inverters[2]);
        }else if(average_peak_power > 1500 && average_peak_power < 2000){
            inverter_name.setText(inverters[3]);
        }else if(average_peak_power > 2000 && average_peak_power < 2500){
            inverter_name.setText(inverters[4]);
        }else if(average_peak_power > 2500 && average_peak_power < 3000){
            inverter_name.setText(inverters[5]);
        }else if(average_peak_power > 3000 && average_peak_power < 3500){
            inverter_name.setText(inverters[6]);
        }else if(average_peak_power > 3500 && average_peak_power < 4000){
            inverter_name.setText(inverters[7]);
        }else if(average_peak_power > 4000 && average_peak_power < 5000){
            inverter_name.setText(inverters[8]);
        }else{
            inverter_name.setText(inverters[9]);
        }


        //Solar Array calculations
//        int parallel_modules = Utilities.roundUpDivide(Utilities.roundUpDivide(Utilities.roundUp(average_peak_power), 24), (long) 7.45);
//        int parallel_modules = Utilities.roundUpDivideDouble(total_dc_current,(long)7.63);
        int parallel_modules = Utilities.roundUp(total_dc_current /7.63);
        int series_modules = Utilities.roundUpDivide(system_voltage, (long)26.2);
        final int modules_no = parallel_modules * series_modules;
        TextView array_modules = (TextView)v.findViewById(R.id.array_modules);
        array_modules.setText(modules_no+" panels ["+series_modules+" Series Modules and "+parallel_modules+" Parallel Modules]");

        //Controller calculations
        TextView conrol_no = (TextView)v.findViewById(R.id.conrol_no);
//        control_no.setText(""+roundUpDivide((long) (8.03 * modules_no * 1.25),60));
        conrol_no.setText(""+Utilities.roundUpDivide(Utilities.roundUp(controller_current),60));

        return v;
    }


}
