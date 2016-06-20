package fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.rise.Details;
import com.project.rise.R;

import java.util.ArrayList;

import models.Appliance;

/**
 * Created by AKINDE-PETERS on 2/28/2016.
 */
public class Preset extends Fragment {
    Context context;
    Button button1, button2, button3;
    Button info1, info2, info3;
    TextView headerone, headertwo, headerthree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_preset,container,false);
        button1 = (Button) v.findViewById(R.id.button1);
        button2 = (Button) v.findViewById(R.id.button2);
        button3 = (Button) v.findViewById(R.id.button3);
        info1 = (Button) v.findViewById(R.id.info1);
        info2 = (Button) v.findViewById(R.id.info2);
        info3 = (Button) v.findViewById(R.id.info3);
        headerone = (TextView)v.findViewById(R.id.headerone);
        headertwo = (TextView)v.findViewById(R.id.headertwo);
        headerthree = (TextView)v.findViewById(R.id.headerthree);

        headerone.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"), Typeface.BOLD);
        headertwo.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"), Typeface.BOLD);
        headerthree.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf"), Typeface.BOLD);


        clickEvents();

        return v;
    }

    private void clickEvents() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation("one");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation("two");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation("three");
            }
        });

        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAssumptions();
            }
        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAssumptions();
            }
        });

        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAssumptions();
            }
        });
    }

    private void showAssumptions() {
        AlertDialog.Builder assumptionBuilder = new AlertDialog.Builder(context);
        assumptionBuilder.setTitle("Info")
                .setMessage("The following assumptions were made:\n\n" +
                        "Television - 150W - 6 hours\n" +
                        "Lighting bulbs - 15W - 6 hours\n" +
                        "Ceiling fans - 85W - 8 hours\n" +
                        "Refrigerator - 250W - 6 hours\n" +
                        "Pressing Iron - 1000W - 2 hours\n" +
                        "Washing machine - 250W - 3 hours\n" +
                        "Pumping machine - 500W - 2 hours\n" +
                        "Air conditioner - 764W - 4 hours\n")
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();

    }

    private void chooseLocation(final String position) {
        AlertDialog.Builder stateBuilder = new AlertDialog.Builder(context);
        final CharSequence[] states = {"Abia","Adamawa","Akwa Ibom","Anambra","Bauchi","Bayelsa","Benue","Borno","Cross River","Delta","Ebonyi","Edo", "Ekiti","Enugu","FCT","Gombe","Imo","Jigawa","Kaduna","Kano","Katsina","Kebbi","Kogi","Kwara","Lagos","Nasarawa","Niger","Ogun","Ondo","Osun","Oyo","Plateau","Rivers","Sokoto","Taraba","Yobe","Zamfara"};
        stateBuilder.setTitle("Choose Location")
                .setItems(states, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ArrayList<Appliance> appliances = null;
                        Intent intent = new Intent(context, Details.class);
                        switch (position) {
                            case "one":
                                appliances = new ArrayList<Appliance>();
                                appliances.add(new Appliance("Television", "1", "150", "6"));
                                appliances.add(new Appliance("Lighting bulbs", "10", "15", "8"));
                                appliances.add(new Appliance("Ceiling fans", "3", "85", "8"));
                                appliances.add(new Appliance("Refrigerator", "1", "250", "6"));
                                appliances.add(new Appliance("Pressing iron", "1", "1000", "2"));
                                intent.putExtra("name", "Home 1");
                                intent.putExtra("saved_home", "yes");
                                break;
                            case "two":
                                appliances = new ArrayList<Appliance>();
                                appliances.add(new Appliance("Television", "2", "150", "6"));
                                appliances.add(new Appliance("Lighting bulbs", "15", "15", "8"));
                                appliances.add(new Appliance("Ceiling fans", "5", "85", "8"));
                                appliances.add(new Appliance("Refrigerator", "1", "250", "6"));
                                appliances.add(new Appliance("Pressing iron", "1", "1000", "2"));
                                appliances.add(new Appliance("Washing machine", "1", "250", "4"));
                                intent.putExtra("name", "Home 2");
                                intent.putExtra("saved_home", "yes");
                                break;
                            case "three":
                                appliances = new ArrayList<Appliance>();
                                appliances.add(new Appliance("Television", "2", "150", "6"));
                                appliances.add(new Appliance("Lighting bulbs", "15", "15", "8"));
                                appliances.add(new Appliance("Ceiling fans", "5", "85", "8"));
                                appliances.add(new Appliance("Refrigerator", "1", "250", "6"));
                                appliances.add(new Appliance("Pressing iron", "1", "1000", "2"));
                                appliances.add(new Appliance("Washing machine", "1", "250", "3"));
                                appliances.add(new Appliance("Pumping machine", "1", "500", "2"));
                                appliances.add(new Appliance("Air conditioner", "2", "764", "4"));
                                intent.putExtra("name", "Home 3");
                                intent.putExtra("saved_home", "yes");
                                break;
                        }



                        intent.putParcelableArrayListExtra("appliances", appliances);
                        intent.putExtra("state", states[i].toString());
                        intent.putExtra("state_position", i);
                        startActivity(intent);

                    }
                })
                .create().show();


    }
}
