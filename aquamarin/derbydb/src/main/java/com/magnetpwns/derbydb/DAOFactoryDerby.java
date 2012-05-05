/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.derbydb;

import com.magnetpwns.derbydb.impl.*;
import com.magnetpwns.integration.*;
import java.sql.Connection;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author MagNet
 */
@ServiceProvider(service=DAOFactoryInterface.class)
public class DAOFactoryDerby implements DAOFactoryInterface {

    Connection connection;

    public DAOFactoryDerby() {
        connection = DerbyConnection.getInstance().getConnection();
    }
    
    @Override
    public ClientDAO createClientDAO() {
        return ClientDAODerby.getInstance(connection);
    }

    @Override
    public CityDAO createCityDAO() {
        return CityDAODerby.getInstance(connection);
    }

    @Override
    public ManufacturerDAO createManufacturerDAO() {
        return ManufacturerDAODerby.getInstance(connection);
    }

    @Override
    public ProductDAO createProductDAO() {
        return ProductDAODerby.getInstance(connection);
    }

    @Override
    public TaxRateDAO createTaxRateDAO() {
        return TaxRateDAODerby.getInstance(connection);
    }

    @Override
    public UnitDAO createUnitDAO() {
        return UnitDAODerby.getInstance(connection);
    }

    @Override
    public StockDAO createStockDAO() {
        return StockDAODerby.getInstance(connection);
    }

    @Override
    public StockTransferDAO createStockTransferDAO() {
        return StockTransferDAODerby.getInstance(connection);
    }

    @Override
    public CountryDAO createCountryDAO() {
        return CountryDAODerby.getInstance(connection);
    }

    @Override
    public ProformaDAO createProformaDAO() {
        return ProformaDAODerby.getInstance(connection);
    }

    @Override
    public DeliveryDAO createDeliveryDAO() {
        return DeliveryDAODerby.getInstance(connection);
    }
    
    @Override
    public InvoiceDAO createInvoiceDAO() {
        return InvoiceDAODerby.getInstance(connection);
    }

    @Override
    public PriceDAO createPriceDAO() {
        return PriceDAODerby.getInstance(connection);
    }
}
