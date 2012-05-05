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
public class JTextFieldLimitedDocument extends PlainDocument {

    int limit;

    public JTextFieldLimitedDocument(int limit) {
        this.limit = limit;
    }
    
    @Override
    public void insertString(int i, String string, AttributeSet as) throws BadLocationException {
        if (string == null)
            return;
        
        int fullLength = getContent().length() + string.length();
        if (fullLength > limit) {
            int overLimit = fullLength - limit;
            string = string.substring(0, string.length() - overLimit);
        }
        
        super.insertString(i, string, as);
    }
    
}
