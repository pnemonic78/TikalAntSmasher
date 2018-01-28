package com.tikalk.antsmasher.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.networking.ApiContract;

/**
 * Created by tamirnoach on 24/10/2017.
 */

public class PrefsHelper {


    private final SharedPreferences preferences;
    private Context context;

    @Inject
    public PrefsHelper(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }


    public void clear(String key) {
        preferences.edit().remove(key).apply();
    }
    public void setUserName(String value) {
        saveStringToPrefs(context.getString(R.string.user_name_key), value);
    }

    private String getString(String key) {
        return preferences.getString(key, null);
    }

    private void saveStringToPrefs(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    private String getStringPref(String key) {
        return preferences.getString(key, null);
    }

    public void saveLongToPrefs(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    private boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    private boolean getBooleanT(String key) {
        return preferences.getBoolean(key, true);
    }

    private long getLong(String key) {
        return preferences.getLong(key, 0L);
    }

    public long getUserId() {
        return getLong(context.getString(R.string.user_id_key));
    }

    public void setUserId(long value) {
        saveLongToPrefs(context.getString(R.string.user_id_key), value);
    }

    public String getUserName() {
        return getString(context.getString(R.string.user_name_key));
    }

    public boolean isInteractiveMusic() {
        return getBooleanT("interactive_music");
    }

    public boolean isInteractiveSounds() {
        return getBooleanT("interactive_sound");
    }

    public boolean isInteractiveVibrate() {
        return getBooleanT("interactive_vibrate");
    }

    public long getTeamId() {
        return getLong(context.getString(R.string.team_id_key));
    }

    public void setTeamId(long teamId) {
        saveLongToPrefs(context.getString(R.string.team_id_key), teamId);
    }

    public long getGameId() {
        return getLong(context.getString(R.string.game_id_key));
    }

    public void setGameId(long gameId) {
        saveLongToPrefs(context.getString(R.string.game_id_key), gameId);
    }

    public long getPlayerId() {
        return getLong(context.getString(R.string.player_id_key));
    }

    public void setPlayerId(long playerId) {
        saveLongToPrefs(context.getString(R.string.player_id_key), playerId);
    }

    public String getServerName() {
        return preferences.getString(context.getString(R.string.default_server_name), ApiContract.AUTHORITY);
    }
}
