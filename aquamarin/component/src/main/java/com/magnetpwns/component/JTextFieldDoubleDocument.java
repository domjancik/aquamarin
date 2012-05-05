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
public class JTextFieldDoubleDocument extends PlainDocument {

    public JTextFieldDoubleDocument() {
    }
    
    @Override
    public void insertString(int i, String string, AttributeSet as) throws BadLocationException {
        System.out.println("adding");
        System.out.println(string);
        if (string == null)
            return;
        
        String fullString = getContent() + string;
        System.out.println(fullString);
        if (fullString.length() == 0 || fullString.matches("[0-9]+(\\.[0-9]*)?")) {
            super.insertString(i, string, as);
        }
    }
    
}
