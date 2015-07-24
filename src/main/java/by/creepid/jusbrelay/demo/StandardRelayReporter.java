/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay.demo;

import by.creepid.jusbrelay.UsbRelayDeviceInfo;
import by.creepid.jusbrelay.UsbRelayException;
import by.creepid.jusbrelay.UsbRelayStatus;
import java.io.PrintStream;

/**
 *
 * @author rusakovich
 */
public class StandardRelayReporter implements RelayReporter {

    private PrintStream out = System.out;

    public void showDevicesFound(int number) {
        out.println("----------------------Devices found:" + number + "----------------------");
    }

    public void showDeviceInfo(UsbRelayDeviceInfo deviceInfo) {
        out.println("*************************************************");
        out.println("Device path: " + deviceInfo.getDevicePath());
        out.println("Device serial number: " + deviceInfo.getSerialNumber());
        out.println("Device type: " + deviceInfo.getDeviceType());
        out.println("*************************************************");
    }

    public void showDeviceChannelStatus(UsbRelayStatus relayStatus) {
        out.println("*************************************************");
        System.out.println("Channel index: " + relayStatus.getIndex());
        System.out.println("Channel status: " + (relayStatus.isOpen() ? "opened" : "closed"));
        out.println("*************************************************");
    }

    public void showRelayError(UsbRelayException error) {
        out.println("!!!!!!!!!!!!!!!!!ERROR OCCURED!!!!!!!!!!!!!!!!!!!!");
        error.printStackTrace();
        out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

}
