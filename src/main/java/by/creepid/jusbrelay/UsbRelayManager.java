/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.creepid.jusbrelay;

/**
 *
 * @author rusakovich
 */
public interface UsbRelayManager {
    
    public void relayInit() throws UsbRelayException;
    
    public void relayExit() throws UsbRelayException;
    
    public UsbRelayDeviceInfo[] deviceEnumerate();
    
    public UsbRelayDeviceHandler deviceOpen(String serialNumber) 
            throws UsbRelayException;
    
    public UsbRelayDeviceHandler deviceOpen(UsbRelayDeviceInfo deviceInfo) 
            throws UsbRelayException;
    
    public void openRelayChannel(UsbRelayDeviceHandler handler, int index) throws 
            UsbRelayException;
    
    public void openRelayChannels(UsbRelayDeviceHandler handle) 
            throws UsbRelayException;
    
    public void closeRelayChannel(UsbRelayDeviceHandler handler, int index) 
            throws UsbRelayException;
    
    public void closeRelayChannels(UsbRelayDeviceHandler handler) 
            throws UsbRelayException;
    
    public UsbRelayStatus[] getStatus(String serialNumber, UsbRelayDeviceHandler handler) 
            throws UsbRelayException;
    
    public void closeRelay(UsbRelayDeviceHandler handler)
            throws UsbRelayException;
    
}
