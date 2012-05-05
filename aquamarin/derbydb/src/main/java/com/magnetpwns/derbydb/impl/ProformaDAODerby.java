/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.ProformaDAO;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MagNet
 */
public class ProformaDAODerby implements ProformaDAO {

    private static ProformaDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement addPs;
    private PreparedStatement addItemPs;
    private PreparedStatement loadPs;
    private PreparedStatement loadItemPs;
    
    private Map<Integer, Client> cachedClients;
    //private Client defaultClient = new Client(new ClientId(0),
    //        "- přídáno -", "", "", false, null, 0, null, null, null, null, null, null, null, 0, false);
    
    private Connection connection;
    
    // TODO: add transfers from invoiceitem table + returns
    
    private ProformaDAODerby(Connection connection) {
        this.connection = connection;
        cachedClients = new TreeMap<Integer, Client>();
        
        try {
            findPs = connection.prepareStatement("SELECT * FROM PROFORMA ORDER BY date_issue DESC");
            addPs = connection.prepareStatement("INSERT INTO PROFORMA VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            addItemPs = connection.prepareStatement("INSERT INTO PROFORMAITEM VALUES (?, ?, ?, ?, ?)");
            loadPs = connection.prepareStatement("SELECT * FROM PROFORMA WHERE id = ?");
            loadItemPs = connection.prepareStatement("SELECT * FROM PROFORMAITEM WHERE doc_id = ?");
        } catch (SQLException ex) {
            Logger.getLogger(ProformaDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ProformaDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new ProformaDAODerby(connection);
        }
        return instance;
    }

    @Override
    public ProformaInvoice add(ProformaInvoice pi) {
        try {
            connection.setAutoCommit(false);
            
            addPs.setInt(1, pi.getClient().getId().getId());
            addPs.setDate(2, new java.sql.Date(pi.getIssueDate().getTime()));
            addPs.setDate(3, new java.sql.Date(pi.getEndDate().getTime()));
            addPs.setString(4, pi.getPaymentType().name());
            addPs.setString(5, pi.getNote());
            addPs.setString(6, pi.getDeliveryNote());
            addPs.setInt(7, pi.getDiscountedTaxedTotalRounded().intValue());
            addPs.executeUpdate();
            
            ResultSet rs = addPs.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            pi.setId(new DocumentId(id));
            
            for (DocumentItem di : pi.getItems()) {
                addItemPs.setInt(1, pi.getId().getId());
                addItemPs.setInt(2, di.getProduct().getId().getId());
                addItemPs.setInt(3, di.getAmount());
                addItemPs.setBigDecimal(4, di.getDiscount());
                int altId = di.getProduct().getAltPriceId().getId();
                if (altId == 0) {
                    addItemPs.setNull(5, Types.INTEGER);
                } else {
                    addItemPs.setInt(5, altId);
                }
                addItemPs.executeUpdate();
            }
            
            rs.close();
            connection.commit();
            
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ProformaDAODerby.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ProformaDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(ProformaDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return pi;
    }

    private Client findClient(int id) {
        Client c = cachedClients.get(id);
        if (c == null) {
            c = ClientDAODerby.getInstance(connection).find(new ClientId(id));
            cachedClients.put(id, c);
        }
        return c;
    }
    
    private ProformaInvoice getProformaInvoiceFromResult(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int clientId = rs.getInt("client_id");
        Client client = findClient(clientId);
        Date issueDate = new Date(rs.getDate("date_issue").getTime());
        Date validDate = new Date(rs.getDate("date_valid").getTime());
        String paymentName = rs.getString("payment_type");
        PaymentType pt = PaymentType.valueOf(paymentName);
        String note = rs.getString("note");
        String deliveryNote = rs.getString("note_delivery");
        int savedTotal = rs.getInt("total");
        return new ProformaInvoice(new DocumentId(id), client, issueDate, null,
                note, deliveryNote, savedTotal, validDate, pt);
    }
    
    @Override
    public Collection<ProformaInvoice> findAll() {
        ArrayList<ProformaInvoice> items = new ArrayList<ProformaInvoice>();
        try {
            ResultSet rs = findPs.executeQuery();
            while (rs.next()) {
                items.add(getProformaInvoiceFromResult(rs));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProformaDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return items;
    }

    /**
     * Parse a proforma DocumentItem from the given result with data valid at the given date
     * @param rs
     * @param date
     * @return
     * @throws SQLException 
     */
    private DocumentItem getDIFromResult(ResultSet rs, Date date) throws SQLException, AquamarinException {
        int productId = rs.getInt("product_id");
        int amount = rs.getInt("amount");
        BigDecimal discount = rs.getBigDecimal("discount");
        int alternatePrice = rs.getInt("price_alt");
        
        DocumentItem di = new DocumentItem(
                ProductDAODerby.getInstance(connection).find(new ProductId(productId), date),
                amount,
                discount,
                null); // We don't care for the source stock on a proforma invoice, TODO: Make ProformaItem class
        
        if (alternatePrice != 0) {
            di.getProduct().setPrice(PriceDAODerby.getInstance(connection)
                    .find(new PriceId(alternatePrice)));
        }        
        
        return di;
            
    }
    
    @Override
    public ProformaInvoice load(ProformaInvoice st) {
        ArrayList<DocumentItem> items = new ArrayList<DocumentItem>();
        
        try {    
            loadItemPs.setInt(1, st.getId().getId());
            ResultSet rs  = loadItemPs.executeQuery();
            while (rs.next()) {
                items.add(getDIFromResult(rs, st.getIssueDate()));
            }
            rs.close();
            st.setItems(items);
        } catch (SQLException ex) {
            Logger.getLogger(ProformaDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AquamarinException ex) {
            Logger.getLogger(ProformaDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return st;
    }
}

