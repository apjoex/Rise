package fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.rise.CustomHome;
import com.project.rise.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by AKINDE-PETERS on 2/28/2016.
 */
public class Custom extends Fragment {
    Context context;
    @InjectView(R.id.placeholder) TextView placeholder;
    @InjectView(R.id.add) FloatingActionButton add;

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

    private void clickEvents() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomHome.class);
                startActivity(intent);
            }
        });
    }
}
