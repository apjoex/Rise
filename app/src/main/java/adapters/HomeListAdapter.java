package adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.rise.R;

import java.util.ArrayList;

import models.Home;

/**
 * Created by AKINDE-PETERS on 4/17/2016.
 */
public class HomeListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Home> homes;

    public HomeListAdapter(Context context, ArrayList<Home> homes){
        this.context = context;
        this.homes = homes;
    }


    @Override
    public int getCount() {
        return homes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.customhome_item, null);

            viewHolder.home_name = (TextView)view.findViewById(R.id.home_name);
            viewHolder.home_location = (TextView)view.findViewById(R.id.home_location);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.home_name.setText(homes.get(i).getName());
        viewHolder.home_location.setText(homes.get(i).getLocation());


        return view;
    }

    class ViewHolder{
        TextView home_name,home_location;
        CardView view;
    }
}
