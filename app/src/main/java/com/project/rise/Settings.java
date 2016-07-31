package com.project.rise;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Settings extends AppCompatActivity {

    Context context;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

//        preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        String autonomy = preferences.getString("autonomy", "3");
//        String factor_string = preferences.getString("safety_factor","1.25");
//        Toast.makeText(Settings.this, "The number of autonomy days is "+autonomy+"\nThe safety factor is "+Double.parseDouble(factor_string) , Toast.LENGTH_SHORT).show();

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_settings, false);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_settings);


        }
    }
}
