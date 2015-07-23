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
public class UsbRelayOutnumberIndexException extends RuntimeException {

    public UsbRelayOutnumberIndexException() {
        super();
    }

    public UsbRelayOutnumberIndexException(String message) {
        super(message);
    }

    public UsbRelayOutnumberIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsbRelayOutnumberIndexException(Throwable cause) {
        super(cause);
    }

    protected UsbRelayOutnumberIndexException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
