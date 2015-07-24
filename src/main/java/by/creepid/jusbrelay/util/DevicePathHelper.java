/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay.util;

/**
 *
 * @author rusakovich
 */
public class DevicePathHelper {

    private DevicePathHelper() {
    }

    public static String getVID(String path) {
        int startIndex = path.indexOf("vid_");
        int endIndex = path.indexOf("&pid");
        return path.substring(startIndex + 4, endIndex);
    }

    public static String getPID(String path) {
        int startIndex = path.indexOf("pid_");
        int endIndex = path.indexOf("#", startIndex);
        return path.substring(startIndex + 4, endIndex);
    }

}
