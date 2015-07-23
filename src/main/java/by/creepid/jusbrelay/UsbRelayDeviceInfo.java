/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay;

import java.io.Serializable;

/**
 *
 * @author rusakovich
 */
public class UsbRelayDeviceInfo implements Serializable {

    private String devicePath;
    private String serialNumber;
    private UsbRelayDeviceType deviceType;

    public UsbRelayDeviceInfo() {
    }

    public UsbRelayDeviceInfo(String devicePath, String serialNumber, UsbRelayDeviceType deviceType) {
        this.devicePath = devicePath;
        this.serialNumber = serialNumber;
        this.deviceType = deviceType;
    }

    public String getDevicePath() {
        return devicePath;
    }

    public void setDevicePath(String devicePath) {
        this.devicePath = devicePath;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public UsbRelayDeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(UsbRelayDeviceType deviceType) {
        this.deviceType = deviceType;
    }
}
