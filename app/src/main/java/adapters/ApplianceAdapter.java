package adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.rise.R;

import java.util.ArrayList;

import models.Appliance;
import resuables.Utilities;

/**
 * Created by AKINDE-PETERS on 3/16/2016.
 */
public class ApplianceAdapter extends  RecyclerView.Adapter<ApplianceAdapter.ViewHolder> implements View.OnCreateContextMenuListener {

    Context context;
    ArrayList<Appliance> appliances;

    public ApplianceAdapter(Context context, ArrayList<Appliance> appliances){
        this.context = context;
        this.appliances = appliances;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.appliance_item_layout, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.appliance_name.setText(appliances.get(position).getName());
        viewHolder.appliance_count.setText(appliances.get(position).getCount());
        viewHolder.appliance_wattage.setText(appliances.get(position).getWattage());
        viewHolder.appliance_duration.setText(appliances.get(position).getDuration());

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
                linearLayout.setPadding(32,30,32,10);
                linearLayout.addView(textEditor, numPicerParams);

                AlertDialog.Builder editorBuilder = new AlertDialog.Builder(context);
                editorBuilder.setView(linearLayout)
                        .setCancelable(true)
                        .setTitle("Appliance Name")
                        .setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                appliances.get(position).setName("" + textEditor.getText().toString());
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

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                final AlertDialog.Builder minuteBuilder = new AlertDialog.Builder(context);

                final RelativeLayout linearLayout = new RelativeLayout(context);
                final NumberPicker aNumberPicker = new NumberPicker(context);
                aNumberPicker.setMaxValue(24);
                aNumberPicker.setMinValue(1);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
                RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                linearLayout.setLayoutParams(params);
                linearLayout.addView(aNumberPicker, numPicerParams);

                //For Minutes
                final RelativeLayout linearLayout2 = new RelativeLayout(context);
                final NumberPicker aNumberPicker2 = new NumberPicker(context);
                aNumberPicker2.setMaxValue(59);
                aNumberPicker2.setMinValue(1);

                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(50, 50);
                RelativeLayout.LayoutParams numPicerParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                numPicerParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);

                linearLayout2.setLayoutParams(params2);
                linearLayout2.addView(aNumberPicker2, numPicerParams2);

                minuteBuilder.setTitle("Daily usage (minutes)")
                        .setView(linearLayout2)
                        .setCancelable(false)
                        .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Double value = Math.round((aNumberPicker2.getValue() / 60.0) * 100.0)/100.0;
                                viewHolder.appliance_duration.setText("" + value);
                                appliances.get(position).setDuration("" + value);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNeutralButton("HOURS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ViewGroup parent = (ViewGroup) linearLayout2.getParent();
                                if (parent != null) {
                                    parent.removeView(linearLayout2);
                                }
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        });


                alertDialogBuilder.setTitle("Daily usage (hours)");
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
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNeutralButton("MINUTES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ViewGroup parent = (ViewGroup) linearLayout.getParent();
                                if (parent != null) {
                                    parent.removeView(linearLayout);
                                }
                                AlertDialog minuteDialog = minuteBuilder.create();
                                minuteDialog.show();
                            }
                        });

                if(appliances.get(position).getDuration().contains(".")){
                    aNumberPicker2.setValue(Utilities.roundUp(Double.valueOf(appliances.get(position).getDuration()) * 60.0));
                    AlertDialog minuteDialog = minuteBuilder.create();
                    minuteDialog.show();
                }else{
                    aNumberPicker.setValue(Integer.valueOf(appliances.get(position).getDuration()));
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }


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
                            linearLayout.setPadding(32,30,32,10);
                            linearLayout.addView(textEditor, numPicerParams);

                            AlertDialog.Builder editorBuilder = new AlertDialog.Builder(context);
                            editorBuilder.setView(linearLayout)
                                    .setCancelable(true)
                                    .setTitle("Appliance Name")
                                    .setPositiveButton("RENAME", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            appliances.get(position).setName("" + textEditor.getText().toString());
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
                            Intent intent = new Intent("LISTENER");
                            intent.putExtra("delete_action", "yes");
                            context.sendBroadcast(intent);
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return appliances.size();
    }

//    @Override
//    public View getView(final int position, View view, ViewGroup viewGroup) {
//        final ViewHolder viewHolder;
//
//        if(view == null){
//            viewHolder = new ViewHolder();
//            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            view = layoutInflater.inflate(R.layout.appliance_item_layout, null);
//
//            viewHolder.appliance_name = (TextView)view.findViewById(R.id.appliance_name);
//            viewHolder.appliance_count = (EditText)view.findViewById(R.id.appliance_count);
//            viewHolder.appliance_wattage = (EditText)view.findViewById(R.id.appliance_load);
//            viewHolder.appliance_duration = (EditText)view.findViewById(R.id.appliance_cycle);
//            viewHolder.more = (ImageView)view.findViewById(R.id.more);
//
//
////            viewHolder.appliance_wattage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
////                @Override
////                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
////                    if (i == EditorInfo.IME_ACTION_DONE) {
////
////                        appliances.get(position).setWattage(""+textView.getText().toString());
////
////                        return true;
////                    }
////                    return false;
////                }
////            });
//
//            view.setTag(viewHolder);
//        }else{
//            viewHolder = (ViewHolder)view.getTag();
//        }
//
//        viewHolder.appliance_name.setText(appliances.get(position).getName());
//        viewHolder.appliance_count.setText(appliances.get(position).getCount());
//        viewHolder.appliance_wattage.setText(appliances.get(position).getWattage());
//        viewHolder.appliance_duration.setText(appliances.get(position).getDuration());
//
//        return view;
//    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView appliance_name;
        EditText appliance_count, appliance_wattage, appliance_duration;
        ImageView more;

        public ViewHolder(View cardView) {
            super(cardView);
            appliance_name = (TextView)cardView.findViewById(R.id.item_name);
            appliance_count = (EditText)cardView.findViewById(R.id.appliance_count);
            appliance_wattage = (EditText)cardView.findViewById(R.id.appliance_load);
            appliance_duration = (EditText)cardView.findViewById(R.id.appliance_cycle);
            more = (ImageView)cardView.findViewById(R.id.more);
        }
    }
}
