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
public class UsbRelayDeviceHandler {

    private final int value;

    public UsbRelayDeviceHandler(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
