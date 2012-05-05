/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author MagNet
 */
public class JTextFieldNumberDocument extends PlainDocument {

    @Override
    public void insertString(int i, String string, AttributeSet as) throws BadLocationException {
        if (string == null)
            return;
        
        string = string.replaceAll("[^0-9]*", "");
        super.insertString(i, string, as);
    }
    
}
