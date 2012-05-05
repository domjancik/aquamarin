/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model.exception;

/**
 *
 * @author MagNet
 */
public class AquamarinException extends Exception {

    public AquamarinException(String message) {
        super(message);
    }

    public AquamarinException(Throwable cause) {
        super(cause);
    }

    public AquamarinException() {
    }

    public AquamarinException(String message, Throwable cause) {
        super(message, cause);
    }
}
