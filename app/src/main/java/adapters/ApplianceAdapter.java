package adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.rise.R;

import java.util.ArrayList;

import models.Appliance;

/**
 * Created by AKINDE-PETERS on 3/16/2016.
 */
public class ApplianceAdapter extends BaseAdapter {

    Context context;
    ArrayList<Appliance> appliances;

    public ApplianceAdapter(Context context, ArrayList<Appliance> appliances){
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.appliance_item_layout, null);

            viewHolder.appliance_name = (TextView)view.findViewById(R.id.appliance_name);
            viewHolder.appliance_count = (EditText)view.findViewById(R.id.appliance_count);
            viewHolder.appliance_wattage = (EditText)view.findViewById(R.id.appliance_load);
            viewHolder.appliance_duration = (EditText)view.findViewById(R.id.appliance_cycle);
            viewHolder.more = (ImageView)view.findViewById(R.id.more);

            viewHolder.appliance_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout linearLayout = new RelativeLayout(context);
                    final EditText textEditor = new EditText(context);
                    if(viewHolder.appliance_name.getText().toString().equals("ENTER APPLIANCE NAME")){
                        textEditor.setHint(""+viewHolder.appliance_name.getText().toString());
                    }else{
                        textEditor.setText(""+viewHolder.appliance_name.getText().toString());
                    }

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    linearLayout.setLayoutParams(params);
                    linearLayout.setPadding(16,30,16,10);
                    linearLayout.addView(textEditor, numPicerParams);

                    AlertDialog.Builder editorBuilder = new AlertDialog.Builder(context);
                    editorBuilder.setView(linearLayout)
                            .setCancelable(true)
                            .setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    appliances.get(position).setName(""+textEditor.getText().toString());
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = editorBuilder.create();
                    alertDialog.show();
                }
            });

            viewHolder.appliance_duration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout linearLayout = new RelativeLayout(context);
                    final NumberPicker aNumberPicker = new NumberPicker(context);
                    aNumberPicker.setMaxValue(24);
                    aNumberPicker.setMinValue(1);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
                    RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    linearLayout.setLayoutParams(params);
                    linearLayout.addView(aNumberPicker, numPicerParams);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Duration of Use (in hours)");
                    alertDialogBuilder.setView(linearLayout);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("SET",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Log.e("", "New Quantity Value : " + aNumberPicker.getValue());
                                            viewHolder.appliance_duration.setText("" + aNumberPicker.getValue());
                                            appliances.get(position).setDuration("" + "" + aNumberPicker.getValue());
                                            notifyDataSetChanged();
                                        }
                                    })
                            .setNegativeButton("CANCEL",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

            viewHolder.appliance_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout linearLayout = new RelativeLayout(context);
                    final EditText textEditor = new EditText(context);
                    textEditor.setInputType(InputType.TYPE_CLASS_NUMBER);

                    if(!viewHolder.appliance_count.getText().toString().equals("0")){
                        textEditor.setText(""+viewHolder.appliance_count.getText().toString());
                    }else{
                        textEditor.setHint("Enter count");
                    }

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    linearLayout.setLayoutParams(params);
                    linearLayout.setPadding(32, 30, 32, 10);
                    linearLayout.addView(textEditor, numPicerParams);

                    AlertDialog.Builder editorBuilder = new AlertDialog.Builder(context);
                    editorBuilder.setView(linearLayout)
                            .setTitle("Count")
                            .setCancelable(true)
                            .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    appliances.get(position).setCount("" + textEditor.getText().toString());
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = editorBuilder.create();
                    alertDialog.show();
                }
            });

            viewHolder.appliance_wattage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout linearLayout = new RelativeLayout(context);
                    final EditText textEditor = new EditText(context);
                    textEditor.setInputType(InputType.TYPE_CLASS_NUMBER);

                    if(!viewHolder.appliance_wattage.getText().toString().equals("0")){
                        textEditor.setText(""+viewHolder.appliance_wattage.getText().toString());
                    }else{
                        textEditor.setHint("Enter AC Load Wattage");
                    }

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    linearLayout.setLayoutParams(params);
                    linearLayout.setPadding(32,30,32,10);
                    linearLayout.addView(textEditor, numPicerParams);

                    AlertDialog.Builder editorBuilder = new AlertDialog.Builder(context);
                    editorBuilder.setView(linearLayout)
                            .setTitle("AC Load Wattage")
                            .setCancelable(true)
                            .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    appliances.get(position).setWattage("" + textEditor.getText().toString());
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = editorBuilder.create();
                    alertDialog.show();
                }
            });

            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_more, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            if(id == R.id.action_rename){
                                RelativeLayout linearLayout = new RelativeLayout(context);
                                final EditText textEditor = new EditText(context);
                                if(viewHolder.appliance_name.getText().toString().equals("ENTER APPLIANCE NAME")){
                                    textEditor.setHint(""+viewHolder.appliance_name.getText().toString());
                                }else{
                                    textEditor.setText(""+viewHolder.appliance_name.getText().toString());
                                }

                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                                linearLayout.setLayoutParams(params);
                                linearLayout.setPadding(16,30,16,10);
                                linearLayout.addView(textEditor, numPicerParams);

                                AlertDialog.Builder editorBuilder = new AlertDialog.Builder(context);
                                editorBuilder.setView(linearLayout)
                                        .setCancelable(true)
                                        .setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                appliances.get(position).setName(""+textEditor.getText().toString());
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = editorBuilder.create();
                                alertDialog.show();
                            }

                            if(id == R.id.action_delete){
                                appliances.remove(appliances.get(position));
                                notifyDataSetChanged();
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });

//            viewHolder.appliance_wattage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                    if (i == EditorInfo.IME_ACTION_DONE) {
//
//                        appliances.get(position).setWattage(""+textView.getText().toString());
//
//                        return true;
//                    }
//                    return false;
//                }
//            });

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.appliance_name.setText(appliances.get(position).getName());
        viewHolder.appliance_count.setText(appliances.get(position).getCount());
        viewHolder.appliance_wattage.setText(appliances.get(position).getWattage());
        viewHolder.appliance_duration.setText(appliances.get(position).getDuration());

        return view;
    }


    class ViewHolder{
        TextView appliance_name;
        EditText appliance_count, appliance_wattage, appliance_duration;
        ImageView more;
    }
}
