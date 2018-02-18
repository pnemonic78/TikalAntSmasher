package com.tikalk.antsmasher.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tikalk.antsmasher.R;

public class PrefsSubScreenFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "TAG_SubSPrefsFragment";
    public static final String PAGE_ID = "page_id";

    SharedPreferences preferences;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pageId the preferences sub screen id.
     * @return A new instance of fragment PrefsSubScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrefsSubScreenFragment newInstance(String pageId) {
        PrefsSubScreenFragment fragment = new PrefsSubScreenFragment();
        Bundle b = new Bundle();

        b.putString(PAGE_ID, pageId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getPreferenceScreen().getSharedPreferences();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: " + getActivity().getSupportFragmentManager().getBackStackEntryCount());
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences.registerOnSharedPreferenceChangeListener(this);
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);

            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                Log.i(TAG, "onResume: " + preferenceGroup.getTitle());
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    if(preferenceGroup.getPreference(j).getKey() != null){
                        updatePreferenceSummary(preferenceGroup.getPreference(j));
                    }
                }
            } else {
                updatePreferenceSummary(preference);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updatePreferenceSummary(getPreferenceScreen().findPreference(s));
    }


    private void updatePreferenceSummary(Preference preference) {

        //If the device id changed to empty or null, it should be revert to the default value which is the uniqueID that created when app was
        //installed.
        String key = preference.getKey();

        if ( key.equals(getString(R.string.server_name_key))) {
            String serverName = preferences.getString(getString(R.string.server_name_key), null);
            if (serverName == null || serverName.isEmpty() || "".equals(serverName)) {
                resetServerName(preference);
                return;
            }
        }

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
        }

        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            String prefValue = preferences.getString(preference.getKey(), null);
            Log.i(TAG, "EditTextPref changed to " + prefValue);
            editTextPreference.setSummary(prefValue);
        }
    }

    private void resetServerName(Preference preference) {

        ((EditTextPreference) preference).setText(getString(R.string.default_server_name));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.server_name_key), getString(R.string.default_server_name));
        editor.commit();
    }

}
