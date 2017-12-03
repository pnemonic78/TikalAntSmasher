package com.tikalk.antsmasher.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Patterns;

/**
 * Created by motibartov on 26/11/2017.
 */

public class Utils {

    public static boolean validateIpAddress(final String ip) {
        return Patterns.IP_ADDRESS.matcher(ip).matches();
    }

    public static void restartApp(Activity activity) {
        final Context context = activity;
        PackageManager packageManager = context.getPackageManager();
        Intent launch = packageManager.getLaunchIntentForPackage(context.getPackageName());
        Intent intent = Intent.makeRestartActivityTask(launch.getComponent());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        activity.finish();
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 300, pendingIntent);
        System.exit(0);
    }
}
