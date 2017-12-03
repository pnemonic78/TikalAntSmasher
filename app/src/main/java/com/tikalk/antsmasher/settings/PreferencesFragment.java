package com.tikalk.antsmasher.settings;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.SeekBarPreference;
import android.util.Log;

import com.tikalk.antsmasher.R;


public class PreferencesFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "TAG_" + PreferencesFragment.class.getSimpleName();

    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getPreferenceScreen().getSharedPreferences();
        preferences.registerOnSharedPreferenceChangeListener(this);

        SeekBarPreference accuracyPreference = (SeekBarPreference) getPreferenceScreen().findPreference("accuracyPrefKey");

    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }


    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    updatePreferenceSummary(preferenceGroup.getPreference(j));
                }
            } else {
                updatePreferenceSummary(preference);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.i(TAG, "onSharedPreferenceChanged");
        //   Toast.makeText(getActivity(), "Prefs changed..", Toast.LENGTH_LONG).show();
        updatePreferenceSummary(findPreference(key));
    }

    private void updatePreferenceSummary(Preference preference) {

        //If the device id changed to empty or null, it should be revert to the default value which is the uniqueID that created when app was
        //installed.
//        if (preference.getKey().equals(getString(R.string.com_tikalk_sensorsui_prefs_device_id))) {
//            String prefDeviceID = preferences.getString(getString(R.string.com_tikalk_sensorsui_prefs_device_id), null);
//            if (prefDeviceID == null || prefDeviceID.isEmpty()) {
//                return;
//            }
//        }

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
        }

//        if (preference instanceof EditTextPreference) {
//            EditTextPreference editTextPreference = (EditTextPreference) preference;
//            String prefValue = preferences.getString(preference.getKey(), null);
//            Log.i(TAG, "EditTextPref changed to " + prefValue);
//            editTextPreference.setSummary(prefValue);
//        }
//
//        if (preference.getKey().equals(getString(R.string.com_tikalk_sensorsui_admin_mode_pref_key))) {
//            Boolean adminMode = preferences.getBoolean(preference.getKey(), false);
//            String uniqueId = preferences.getString(getString(R.string.com_tikalk_sensorsui_prefs_unique_id_key), null);
//            String deviceId = preferences.getString(getString(R.string.com_tikalk_sensorsui_prefs_device_id), null);
//
//            if (!adminMode && (uniqueId != null && !uniqueId.equals(deviceId))) {
//                {
//                    Log.i(TAG, "Admin mode canceled, device ID changed, reset it to original");
//                    resetDeviceId(getPreferenceScreen().findPreference(getString(R.string.com_tikalk_sensorsui_prefs_device_id)));
//                }
//            }
//
//        }
    }


    @Override
    public void onStop() {
        super.onStop();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

