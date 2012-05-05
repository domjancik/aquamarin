/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 * All possible payment types for an invoice
 * @author MagNet
 */
public enum PaymentType {
    TRANSFER("Převodním příkazem"),
    CASH("Hotově"),
    COD("Na dobírku"); // Cash on delivery
    
    private String name;

    private PaymentType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
