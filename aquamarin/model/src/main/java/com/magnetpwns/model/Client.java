/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author MagNet
 */
public class Client implements Identifiable {
    
    public static final Client DUMMY = new Client(new ClientId(0),
            "", "", "", false, "", 0, "", "", "", "", "", "", City.DUMMY, BigDecimal.ZERO, false);
    
    private ClientId id;
    private String title;
    private String titleShort;
    private String name;
    private boolean vip;
    private String street;
    private int ico;
    private String dic;
    private String telephoneWork;
    private String telephoneHome;
    private String telephoneCell;
    private String email;
    private String note;
    private City city;
    private BigDecimal tempSum;
    private boolean stockEnabled;

    public Client(ClientId id, String title, String titleShort, String name, boolean vip, String street, int ico, String dic, String telephoneWork, String telephoneHome, String telephoneCell, String email, String note, City city, BigDecimal tempSum, boolean stockEnabled) {
        this.id = id;
        this.title = title;
        this.titleShort = titleShort;
        this.name = name;
        this.vip = vip;
        this.street = street;
        this.ico = ico;
        this.dic = dic;
        this.telephoneWork = telephoneWork;
        this.telephoneHome = telephoneHome;
        this.telephoneCell = telephoneCell;
        this.email = email;
        this.note = note;
        this.city = city;
        this.tempSum = tempSum.setScale(2, RoundingMode.HALF_UP);
        this.stockEnabled = stockEnabled;
    }

    public BigDecimal getTempSum() {
        return tempSum;
    }
    
    public City getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public ClientId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isVip() {
        return vip;
    }

    @Override
    public String toString() {
        String s = title;
        if (name != null && name.length() > 0) {
            s += " (" + name + ')';
        }
        return s;
    }

    public String getDic() {
        return dic;
    }

    public String getEmail() {
        return email;
    }

    public int getIco() {
        return ico;
    }

    public String getNote() {
        return note;
    }

    public String getTelephoneCell() {
        return telephoneCell;
    }

    public String getTelephoneHome() {
        return telephoneHome;
    }

    public String getTelephoneWork() {
        return telephoneWork;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleShort() {
        return titleShort;
    }

    public boolean isStockEnabled() {
        return stockEnabled;
    }
    
    @Override
    public void setId(AbstractId id) {
        this.id = (ClientId) id;
    }
    
    
    
}
