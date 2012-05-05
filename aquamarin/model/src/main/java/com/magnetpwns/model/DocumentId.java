/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

/**
 *
 * @author MagNet
 */
public class DocumentId extends AbstractId {

    public final static DocumentId ZERO = new DocumentId(0);
    
    public DocumentId(int id) {
        super(id);
    }
    
}
