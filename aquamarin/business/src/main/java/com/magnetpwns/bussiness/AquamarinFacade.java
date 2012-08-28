/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.bussiness;

import com.magnetpwns.bussiness.impl.AquamarinFacadeImpl;
import com.magnetpwns.model.*;
import com.magnetpwns.model.exception.AquamarinException;
import com.magnetpwns.model.exception.NoResultException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import org.openide.util.Lookup;

/**
 *
 * @author MagNet
 */
public abstract class AquamarinFacade {
    
    private static AquamarinFacade instance;
    
    protected AquamarinFacade() {}
    
    public static AquamarinFacade getDefault() {
        if (instance == null) {
            instance = Lookup.getDefault().lookup(AquamarinFacade.class);
            if (instance == null) {
                instance = new AquamarinFacadeImpl();
            }
        }
        return instance;
    }
    
    public abstract Collection<Client> findAllClients();
    public abstract Collection<Client> findFilteredClients(Client c);
    public abstract Client findClient(ClientId id);
    public abstract Client addClient(Client c);
    public abstract boolean deleteClient(Client c);
    public abstract boolean updateClient(Client c);
    
    public abstract Collection<City> findAllCities();
    public abstract City addCity(City c);
    public abstract boolean deleteCity(City c);
    public abstract boolean updateCity(City c);
    
    public abstract Collection<Country> findAllCountries();
    public abstract Country findCountry(CountryId id);
    public abstract Country addCountry(Country c);
    public abstract boolean deleteCountry(Country c);
    public abstract boolean updateCountry(Country c);
    
    public abstract Collection<Product> findAllProducts();
    /**
     * Find a product with price and taxrate valid at the given date.
     * @param id
     * @param date
     * @return 
     */
    public abstract Product addProduct(Product p);
    public abstract boolean deleteProduct(Product p);
    public abstract boolean updateProduct(Product p);
    public abstract void addProductPrice(Product p, BigDecimal newPrice) throws AquamarinException;
    public abstract void loadProduct(Product p) throws AquamarinException;
    
    public abstract Collection<Manufacturer> findAllManufs();
    public abstract Manufacturer addManuf(Manufacturer m);
    public abstract boolean deleteManuf(Manufacturer m);
    public abstract boolean updateManuf(Manufacturer m);
    
    public abstract Collection<TaxRate> findAllTaxRates();
    public abstract TaxRate addTaxRate(TaxRate t);
    public abstract boolean deleteTaxRate(TaxRate t);
    public abstract boolean updateTaxRate(TaxRate t);
    
    public abstract Collection<Unit> findAllUnits();
    public abstract Unit addUnit(Unit u);
    public abstract boolean deleteUnit(Unit u);
    public abstract boolean updateUnit(Unit u);
    
    public abstract Collection<Client> findStockOwners();
    public abstract Stock findStock(Client id);
    public abstract void alterItems(Stock target, Collection<CountedProduct> items, String note) throws AquamarinException;
    public abstract boolean transferItems(Stock source, Stock target, Collection<CountedProduct> items, String note, Date date);
    public abstract boolean setStockItemMinimalCount(Stock stock, StockItem item);
    
    public abstract StockTransfer addStockTransfer(StockTransfer st);
    public abstract Collection<StockTransfer> findStockTransfers();
    public abstract Collection<StockTransfer> findStockTransfersByYear(int year);
    public abstract Collection<StockTransfer> findStockTransfersByYearProduct(int year, Product p) throws NoResultException;
    public abstract Collection<Integer> findStockTransferYears();
    public abstract StockTransfer loadStockTransfer(StockTransfer st);
    
    public abstract ProformaInvoice addProforma(ProformaInvoice p);
    public abstract ProformaInvoice loadProforma(ProformaInvoice p);
    
    public abstract Invoice addInvoice(Invoice i);
    public abstract Invoice loadInvoice(Invoice i);
    public abstract void addInvoicePayment(Invoice i, Payment p) throws AquamarinException;
    public abstract void cancelInvoice(Invoice i, Date date) throws AquamarinException;
    
    public abstract Delivery addDelivery(Delivery d);
    public abstract Delivery loadDelivery(Delivery d);
    public abstract boolean alterDeliveryBlocks(Delivery d, Collection<TrackedProduct> items);
    
    public abstract Collection<Document> findAllDocuments();
    
    
}
