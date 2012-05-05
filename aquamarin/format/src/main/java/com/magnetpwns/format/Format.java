/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.format;

import java.text.DateFormat;
import java.util.Locale;

/**
 *
 * @author MagNet
 */
public class Format {
    public final static String DATE = "dd.MM.yyyy";
    public final static Locale LOCALE = Locale.GERMAN;
    public final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT, LOCALE);
}
