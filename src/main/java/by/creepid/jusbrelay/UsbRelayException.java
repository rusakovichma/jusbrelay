/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.jusbrelay;

import java.io.IOException;

/**
 *
 * @author rusakovich
 */
public class UsbRelayException extends IOException {

    public UsbRelayException() {
        super();
    }

    public UsbRelayException(String message) {
        super(message);
    }

    public UsbRelayException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsbRelayException(Throwable cause) {
        super(cause);
    }

}
