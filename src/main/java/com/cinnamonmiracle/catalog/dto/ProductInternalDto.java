package com.cinnamonmiracle.catalog.dto;

/**
 * Minimal product view exposed to other services (sales-service) over the
 * internal API — just what is needed to validate/decrement stock.
 */
public class ProductInternalDto {
    public String id;
    public String itemName;
    public Double stocks;

    public ProductInternalDto() {
    }

    public ProductInternalDto(String id, String itemName, Double stocks) {
        this.id = id;
        this.itemName = itemName;
        this.stocks = stocks;
    }
}
