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
public enum UsbRelayDeviceType {

    ONE_CHANNEL(1),
    TWO_CHANNEL(2),
    FOUR_CHANNEL(4),
    EIGHT_CHANNEL(8);
    private final int channels;

    private UsbRelayDeviceType(int channels) {
        this.channels = channels;
    }

    public int getChannels() {
        return channels;
    }

}
