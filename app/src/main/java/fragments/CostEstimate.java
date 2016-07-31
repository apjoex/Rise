package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    Double energy_to_be_produced, pv_capacity, battery, controller_current;
    int system_voltage;
    double load_Demand;
    int total = 0;
    int battery_amount = 0;
    int controller_amount = 0;
    int inverter_amount = 0;
    int array_amount = 0;

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
        View v =inflater.inflate(R.layout.fragment_cost_estimate,container,false);
        ButterKnife.inject(this, v);
        DatabaseReference db =  FirebaseDatabase.getInstance().getReference();

        final int finalNumber_of_batt = Utilities.roundUpDivide(battery.longValue() , 200);
        int parallel_modules = Utilities.roundUpDivide(Utilities.roundUpDivide(Utilities.roundUp(pv_capacity), 24), (long) 7.45);
        int series_modules = Utilities.roundUpDivide(system_voltage, 26);
        final int modules_no = parallel_modules * series_modules;

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

        return v;    }

    private void calculateTotal() {
        total = 0;
        total = total + battery_amount + array_amount + controller_amount + inverter_amount;
        total_cost.setText("≈ ₦ "+NumberFormat.getNumberInstance(Locale.US).format(total) );
    }

}
