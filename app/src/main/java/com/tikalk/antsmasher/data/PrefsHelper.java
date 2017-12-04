package com.tikalk.antsmasher.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import javax.inject.Inject;

import com.tikalk.antsmasher.networking.ApiContract;

/**
 * Created by tamirnoach on 24/10/2017.
 */

public class PrefsHelper {

    public static final String USER_NAME = "user_name";
    private static final String USER_ID = "user_id";
    private static final String TEAM_ID = "team_id";
    private static final String PLAYER_ID = "player_id";
    private static final String GAME_ID = "game_id";
    public static final String BASE_IP = "base_ip";

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
        return getLong(USER_ID);
    }

    public void setUserId(long value) {
        saveLongToPrefs(USER_ID, value);
    }

    public String getUserName() {
        return getString(USER_NAME);
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
        return getLong(TEAM_ID);
    }

    public void setTeamId(long teamId) {
        saveLongToPrefs(TEAM_ID, teamId);
    }

    public long getGameId() {
        return getLong(GAME_ID);
    }

    public void setGameId(long gameId) {
        saveLongToPrefs(GAME_ID, gameId);
    }

    public long getPlayerId() {
        return getLong(PLAYER_ID);
    }

    public void setPlayerId(long playerId) {
        saveLongToPrefs(PLAYER_ID, playerId);
    }

    public String getServerAuthority() {
        return preferences.getString(BASE_IP, ApiContract.AUTHORITY);
    }

    public void setServerAuthority(String authority) {
        saveStringToPrefs(BASE_IP, authority);
    }

    public boolean isServerAuthorityEmpty() {
        return !preferences.contains(BASE_IP) || TextUtils.isEmpty(getServerAuthority());
    }
}
