package com.tikalk.antsmasher.utils;

import android.util.Patterns;

/**
 * Created by motibartov on 26/11/2017.
 */

public class Utils {

    public static boolean validateIpAddress(final String ip) {
        return Patterns.IP_ADDRESS.matcher(ip).matches();
    }

}
