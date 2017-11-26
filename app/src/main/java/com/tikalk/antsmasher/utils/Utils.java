package com.tikalk.antsmasher.utils;

import java.util.regex.Pattern;

/**
 * Created by motibartov on 26/11/2017.
 */

public class Utils {

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validateIpAddress(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

}
