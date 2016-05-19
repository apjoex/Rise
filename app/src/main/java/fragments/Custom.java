package fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.project.rise.Appliances;
import com.project.rise.Details;
import com.project.rise.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import adapters.HomeListAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import connectors.Database;
import models.Appliance;
import models.Home;

/**
 * Created by AKINDE-PETERS on 2/28/2016.
 */
public class Custom extends Fragment {
    Context context;
    @InjectView(R.id.body) RelativeLayout body;
    @InjectView(R.id.home_list) ListView home_list;
    @InjectView(R.id.add) FloatingActionButton add;
    Database database;
    HomeListAdapter homeListAdapter;
    ArrayList<Home> homes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_custom,container,false);
        ButterKnife.inject(this, v);
        add.setRippleColor(Color.rgb(230, 230, 230));
        clickEvents();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        database = new Database(getActivity());
        homes = database.getCustomhomes();
        database.close();
        homeListAdapter = new HomeListAdapter(getActivity(), homes);

        if(homes.size() > 0){
            body.setVisibility(View.INVISIBLE);
            home_list.setAdapter(homeListAdapter);
        }else{
            body.setVisibility(View.VISIBLE);
        }

        home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<Appliance> appliances = new ArrayList<Appliance>();

                try {
                    JSONArray source = new JSONArray(homes.get(i).getAppliances());
                    for (int k = 0; k < source.length(); k++){
                        String name = source.getJSONObject(k).getString("name");
                        String count = source.getJSONObject(k).getString("count");
                        String wattage = source.getJSONObject(k).getString("wattage");
                        String duration = source.getJSONObject(k).getString("duration");

                        appliances.add(new Appliance(name,count,wattage,duration));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(context, Details.class);
                intent.putParcelableArrayListExtra("appliances", appliances);
                intent.putExtra("state", homes.get(i).getLocation());
                intent.putExtra("state_position", getStateNumber(homes.get(i).getLocation()));
                intent.putExtra("name", homes.get(i).getName());
                intent.putExtra("saved_home", "yes");
                startActivity(intent);
            }
        });

        home_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getActivity());
                deleteBuilder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete the home configuration for "+homes.get(position).getName()+"?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                database.deleteHome(homes.get(position).getName());
                                database.close();
                                onResume();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                return true;
            }
        });
    }

    private int getStateNumber(String location) {
        int number = 0;
        switch (location){
            case "Abia":
                number = 0;
                break;
            case "Adamawa":
                number = 1;
                break;
            case "Akwa Ibom":
                number = 2;
                break;
            case "Anambra":
                number = 3;
                break;
            case "Bauchi":
                number = 4;
                break;
            case "Bayelsa":
                number = 5;
                break;
            case "Benue":
                number = 6;
                break;
            case "Borno":
                number = 7;
                break;
            case "Cross River":
                number = 8;
                break;
            case "Delta":
                number = 9;
                break;
            case "Ebonyi":
                number = 10;
                break;
            case "Edo":
                number = 11;
                break;
            case "Ekiti":
                number = 12;
                break;
            case "Enugu":
                number = 13;
                break;
            case "FCT":
                number = 14;
                break;
            case "Gombe":
                number = 15;
                break;
            case "Imo":
                number = 16;
                break;
            case "Jigawa":
                number = 17;
                break;
            case "Kaduna":
                number = 18;
                break;
            case "Kano":
                number = 19;
                break;
            case "Katsina":
                number = 20;
                break;
            case "Kebbi":
                number = 21;
                break;
            case "Kogi":
                number = 22;
                break;
            case "Kwara":
                number = 23;
                break;
            case "Lagos":
                number = 24;
                break;
            case "Nasarawa":
                number = 25;
                break;
            case "Niger":
                number = 26;
                break;
            case "Ogun":
                number = 27;
                break;
            case "Ondo":
                number = 28;
                break;
            case "Osun":
                number = 29;
                break;
            case "Oyo":
                number = 30;
                break;
            case "Plateau":
                number = 31;
                break;
            case "Rivers":
                number = 32;
                break;
            case "Sokoto":
                number = 33;
                break;
            case "Taraba":
                number = 34;
                break;
            case "Yobe":
                number = 35;
                break;
            case "Zamfara":
                number = 36;
                break;
        }

        return number;
    }

    private void clickEvents() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Appliances.class);
                startActivity(intent);
            }
        });
    }
}
