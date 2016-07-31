package fragments;

import android.content.Context;
import android.os.Bundle;
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
    Double energy_to_be_produced, pv_capacity, battery, controller_current;
    int system_voltage;
    double load_Demand;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        load_Demand = getActivity().getIntent().getExtras().getDouble("load_Demand");

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
        battery_no.setText(number_of_batt+" Batteries [ "+parallel_batt+" parallel branch(es) and "+series_batt+" series branch(es) ]");

        //Inverter calculations
        TextView inverter_name = (TextView)v.findViewById(R.id.inverter_name);
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
        int parallel_modules = Utilities.roundUpDivide(Utilities.roundUpDivide(Utilities.roundUp(pv_capacity), 24), (long) 7.45);
        int series_modules = Utilities.roundUpDivide(system_voltage, 24);
        final int modules_no = parallel_modules * series_modules;
        TextView array_modules = (TextView)v.findViewById(R.id.array_modules);
        array_modules.setText(""+modules_no+" panels");

        //Contoller calculations
        TextView conrol_no = (TextView)v.findViewById(R.id.conrol_no);
//        conrol_no.setText(""+roundUpDivide((long) (8.03 * modules_no * 1.25),60));
        conrol_no.setText(""+Utilities.roundUpDivide(Utilities.roundUp(controller_current),60));

        return v;
    }


}
