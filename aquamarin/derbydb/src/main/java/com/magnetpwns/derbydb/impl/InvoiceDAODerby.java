/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb.impl;

import com.magnetpwns.integration.InvoiceDAO;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.model.exception.ForbiddenActionException;
import com.magnetpwns.model.exception.OutOfBoundsException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Exceptions;

/**
 *
 * @author MagNet
 */
public class InvoiceDAODerby implements InvoiceDAO {

    private static InvoiceDAODerby instance;
    
    private PreparedStatement findPs;
    private PreparedStatement addPs;
    private PreparedStatement addItemPs;
    private PreparedStatement addSpecItemPs;
    private PreparedStatement addPaymentPs;
    private PreparedStatement updateUnpaidPs;
    private PreparedStatement updateCancelPs;
    private PreparedStatement loadPs;
    private PreparedStatement findItemPs;
    private PreparedStatement findSpecItemPs;
    private PreparedStatement findPaymentPs;
    private PreparedStatement findAlternatePrice;
    private PreparedStatement findOneItemPs;
    private PreparedStatement loadOneItemPs;
    private PreparedStatement findMaxSeqPs;
    
    private PreparedStatement updateTotalPs;
    
    private Map<Integer, Client> cachedClients;
    
    private Connection connection;
    
    private final Manufacturer emptyManuf = new Manufacturer(new ManufacturerId(0), "");
    
    
    private void updateTotal(Invoice i) {
        try {
            updateTotalPs.setInt(1, i.getDiscountedTaxedTotalRounded().intValue());
            updateTotalPs.setInt(2, i.getId().getId());
            updateTotalPs.executeUpdate();
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private InvoiceDAODerby(Connection connection) {
        this.connection = connection;
        cachedClients = new TreeMap<Integer, Client>();
        
        try {
            updateTotalPs = connection.prepareStatement("UPDATE INVOICE SET total = ? WHERE id = ?");
            
            findPs = connection.prepareStatement("SELECT * FROM INVOICE ORDER BY date_issue DESC");
            addPs = connection.prepareStatement("INSERT INTO INVOICE VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            addItemPs = connection.prepareStatement("INSERT INTO INVOICEITEM VALUES (?, ?, ?, ?, ?, ?)");
            addSpecItemPs = connection.prepareStatement("INSERT INTO INVOICEFULLITEM VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            addPaymentPs = connection.prepareStatement("INSERT INTO PAYMENT VALUES (?, ?, ?, ?)");
            loadPs = connection.prepareStatement("SELECT * FROM INVOICE WHERE id = ?");
            findItemPs = connection.prepareStatement("SELECT * FROM INVOICEITEM WHERE doc_id = ?");
            findSpecItemPs = connection.prepareStatement("SELECT * FROM INVOICEFULLITEM WHERE doc_id = ?");
            findPaymentPs = connection.prepareStatement("SELECT * FROM PAYMENT WHERE doc_id = ?");
            findOneItemPs = connection.prepareStatement("SELECT * FROM INVOICEITEM WHERE doc_id = ? AND stock_id = ? AND product_id = ?");
            updateUnpaidPs = connection.prepareStatement("UPDATE INVOICE SET unpaid = ? WHERE id = ?");
            updateCancelPs = connection.prepareStatement("UPDATE INVOICE SET date_cancelled = ? WHERE id = ?");
            findMaxSeqPs = connection.prepareStatement("SELECT seq FROM INVOICE WHERE YEAR(date_issue) = ? ORDER BY seq DESC");
            findAlternatePrice = connection.prepareStatement("SELECT price FROM ALTERNATEPRICE WHERE ID = ?");
            findMaxSeqPs.setFetchSize(1);
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static InvoiceDAODerby getInstance(Connection connection) {
        if (instance == null) {
            instance = new InvoiceDAODerby(connection);
        }
        return instance;
    }

    private void addItem(Invoice i, DocumentItem di) throws AquamarinException, SQLException {
        addItemPs.setInt(1, i.getId().getId());
        addItemPs.setInt(2, di.getSourceStock().getId().getId());
        addItemPs.setInt(3, di.getProduct().getId().getId());
        addItemPs.setInt(4, di.getAmount());
        addItemPs.setBigDecimal(5, di.getDiscount());
        int altId = di.getProduct().getAltPriceId().getId();
        if (altId == 0) {
            addItemPs.setNull(6, Types.INTEGER);
        } else {
            addItemPs.setInt(6, altId);
        }
        
        addItemPs.executeUpdate();
                
        StockDAODerby.getInstance(connection).alterItem(
                new TrackedProduct(di.getProduct(), -di.getAmount(), di.getSourceStock()));
    }

    private void addSpecItem(int docId, DocumentItem di) throws SQLException {
        addSpecItemPs.setInt(1, docId);
        addSpecItemPs.setInt(2, di.getAmount());
        addSpecItemPs.setBigDecimal(3, di.getDiscount());
        addSpecItemPs.setString(4, di.getProduct().getCode());
        addSpecItemPs.setString(5, di.getProduct().getName());
        addSpecItemPs.setBigDecimal(6, di.getProduct().getPrice());
        addSpecItemPs.setInt(7, di.getProduct().getTaxRate().getId().getId());
        addSpecItemPs.setInt(8, di.getProduct().getUnit().getId().getId());
        addSpecItemPs.executeUpdate();      
    }
    
    private DocumentItem parseSpecItem(ResultSet rs, Date invoiceDate) throws SQLException {
        return new DocumentItem(
                new Product(
                ProductId.SPECIFIC, rs.getString("product_code"), null,
                rs.getString("product_name"), null,
                rs.getBigDecimal("price"), BigDecimal.ZERO,
                emptyManuf,
                TaxRateDAODerby.getInstance(connection).find(
                    new TaxRateId(rs.getInt("tax_id")), invoiceDate
                ), UnitDAODerby.getInstance(connection).find(
                    new UnitId(rs.getInt("unit_id"))
                )), rs.getInt("amount"),
                rs.getBigDecimal("discount"), null);
    }
    
    @Override
    public Invoice add(Invoice i) {
        try {
            connection.setAutoCommit(false);
            
            ArrayList<DocumentItem> productItems = new ArrayList<DocumentItem>();
            ArrayList<DocumentItem> specItems = new ArrayList<DocumentItem>();

            for (DocumentItem documentItem : i.getItems()) {
                if (documentItem.getProduct().getId().equals(ProductId.SPECIFIC)) {
                    specItems.add(documentItem);
                } else {
                    productItems.add(documentItem);
                }
            }
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(i.getIssueDate());
            int year = cal.get(Calendar.YEAR);
            
            findMaxSeqPs.setInt(1, year);
            ResultSet rs = findMaxSeqPs.executeQuery();
            int seq;
             if (rs.next()) {
                seq = rs.getInt(1) + 1;
            } else {
                seq = 1;
            }
            rs.close();
            
            addPs.setInt(1, i.getClient().getId().getId());
            addPs.setDate(2, new java.sql.Date(i.getIssueDate().getTime()));
            addPs.setDate(3, new java.sql.Date(i.getEndDate().getTime()));
            addPs.setString(4, i.getPaymentType().name());
            addPs.setBigDecimal(5, i.checkUnpaid());
            addPs.setDate(6, new java.sql.Date(i.getCancelledDate().getTime()));
            addPs.setString(7, i.getNote());
            addPs.setString(8, i.getDeliveryNote());
            addPs.setInt(9, seq);
            addPs.setInt(10, i.getDiscountedTaxedTotalRounded().intValue());
            addPs.executeUpdate();
            
            rs = addPs.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            i.setId(new DocumentId(id));
            i.setSeqNumber(seq);
            rs.close();
            for (DocumentItem di : productItems) {
                addItem(i, di);
            }
            for (DocumentItem documentItem : specItems) {
                addSpecItem(id, documentItem);
            }
            
            connection.commit();
            
        } catch (AquamarinException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Exceptions.printStackTrace(ex1);
            }
            Exceptions.printStackTrace(ex);
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return i;
    }

    private Client findClient(int id) {
        Client c = cachedClients.get(id);
        if (c == null) {
            c = ClientDAODerby.getInstance(connection).find(new ClientId(id));
            cachedClients.put(id, c);
        }
        return c;
    }
    
    private Invoice getInvoiceFromResult(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int clientId = rs.getInt("client_id");
        Client client = findClient(clientId);
        Date issueDate = new Date(rs.getDate("date_issue").getTime());
        Date endDate = new Date(rs.getDate("date_due").getTime());
        Date cancelDate = new Date(rs.getDate("date_cancelled").getTime());
        PaymentType paymentType = PaymentType.valueOf(rs.getString("payment_type"));
        BigDecimal unpaid = rs.getBigDecimal("unpaid");
        String note = rs.getString("note");
        String deliveryNote = rs.getString("note_delivery");
        int seq = rs.getInt("seq");
        int savedTotal = rs.getInt("total");
        return new Invoice(
                new DocumentId(id), client, issueDate, null, note, deliveryNote,
                savedTotal, endDate, paymentType, null, unpaid, cancelDate, seq);
    }
    
    public void updateTotals() {
        try {
            ResultSet rs = findPs.executeQuery();
            while (rs.next()) {
                Invoice i = getInvoiceFromResult(rs);
                Invoice iLoaded = load(i);
                updateTotal(iLoaded);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public Collection<Invoice> findAll() {
        ArrayList<Invoice> items = new ArrayList<Invoice>();
        try {
            ResultSet rs = findPs.executeQuery();
            while (rs.next()) {
                items.add(getInvoiceFromResult(rs));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return items;
    }

    /**
     * Parse a DocumentItem from the given result with data valid at the given date
     * @param rs
     * @param date
     * @return
     * @throws SQLException 
     */
    private DocumentItem getDIFromResult(ResultSet rs, Date date) throws SQLException, AquamarinException {
        int productId = rs.getInt("product_id");
        int stockId = rs.getInt("stock_id");
        int amount = rs.getInt("amount");
        BigDecimal discount = rs.getBigDecimal("discount");
        int alternatePrice = rs.getInt("price_alt");
        
        DocumentItem di = new DocumentItem(
                ProductDAODerby.getInstance(connection).find(new ProductId(productId), date),
                amount,
                discount,
                ClientDAODerby.getInstance(connection).find(new ClientId(stockId)));
        
        if (alternatePrice != 0) {
            di.getProduct().setPrice(PriceDAODerby.getInstance(connection)
                    .find(new PriceId(alternatePrice)));
        }        
        
        return di;
    }
    
    private Payment getPFromResult(ResultSet rs) throws SQLException {
        Date payDate = rs.getDate("payment_date");
        String log = rs.getString("log");
        BigDecimal amount = rs.getBigDecimal("amount");
        return new Payment(payDate, log, amount);
    }
    
    @Override
    public Invoice load(Invoice i) {
        ArrayList<DocumentItem> items = new ArrayList<DocumentItem>();
        ArrayList<Payment> payments = new ArrayList<Payment>();
        
        try {
            findItemPs.setInt(1, i.getId().getId());
            ResultSet rs  = findItemPs.executeQuery();
            while (rs.next()) {
                items.add(getDIFromResult(rs, i.getIssueDate()));
            }
            rs.close();
            
            findSpecItemPs.setInt(1, i.getId().getId());
            rs  = findSpecItemPs.executeQuery();
            while (rs.next()) {
                items.add(parseSpecItem(rs, i.getIssueDate()));
            }
            rs.close();
            
            findPaymentPs.setInt(1, i.getId().getId());
            rs  = findPaymentPs.executeQuery();
            while (rs.next()) {
                payments.add(getPFromResult(rs));
            }
            rs.close();
            
            i.setItems(items);
            i.setPayments(payments);
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AquamarinException ex) {
            Logger.getLogger(InvoiceDAODerby.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return i;
    }

    @Override
    public void cancel(Invoice i, Date date) throws AquamarinException {
        if (i.isCancelled())
            throw new ForbiddenActionException();
        
        try {    
            connection.setAutoCommit(false);
            
            updateCancelPs.setDate(1, new java.sql.Date(date.getTime()));
            updateCancelPs.setInt(2, i.getId().getId());
            updateCancelPs.executeUpdate();
            
            // Return all items
            for (DocumentItem documentItem : i.getItems()) {
                if (documentItem.getProduct().getId() != ProductId.SPECIFIC)
                    StockDAODerby.getInstance(connection).alterItem(documentItem);
            }
            
            // Return all payments
            for (Payment payment : i.getPayments()) {
                ClientDAODerby.getInstance(connection).alterPaid(i.getClient(), payment.getAmount().negate());
            }
            
            connection.commit();
            
            i.setCancelledDate(date);
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Exceptions.printStackTrace(ex1);
                throw new AquamarinException(ex1);
            }
            Exceptions.printStackTrace(ex);
            throw new AquamarinException(ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
                throw new AquamarinException(ex);
            }
        }
    }

    @Override
    public void pay(Invoice i, Payment p) throws AquamarinException {
        if (i.isCancelled())
            throw new ForbiddenActionException();
        if (p.getAmount().compareTo(i.checkUnpaid()) > 0)
            throw new OutOfBoundsException();
        
        try {
            connection.setAutoCommit(false);
            
            addPaymentPs.setInt(1, i.getId().getId());
            addPaymentPs.setDate(2, new java.sql.Date(p.getDate().getTime()));
            addPaymentPs.setString(3, p.getLog());
            addPaymentPs.setBigDecimal(4, p.getAmount());
            addPaymentPs.executeUpdate();
            
            i.pay(p);
            
            updateUnpaidPs.setBigDecimal(1, i.getUnpaid());
            updateUnpaidPs.setInt(2, i.getId().getId());
            updateUnpaidPs.executeUpdate();
            
            ClientDAODerby.getInstance(connection).alterPaid(i.getClient(), p.getAmount());
            
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Exceptions.printStackTrace(ex1);
                throw new AquamarinException(ex1);
            }
            Exceptions.printStackTrace(ex);
            throw new AquamarinException(ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
                throw new AquamarinException(ex);
            }
        }
        
    }
}

