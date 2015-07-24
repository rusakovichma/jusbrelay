/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay.demo;

import by.creepid.jusbrelay.NativeUsbRelayManager;
import by.creepid.jusbrelay.UsbRelayDeviceHandler;
import by.creepid.jusbrelay.UsbRelayDeviceInfo;
import by.creepid.jusbrelay.UsbRelayException;
import by.creepid.jusbrelay.UsbRelayManager;
import by.creepid.jusbrelay.UsbRelayStatus;

/**
 *
 * @author rusakovich
 */
public class RelayDemo {

    private static final RelayReporter reporter = new StandardRelayReporter();

    public static void main(String[] args) {
        UsbRelayManager manager = NativeUsbRelayManager.getInstance();

        try {
            manager.relayInit();

            UsbRelayDeviceInfo[] devices = manager.deviceEnumerate();
            reporter.showDevicesFound(devices.length);

            for (int i = 0; i < devices.length; i++) {
                UsbRelayDeviceInfo usbRelayDeviceInfo = devices[i];
                reporter.showDeviceInfo(usbRelayDeviceInfo);

                UsbRelayDeviceHandler handler = manager.deviceOpen(usbRelayDeviceInfo.getSerialNumber());

                UsbRelayStatus[] statuses = manager.getStatus(usbRelayDeviceInfo.getSerialNumber(), handler);
                for (int j = 0; j < statuses.length; j++) {
                    UsbRelayStatus usbRelayStatus = statuses[j];
                    reporter.showDeviceChannelStatus(usbRelayStatus);
                }

                manager.closeRelay(handler);
            }

        } catch (UsbRelayException ex) {
            reporter.showRelayError(ex);
        } finally {
            try {
                manager.relayExit();
            } catch (UsbRelayException ex) {
                reporter.showRelayError(ex);
            }
        }

    }

}
