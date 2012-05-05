/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magnetpwns.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author MagNet
 */
public class Product implements Identifiable {
    
    private ProductId id;
    private String code;
    private String codeManuf;
    private String name;
    private String description;
    private BigDecimal price;
    private Map<PriceId, BigDecimal> altPrices;
    private PriceId altPriceId;
    private BigDecimal boughtPrice;
    private Manufacturer manufacturer;
    private TaxRate taxRate;
    private Unit unit;

    public Product(ProductId id, String code, String codeManuf, String name, String description, BigDecimal price, BigDecimal boughtPrice, Manufacturer manufacturer, TaxRate taxRate, Unit unit) {
        this.id = id;
        this.code = code;
        this.codeManuf = codeManuf;
        this.name = name;
        this.description = description;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.boughtPrice = boughtPrice.setScale(2, RoundingMode.HALF_UP);
        this.manufacturer = manufacturer;
        this.taxRate = taxRate;
        this.unit = unit;
        altPrices = null;
        altPriceId = Price.DEFAULT;
    }

    public Map<PriceId, BigDecimal> getAltPrices() {
        return altPrices;
    }

    public PriceId getAltPriceId() {
        return altPriceId;
    }

    public void setAltPriceId(PriceId altPriceId) {
        if (altPriceId.equals(Price.DEFAULT)) {
            this.altPriceId = altPriceId;
            return;
        }
            
        if (!altPrices.containsKey(altPriceId))
            throw new IndexOutOfBoundsException("The selected price is not loaded");
        this.altPriceId = altPriceId;
    }
    
    public Collection<Price> getPriceList() {
        ArrayList<Price> prices = new ArrayList<Price>();
        prices.add(new Price(Price.DEFAULT, price));
        if (altPrices != null) {
            for (Map.Entry<PriceId, BigDecimal> e : altPrices.entrySet()) {
                prices.add(new Price(e.getKey(), e.getValue()));
            }
        }
        return prices;
    }
    
    public void setAltPrices(Map<PriceId, BigDecimal> altPrices) {
        this.altPrices = altPrices;
    }
    
    public BigDecimal getPriceBought() {
        return boughtPrice;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public ProductId getId() {
        return id;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        if (altPriceId.equals(Price.DEFAULT))
            return price;
        return altPrices.get(altPriceId);
    }
    
    public BigDecimal getUntaxedPrice() {
        return getPrice().divide(taxRate.getMultiplication(), 2, RoundingMode.HALF_UP);
    }

    public TaxRate getTaxRate() {
        return taxRate;
    }

    public String getCodeManuf() {
        return codeManuf;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public void setId(AbstractId id) {
        this.id = (ProductId) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (manufacturer.getName().length() > 0)
            return manufacturer.getName() + ' ' + name;
        return name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }
    
    
}
