package controllers;

import android.app.Application;
import android.content.Context;

import com.project.rise.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by AKINDE-PETERS on 9/29/2015.
 */
public class AppController extends Application {
    public static final String TAG = AppController.class
            .getSimpleName();

    Context context;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
}

