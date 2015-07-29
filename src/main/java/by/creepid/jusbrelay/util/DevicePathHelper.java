/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rusakovich
 *
 * @see
 * http://community.silabs.com/t5/Interface-Knowledge-Base/Windows-USB-Device-Path/ta-p/114059
 */
public class DevicePathHelper {

    private static final Pattern SERIAL_PATTERN = Pattern.compile("&[0-9]{4}#");
    private static final Pattern GUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    private DevicePathHelper() {
    }

    public static Integer getSerial(String path) {
        if (path == null) {
            return null;
        }
        Matcher matcher = SERIAL_PATTERN.matcher(path);
        if (matcher.find()) {
            String serialStr = matcher.group(0).substring(1, 5);
            return Integer.parseInt(serialStr);
        } else {
            return null;
        }
    }

    public static String getDriverGUID(String path) {
        if (path == null) {
            return null;
        }

        Matcher matcher = GUID_PATTERN.matcher(path);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return null;
        }
    }

    public static String getVID(String path) {
        if (path == null) {
            return null;
        }

        int startIndex = path.indexOf("vid_");
        int endIndex = path.indexOf("&pid");
        return path.substring(startIndex + 4, endIndex);
    }

    public static String getPID(String path) {
        if (path == null) {
            return null;
        }

        int startIndex = path.indexOf("pid_");
        int endIndex = path.indexOf("#", startIndex);
        return path.substring(startIndex + 4, endIndex);
    }
}
