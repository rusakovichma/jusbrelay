/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay;

import by.creepid.jusbrelay.util.NativeHelper;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author rusakovich
 */
public class NativeUsbRelayManager implements UsbRelayManager {

    static {
        try {
            File libFile = NativeHelper.findLibFile();
            System.load(libFile.getAbsolutePath());
            NativeHelper.cleanupTempFiles();
        } catch (IOException e) {
            throw new RuntimeException("Error loading dll" + e.getMessage(), e);
        }
    }

    private NativeUsbRelayManager() {
    }

    public native void relayInit() throws UsbRelayException;

    public native void relayExit() throws UsbRelayException;

    public native UsbRelayDeviceInfo[] deviceEnumerate();

    public native UsbRelayDeviceHandler deviceOpen(String serialNumber);

    public native UsbRelayDeviceHandler deviceOpen(UsbRelayDeviceInfo deviceInfo);

    public native void openRelayChannel(UsbRelayDeviceHandler handler, int index);

    public native void openRelayChannels(UsbRelayDeviceHandler handle);

    public native void closeRelayChannel(UsbRelayDeviceHandler handler, int index);

    public native void closeRelayChannels(UsbRelayDeviceHandler handler);

    public native UsbRelayStatus[] getStatus(UsbRelayDeviceHandler handler);

    public static class InstanceHolder {

        public static final UsbRelayManager INSTANCE = new NativeUsbRelayManager();
    }

    public static UsbRelayManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

}
