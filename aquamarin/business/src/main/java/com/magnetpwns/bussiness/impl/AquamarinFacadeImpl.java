/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.bussiness.impl;

import com.magnetpwns.bussiness.AquamarinFacade;
import com.magnetpwns.integration.DAOFactory;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.model.exception.NoResultException;
import com.magnetpwns.model.exception.OutOfBoundsException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MagNet
 */
public class AquamarinFacadeImpl extends AquamarinFacade {

    @Override
    public Collection<Client> findAllClients() {
        return DAOFactory.getDefault().createClientDAO().findAll();
    }

    @Override
    public Collection<Client> findFilteredClients(Client c) {
        return DAOFactory.getDefault().createClientDAO().findFiltered(c);
    }

    @Override
    public Client findClient(ClientId id) {
        return DAOFactory.getDefault().createClientDAO().find(id);
    }

    @Override
    public Client addClient(Client c) {
        return DAOFactory.getDefault().createClientDAO().add(c);
    }

    @Override
    public boolean deleteClient(Client c) {
        return DAOFactory.getDefault().createClientDAO().delete(c);
    }

    @Override
    public boolean updateClient(Client c) {
        return DAOFactory.getDefault().createClientDAO().update(c);
    }

    @Override
    public Collection<City> findAllCities() {
        return DAOFactory.getDefault().createCityDAO().findAll();
    }

    @Override
    public City addCity(City c) {
        return DAOFactory.getDefault().createCityDAO().add(c);
    }

    @Override
    public boolean deleteCity(City c) {
        return DAOFactory.getDefault().createCityDAO().delete(c);
    }

    @Override
    public boolean updateCity(City c) {
        return DAOFactory.getDefault().createCityDAO().update(c);
    }

    @Override
    public Collection<Product> findAllProducts() {
        return DAOFactory.getDefault().createProductDAO().findAll();
    }

    @Override
    public Product addProduct(Product p) {
        return DAOFactory.getDefault().createProductDAO().add(p);
    }

    @Override
    public boolean deleteProduct(Product p) {
        return DAOFactory.getDefault().createProductDAO().delete(p);
    }

    @Override
    public boolean updateProduct(Product p) {
        return DAOFactory.getDefault().createProductDAO().update(p);
    }

    @Override
    public Collection<Manufacturer> findAllManufs() {
        return DAOFactory.getDefault().createManufacturerDAO().findAll();
    }

    @Override
    public Manufacturer addManuf(Manufacturer m) {
        return DAOFactory.getDefault().createManufacturerDAO().add(m);
    }

    @Override
    public boolean deleteManuf(Manufacturer m) {
        return DAOFactory.getDefault().createManufacturerDAO().delete(m);
    }

    @Override
    public boolean updateManuf(Manufacturer m) {
        return DAOFactory.getDefault().createManufacturerDAO().update(m);
    }

    @Override
    public Collection<TaxRate> findAllTaxRates() {
        return DAOFactory.getDefault().createTaxRateDAO().findAll();
    }

    @Override
    public Collection<TaxRate> findAllTaxRates(Date date) {
        Collection<TaxRate> taxRatesNoDate = AquamarinFacade.getDefault().findAllTaxRates();
        
        Collection<TaxRate> taxRates = new LinkedList<TaxRate>();
        
        for (TaxRate taxRate : taxRatesNoDate) {
            taxRates.add(
                    DAOFactory.getDefault().createTaxRateDAO().find(
                        taxRate.getId(), date)
                    );
        }
        
        return taxRates;
    }

    @Override
    public TaxRate findTaxRate(TaxRateId id, Date date) {
        return DAOFactory.getDefault().createTaxRateDAO().find(id, date);
    }

    @Override
    public TaxRate addTaxRate(TaxRate t) {
        return DAOFactory.getDefault().createTaxRateDAO().add(t);
    }

    @Override
    public boolean deleteTaxRate(TaxRate t) {
        return DAOFactory.getDefault().createTaxRateDAO().delete(t);
    }

    @Override
    public boolean updateTaxRate(TaxRate t) {
        return DAOFactory.getDefault().createTaxRateDAO().update(t);
    }

    @Override
    public Collection<Unit> findAllUnits() {
        return DAOFactory.getDefault().createUnitDAO().findAll();
    }

    @Override
    public Unit addUnit(Unit u) {
        return DAOFactory.getDefault().createUnitDAO().add(u);
    }

    @Override
    public boolean deleteUnit(Unit u) {
        return DAOFactory.getDefault().createUnitDAO().delete(u);
    }

    @Override
    public boolean updateUnit(Unit u) {
        return DAOFactory.getDefault().createUnitDAO().update(u);
    }

    @Override
    public Collection<Client> findStockOwners() {
        return DAOFactory.getDefault().createClientDAO().findAllStockOwners();
    }

    @Override
    public Stock findStock(Client id) {
        return DAOFactory.getDefault().createStockDAO().find(id);
    }

    @Override
    public void alterItems(Stock target, Collection<CountedProduct> items, String note) throws AquamarinException {
        boolean ret = DAOFactory.getDefault().createStockDAO().alterItems(target, items);
        if (ret) {
            StockTransfer st = new StockTransfer(null, null, target.getOwner(), new Date(), items, StockTransferType.ADDREMOVE, note);
            DAOFactory.getDefault().createStockTransferDAO().add(st);
        }
    }

    @Override
    public boolean transferItems(Stock source, Stock target, Collection<CountedProduct> items, String note, Date date) {
        boolean ret = DAOFactory.getDefault().createStockDAO().transferItems(source, target, items);
        if (ret) {
            StockTransfer st = new StockTransfer(null, source.getOwner(), target.getOwner(), date, items, StockTransferType.TRANSFER, note);
            DAOFactory.getDefault().createStockTransferDAO().add(st);
        }
        return ret;
    }

    @Override
    public boolean setStockItemMinimalCount(Stock stock, StockItem item) {
        return DAOFactory.getDefault().createStockDAO().setMinimalAmount(stock, item);
    }

    @Override
    public StockTransfer addStockTransfer(StockTransfer st) {
        return DAOFactory.getDefault().createStockTransferDAO().add(st);
    }

    @Override
    public Collection<StockTransfer> findStockTransfers() {
        return DAOFactory.getDefault().createStockTransferDAO().findAll();
    }

    @Override
    public Collection<StockTransfer> findStockTransfersByYear(int year) {
        return DAOFactory.getDefault().createStockTransferDAO().findByYear(year);
    }

    @Override
    public StockTransfer loadStockTransfer(StockTransfer st) {
        return DAOFactory.getDefault().createStockTransferDAO().load(st);
    }

    @Override
    public Collection<Integer> findStockTransferYears() {
        return DAOFactory.getDefault().createStockTransferDAO().findYears();
    }

    @Override
    public Collection<Country> findAllCountries() {
        return DAOFactory.getDefault().createCountryDAO().findAll();
    }

    @Override
    public Country addCountry(Country c) {
        return DAOFactory.getDefault().createCountryDAO().add(c);
    }

    @Override
    public boolean deleteCountry(Country c) {
        return DAOFactory.getDefault().createCountryDAO().delete(c);
    }

    @Override
    public boolean updateCountry(Country c) {
        return DAOFactory.getDefault().createCountryDAO().update(c);
    }

    @Override
    public Country findCountry(CountryId id) {
        return DAOFactory.getDefault().createCountryDAO().find(id);
    }

    @Override
    public ProformaInvoice addProforma(ProformaInvoice p) {
        return DAOFactory.getDefault().createProformaDAO().add(p);
    }

    @Override
    public ProformaInvoice loadProforma(ProformaInvoice p) {
        return DAOFactory.getDefault().createProformaDAO().load(p);
    }

    @Override
    public Delivery addDelivery(Delivery d) {
        return DAOFactory.getDefault().createDeliveryDAO().add(d);
    }

    @Override
    public Delivery loadDelivery(Delivery d) {
        return DAOFactory.getDefault().createDeliveryDAO().load(d);
    }

    @Override
    public boolean alterDeliveryBlocks(Delivery d, Collection<TrackedProduct> items) {
        try {
            return DAOFactory.getDefault().createDeliveryDAO().alterBlocks(d, items);
        } catch (OutOfBoundsException ex) {
            Logger.getLogger(AquamarinFacadeImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Collection<Document> findAllDocuments() {
        Collection<ProformaInvoice> pis = DAOFactory.getDefault().createProformaDAO().findAll();
        Collection<Delivery> ds = DAOFactory.getDefault().createDeliveryDAO().findAll();
        Collection<Invoice> is = DAOFactory.getDefault().createInvoiceDAO().findAll();
        
        Collection<Document> allDocs = getOrderedDocs(pis, ds);
        allDocs = getOrderedDocs(allDocs, is);
        
        return allDocs;
    }
    
    private Collection<Document> getOrderedDocs(Collection<? extends Document> col1, Collection<? extends Document> col2) {
        Collection<Document> orderedDocs = new ArrayList<Document>();
        
        Iterator<? extends Document> it1 = col1.iterator();
        Iterator<? extends Document> it2 = col2.iterator();
        
        Document doc1 = null;
        Document doc2 = null;
                
        if (it1.hasNext())
            doc1 = it1.next();
        if (it2.hasNext())
            doc2 = it2.next();
        
        do {
            if (doc1 == null) {
                orderedDocs.add(doc2);
                try {
                    doc2 = it2.next();
                } catch (NoSuchElementException e) {
                    doc2 = null;
                }
                continue;
            }
            if (doc2 == null) {
                orderedDocs.add(doc1);
                try {
                    doc1 = it1.next();
                } catch (NoSuchElementException e) {
                    doc1 = null;
                }
                continue;
            }
            
            if (doc1.getIssueDate().after(doc2.getIssueDate())) {
                orderedDocs.add(doc1);
                if (it1.hasNext()) {
                    doc1 = it1.next();
                } else {
                    doc1 = null;
                }
                continue;
            }
            orderedDocs.add(doc2);
            if (it2.hasNext()) {
                doc2 = it2.next();
            } else {
                doc2 = null;
            }
        } while (doc1 != null || doc2 != null);
        
        return orderedDocs;
    }

    @Override
    public Invoice addInvoice(Invoice i) {
        return DAOFactory.getDefault().createInvoiceDAO().add(i);
    }

    @Override
    public Invoice loadInvoice(Invoice i) {
        return DAOFactory.getDefault().createInvoiceDAO().load(i);
    }

    @Override
    public void addInvoicePayment(Invoice i, Payment p) throws AquamarinException {
        DAOFactory.getDefault().createInvoiceDAO().pay(i, p);
    }

    @Override
    public void cancelInvoice(Invoice i, Date date) throws AquamarinException {
        DAOFactory.getDefault().createInvoiceDAO().cancel(i, date);
    }

    @Override
    public void addProductPrice(Product p, BigDecimal newPrice) throws AquamarinException {
        DAOFactory.getDefault().createProductDAO().addPrice(p, newPrice);
    }

    @Override
    public void loadProduct(Product p) throws AquamarinException {
        DAOFactory.getDefault().createProductDAO().load(p);
    }

    @Override
    public Collection<StockTransfer> findStockTransfersByYearProduct(int year, Product p) throws NoResultException {
        return DAOFactory.getDefault().createStockTransferDAO().findByYearProduct(year, p);
    }
    
}
