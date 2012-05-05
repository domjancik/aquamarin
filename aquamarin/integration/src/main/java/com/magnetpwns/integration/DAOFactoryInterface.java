/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.integration;

/**
 *
 * @author MagNet
 */
public interface DAOFactoryInterface {
    public ClientDAO createClientDAO();
    public CityDAO createCityDAO();
    public CountryDAO createCountryDAO();
    public ManufacturerDAO createManufacturerDAO();
    public ProductDAO createProductDAO();
    public TaxRateDAO createTaxRateDAO();
    public UnitDAO createUnitDAO();
    public StockDAO createStockDAO();
    public StockTransferDAO createStockTransferDAO();
    public ProformaDAO createProformaDAO();
    public DeliveryDAO createDeliveryDAO();
    public InvoiceDAO createInvoiceDAO();
    public PriceDAO createPriceDAO();
}
