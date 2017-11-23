package com.tikalk.antsmasher.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

/**
 * Created by tamirnoach on 24/10/2017.
 */

public class PrefsHelper {

    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String DEV_TEAM = "dev_team";

    private final SharedPreferences preferences;

    @Inject
    public PrefsHelper(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void setUserName(String value) {
        saveStringToPrefs(USER_NAME, value);
    }

    private String getString(String key) {
        return preferences.getString(key, null);
    }

    private void saveStringToPrefs(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getDeveloperTeam() {
        return preferences.getString(DEV_TEAM, null);
    }

    public void setDeveloperTeam(String value) {
        preferences.edit().putString(DEV_TEAM, value).apply();
    }

    public String getUserId() {
        return getString(USER_ID);
    }

    public void setUserId(String value) {
        saveStringToPrefs(USER_ID, value);
    }

    public String getUserName() {
        return getString(USER_NAME);
    }
}
