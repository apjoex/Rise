package fragments;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.rise.R;

/**
 * Created by AKINDE-PETERS on 3/11/2016.
 */
public class CreditsSheet extends BottomSheetDialogFragment {

    static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.credits, container, false);
    }
}