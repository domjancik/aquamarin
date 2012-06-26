/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.NbPreferences;

/**
 *
 * @author MagNet
 */
public class DerbyConnection {
    public static final String DBNAME = "aquamarindb";
    public final static String DEFAULT = System.getProperty("user.home")
                    + "/aquamarin";
    private Connection connection;
    
    private static DerbyConnection instance;
    private static String paymentTypeEnum = "payment_type VARCHAR(20) CONSTRAINT payment_type_ck CHECK (payment_type IN ('TRANSFER', 'CASH', 'COD')),";
    private static String paymentTypeEnum2 = "payment_type VARCHAR(20) CONSTRAINT payment_type_ck2 CHECK (payment_type IN ('TRANSFER', 'CASH', 'COD')),";
    private static String transferTypeEnum = "transfer_type VARCHAR(20) CONSTRAINT transfer_type_ck CHECK (transfer_type IN ('ADDREMOVE', 'TRANSFER', 'SOLD', 'RETURNED')),";

    private DerbyConnection() {
        try {            
            String location = NbPreferences.forModule(DerbyConnection.class).get("dblocation", DerbyConnection.DEFAULT);
            String dbURL = "jdbc:derby:" + location + "/" + DBNAME + ";create=true";
            connection = DriverManager.getConnection(dbURL);
            DatabaseMetaData m = connection.getMetaData();
            
            ResultSet rs = m.getTables(null, null, "COUNTRY", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table COUNTRY generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE COUNTRY ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "country_name VARCHAR(50) NOT NULL)"
                );
            }
            
            rs = m.getTables(null, null, "CITY", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table CITY generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE CITY ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "cityname VARCHAR(50) NOT NULL,"
                    + "postcode INT NOT NULL,"
                    + "country_id INT NOT NULL,"
                    + "CONSTRAINT CITY_COUNTRY_FK FOREIGN KEY (country_id)"
                    + "REFERENCES COUNTRY (ID))"
                );
            }
            
            //Statement st = connection.createStatement();
            //st.executeUpdate("DROP TABLE STOCK");
            
            rs = m.getTables(null, null, "CLIENT", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table CLIENT generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE CLIENT ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED BY DEFAULT AS IDENTITY,"
                    + "client_title VARCHAR(150) NOT NULL,"
                    + "client_title_short VARCHAR(50) NOT NULL,"
                    + "client_name VARCHAR(50),"
                    + "vip SMALLINT,"
                    + "street VARCHAR(50) NOT NULL,"
                    + "ico INT,"
                    + "dic VARCHAR(20),"
                    + "telephone_work VARCHAR(20),"
                    + "telephone_home VARCHAR(20),"
                    + "telephone_cell VARCHAR(20),"
                    + "email VARCHAR(50),"
                    + "note VARCHAR(200),"
                    + "tempsum DOUBLE,"
                    + "city_id INT NOT NULL,"
                    + "stock_enabled SMALLINT DEFAULT NULL,"
                    + "CONSTRAINT CLIENT_CITY_FK FOREIGN KEY (city_id)"
                    + "REFERENCES CITY (ID))"
                );
            }
            
            rs = m.getTables(null, null, "MANUF", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table MANUF generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE MANUF ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "manuf_name VARCHAR(50) NOT NULL)"
                );
            }
            
            rs = m.getTables(null, null, "TAXRATE", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table TAXRATE generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE TAXRATE ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "taxrate_name VARCHAR(50) NOT NULL,"
                    + "taxrate DECIMAL(5, 2))"
                );
            }
            
            rs = m.getTables(null, null, "TAXRATEHISTORY", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table TAXRATEHISTORY generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE TAXRATEHISTORY ("
                    + "taxrate_id INT NOT NULL,"
                    + "taxrate DECIMAL(5, 2),"
                    + "valid DATE,"
                    + "CONSTRAINT TXH_TAXRATE_FK FOREIGN KEY (taxrate_id)"
                    + "REFERENCES TAXRATE (ID))"
                );
            }
            
            
            
            rs = m.getTables(null, null, "UNIT", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table UNIT generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE UNIT ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "unit_name VARCHAR(50) NOT NULL)"
                );
            }
            
            rs = m.getTables(null, null, "PRODUCT", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table PRODUCT generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE PRODUCT ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "product_code VARCHAR(50) UNIQUE NOT NULL,"
                    + "product_code_manuf VARCHAR(50),"
                    + "product_name VARCHAR(50),"
                    + "description VARCHAR(100),"
                    + "price DECIMAL(11,2),"
                    + "price_bought DECIMAL(11,2),"
                    + "manuf_id INT NOT NULL,"
                    + "tax_id INT NOT NULL,"
                    + "unit_id INT NOT NULL,"
                    + "CONSTRAINT PRODUCT_MANUF_FK FOREIGN KEY (manuf_id)"
                    + "REFERENCES MANUF (ID),"
                    + "CONSTRAINT PRODUCT_TAX_FK FOREIGN KEY (tax_id)"
                    + "REFERENCES TAXRATE (ID),"
                    + "CONSTRAINT PRODUCT_UNIT_FK FOREIGN KEY (unit_id)"
                    + "REFERENCES UNIT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "PRICEHISTORY", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table PRICEHISTORY generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE PRICEHISTORY ("
                    + "product_id INT NOT NULL,"
                    + "price DECIMAL(11,2),"
                    + "valid DATE,"
                    + "CONSTRAINT PH_PRODUCT_FK FOREIGN KEY (product_id)"
                    + "REFERENCES PRODUCT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "ALTERNATEPRICE", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table ALTERNATEPRICE generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE ALTERNATEPRICE ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "product_id INT NOT NULL,"
                    + "price DECIMAL(11,2),"
                    + "CONSTRAINT AP_PRODUCT_FK FOREIGN KEY (product_id)"
                    + "REFERENCES PRODUCT (ID))"
                );
            }
            
            
            rs = m.getTables(null, null, "STOCK", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table STOCK generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE STOCK ("
                    + "client_id INT NOT NULL,"
                    + "product_id INT NOT NULL,"
                    + "amount INT,"
                    + "amount_min INT DEFAULT 0,"
                    + "amount_blocked INT DEFAULT 0,"
                    + "CONSTRAINT STOCK_CLIENT_FK FOREIGN KEY (client_id)"
                    + "REFERENCES CLIENT (ID),"
                    + "CONSTRAINT STOCK_PRODUCT_FK FOREIGN KEY (product_id)"
                    + "REFERENCES PRODUCT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "STOCKTRANSFER", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table STOCKTRANSFER generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE STOCKTRANSFER ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "src_id INT,"
                    + "dst_id INT NOT NULL,"
                    + "transfer_date DATE,"
                    + transferTypeEnum
                    + "note VARCHAR(200),"
                    + "CONSTRAINT STOCKTRANSFER_CLIENT_SRC_FK FOREIGN KEY (src_id)"
                    + "REFERENCES CLIENT (ID),"
                    + "CONSTRAINT STOCKTRANSFER_CLIENT_DST_FK FOREIGN KEY (dst_id)"
                    + "REFERENCES CLIENT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "STOCKTRANSFERITEM", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table STOCKTRANSFERITEM generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE STOCKTRANSFERITEM ("
                    + "transfer_id INT NOT NULL,"
                    + "product_id INT NOT NULL,"
                    + "amount int NOT NULL,"
                    + "CONSTRAINT STOCKTRANSFERITEM_STOCKTRANSFER_FK FOREIGN KEY (transfer_id)"
                    + "REFERENCES STOCKTRANSFER (ID),"
                    + "CONSTRAINT STOCKTRANSFERITEM_PRODUCT_FK FOREIGN KEY (product_id)"
                    + "REFERENCES PRODUCT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "PROFORMA", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table PROFORMA generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE PROFORMA ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "client_id INT NOT NULL,"
                    + "date_issue DATE,"
                    + "date_valid DATE,"
                    + paymentTypeEnum
                    + "note VARCHAR(200),"
                    + "note_delivery VARCHAR(200),"
                    + "total INT,"
                    + "CONSTRAINT PROFORMA_CLIENT_FK FOREIGN KEY (client_id)"
                    + "REFERENCES CLIENT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "PROFORMAITEM", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table PROFORMAITEM generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE PROFORMAITEM ("
                    + "doc_id INT NOT NULL,"
                    + "product_id INT NOT NULL,"
                    + "amount int NOT NULL,"
                    + "discount DECIMAL(5,2),"
                    + "price_alt INT,"
                    + "CONSTRAINT PROFORMAITEM_PROFORMA_FK FOREIGN KEY (doc_id)"
                    + "REFERENCES PROFORMA (ID),"
                    + "CONSTRAINT PROFORMAITEM_APRICE_FK FOREIGN KEY (price_alt)"
                    + "REFERENCES ALTERNATEPRICE (ID),"
                    + "CONSTRAINT PROFORMAITEM_PRODUCT_FK FOREIGN KEY (product_id)"
                    + "REFERENCES PRODUCT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "INVOICE", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table INVOICE generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE INVOICE ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "client_id INT NOT NULL,"
                    + "date_issue DATE,"
                    + "date_due DATE,"
                    + paymentTypeEnum2
                    + "unpaid DECIMAL(11,2),"
                    + "date_cancelled DATE,"
                    + "note VARCHAR(200),"
                    + "note_delivery VARCHAR(200),"
                    + "seq INT,"
                    + "total INT,"
                    + "CONSTRAINT INVOICE_CLIENT_FK FOREIGN KEY (client_id)"
                    + "REFERENCES CLIENT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "PAYMENT", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table PAYMENT generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE PAYMENT ("
                    + "doc_id INT NOT NULL,"
                    + "payment_date DATE,"
                    + "log VARCHAR(50),"
                    + "amount DECIMAL(11,2) NOT NULL,"
                    + "CONSTRAINT PAYMENT_INVOICE_FK FOREIGN KEY (doc_id)"
                    + "REFERENCES INVOICE (ID))"
                );
            }
            
            rs = m.getTables(null, null, "INVOICEITEM", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table INVOICEITEM generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE INVOICEITEM ("
                    + "doc_id INT NOT NULL,"
                    + "stock_id INT NOT NULL,"
                    + "product_id INT NOT NULL,"
                    + "amount INT NOT NULL,"
                    + "discount DECIMAL(5,2),"
                    + "price_alt INT,"
                    + "CONSTRAINT INVOICEITEM_APRICE_FK FOREIGN KEY (price_alt)"
                    + "REFERENCES ALTERNATEPRICE (ID),"
                    + "CONSTRAINT INVOICEITEM_CLIENT_FK FOREIGN KEY (stock_id)"
                    + "REFERENCES CLIENT (ID),"
                    + "CONSTRAINT INVOICEITEM_INVOICE_FK FOREIGN KEY (doc_id)"
                    + "REFERENCES INVOICE (ID),"
                    + "CONSTRAINT INVOICEITEM_PRODUCT_FK FOREIGN KEY (product_id)"
                    + "REFERENCES PRODUCT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "INVOICEFULLITEM", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table INVOICEFULLITEM generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE INVOICEFULLITEM ("
                    + "doc_id INT NOT NULL,"
                    + "amount INT NOT NULL,"
                    + "discount DECIMAL(5,2),"
                    + "product_code VARCHAR(50),"
                    + "product_name VARCHAR(50),"
                    + "price DECIMAL(11,2),"
                    + "tax_id INT NOT NULL,"
                    + "unit_id INT NOT NULL,"
                    + "CONSTRAINT IFULL_TAX_FK FOREIGN KEY (tax_id)"
                    + "REFERENCES TAXRATE (ID),"
                    + "CONSTRAINT IFULL_UNIT_FK FOREIGN KEY (unit_id)"
                    + "REFERENCES UNIT (ID),"
                    + "CONSTRAINT IFULL_INVOICE_FK FOREIGN KEY (doc_id)"
                    + "REFERENCES INVOICE (ID))"
                );
            }
            
            rs = m.getTables(null, null, "DELIVERY", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table DELIVERY generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE DELIVERY ("
                    + "ID INT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,"
                    + "client_id INT NOT NULL,"
                    + "date_issue DATE,"
                    + "note VARCHAR(200),"
                    + "note_delivery VARCHAR(200),"
                    + "blocking INT,"
                    + "total INT,"
                    + "CONSTRAINT DELIVERY_CLIENT_FK FOREIGN KEY (client_id)"
                    + "REFERENCES CLIENT (ID))"
                );
            }
            
            rs = m.getTables(null, null, "DELIVERYITEM", null);
            if (!rs.next()) {
                Logger.getLogger(getClass().getName()).
                        log(Level.INFO, "Table DELIVERYITEM generated");
                Statement s = connection.createStatement();
                s.executeUpdate("CREATE TABLE DELIVERYITEM ("
                    + "doc_id INT NOT NULL,"
                    + "stock_id INT NOT NULL,"
                    + "product_id INT NOT NULL,"
                    + "amount INT NOT NULL,"
                    + "amount_blocked INT,"
                    + "CONSTRAINT DELIVERYITEM_CLIENT_FK FOREIGN KEY (stock_id)"
                    + "REFERENCES CLIENT (ID),"
                    + "CONSTRAINT DELIVERYITEM_DELIVERY_FK FOREIGN KEY (doc_id)"
                    + "REFERENCES DELIVERY (ID),"
                    + "CONSTRAINT DELIVERYITEM_PRODUCT_FK FOREIGN KEY (product_id)"
                    + "REFERENCES PRODUCT (ID))"
                );
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }
    
    public void executeQuery(String query) { // TODO remove after dev finish
        try {
            Statement st = connection.createStatement();
            st.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(DerbyConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static DerbyConnection getInstance() {
        if (instance == null) {
            instance = new DerbyConnection();
        }
        return instance;
    }
}
