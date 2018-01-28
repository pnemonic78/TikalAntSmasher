package com.tikalk.antsmasher.settings;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.tikalk.antsmasher.R;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback{

    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fm = getSupportFragmentManager();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null){
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.container, new PrefsFragment());
            transaction.commit();
        }
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        PrefsSubScreenFragment prefsSubScreenFragment = PrefsSubScreenFragment.newInstance(pref.getTitle().toString());
        Bundle b = new Bundle();
        b.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.getKey());
        prefsSubScreenFragment.setArguments(b);
        FragmentTransaction transaction = fm.beginTransaction().replace(R.id.container, prefsSubScreenFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        return true;
    }
}
