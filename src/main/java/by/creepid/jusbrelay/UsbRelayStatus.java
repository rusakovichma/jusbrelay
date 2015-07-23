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
public class UsbRelayStatus implements Serializable {

    private final int index;
    private final boolean open;

    public UsbRelayStatus(int index, boolean open) {
        this.index = index;
        this.open = open;
    }

    public int getIndex() {
        return index;
    }

    public boolean isOpen() {
        return open;
    }

}
