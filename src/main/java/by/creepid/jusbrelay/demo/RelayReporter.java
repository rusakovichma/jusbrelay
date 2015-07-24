/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.creepid.jusbrelay.demo;

import by.creepid.jusbrelay.UsbRelayDeviceInfo;
import by.creepid.jusbrelay.UsbRelayException;
import by.creepid.jusbrelay.UsbRelayStatus;

/**
 *
 * @author rusakovich
 */
public interface RelayReporter {
    
    public void showDevicesFound(int number);
    
    public void showDeviceInfo(UsbRelayDeviceInfo deviceInfo);
    
    public void showDeviceChannelStatus(UsbRelayStatus relayStatus);
    
    public void showRelayError(UsbRelayException error);
       
}
