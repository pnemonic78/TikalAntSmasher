package com.antsmasher.tikakl.tikalantsmasher.data;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by tamirnoach on 24/10/2017.
 */

public class PrefsHelper {

    public static final String PREF_FILE_NAME = "android_antsmasher_pref_file";

    private final SharedPreferences mPref;

    @Inject
    public PrefsHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void saveUserName(final String userName) {
        mPref.edit ().putString (PrefsConstants.USER_NAME,userName).commit ();
    }


    public String getString(String key) {
        return mPref.getString (key,null);
    }
}
