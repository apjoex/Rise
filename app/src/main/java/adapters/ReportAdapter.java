package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.rise.R;

import java.util.ArrayList;

import models.Appliance;

/**
 * Created by AKINDE-PETERS on 8/15/2016.
 */
public class ReportAdapter extends BaseAdapter {

    Context context;
    ArrayList<Appliance> appliances;

    public ReportAdapter(Context context,ArrayList<Appliance> appliances){
        this.context = context;
        this.appliances = appliances;
    }

    @Override
    public int getCount() {
        return appliances.size();
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

            view = layoutInflater.inflate(R.layout.report_appliance_list_item, viewGroup, false);

            viewHolder.item_name = (TextView)view.findViewById(R.id.item_name);
            viewHolder.item_watt = (TextView)view.findViewById(R.id.item_watt);
            viewHolder.item_hour = (TextView)view.findViewById(R.id.item_hour);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.item_name.setText(appliances.get(i).getName()+" ("+appliances.get(i).getCount()+")");
        viewHolder.item_watt.setText(appliances.get(i).getWattage());
        viewHolder.item_hour.setText(appliances.get(i).getDuration());

        return view;
    }

    class ViewHolder{
        TextView item_name,item_watt,item_hour;
    }
}
