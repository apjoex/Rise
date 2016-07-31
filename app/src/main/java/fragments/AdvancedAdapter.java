package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.rise.R;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by AKINDE-PETERS on 4/23/2016.
 */
public class AdvancedAdapter extends BottomSheetDialogFragment {

    Context context;
    @InjectView(R.id.pc) TextView pc;
    @InjectView(R.id.sv) TextView sv;
    @InjectView(R.id.bbc) TextView bbc;
    @InjectView(R.id.inv) TextView inv;
    @InjectView(R.id.cont) TextView cont;
    int system_voltage, pv_capacity,controller_current, load_demand;
    Double battery;


    public AdvancedAdapter()
    {}

    static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        battery = getArguments().getDouble("battery");
        system_voltage = getArguments().getInt("system_voltage");
        pv_capacity = getArguments().getInt("average_peak_power");
        controller_current = getArguments().getInt("controller_current");
        load_demand = getArguments().getInt("load_demand");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_advanced, container, false);
        ButterKnife.inject(this,v);

        //        ep.setText(""+required_daily_energy_demand+" Wh");
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


        battery = (load_demand * 3)/(0.8 * system_voltage) ;

        bbc.setText(roundUp(battery)+" Ah");

        inv.setText("Input voltage: "+system_voltage+" V\nOutput voltage: 220 V or 230 V\nMaximum power: "+pv_capacity+" W");

        controller_current = pv_capacity / system_voltage;

        cont.setText("System voltage: "+system_voltage+" V\nInput and Output current: "+roundUp(controller_current)+" A");


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