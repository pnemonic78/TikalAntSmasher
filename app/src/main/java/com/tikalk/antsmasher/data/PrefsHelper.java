package com.tikalk.antsmasher.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import static com.tikalk.antsmasher.data.PrefsConstants.*;

/**
 * Created by tamirnoach on 24/10/2017.
 */

public class PrefsHelper {

    private final SharedPreferences preferences;

    @Inject
    public PrefsHelper(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void saveUserName(final String userName) {
        preferences.edit().putString(USER_NAME, userName).apply();
    }

    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public void saveStringToPrefs(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getDeveloperTeam() {
        return preferences.getString(DEV_TEAM, null);
    }

    public void setDeveloperTeam(String value) {
        preferences.edit().putString(DEV_TEAM, value).apply();
    }
}
