package com.cinnamonmiracle.catalog.entity;

import com.cinnamonmiracle.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/** Port of models/Product.js */
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @NotBlank(message = "Item code is required")
    @Column(nullable = false, unique = true)
    private String itemCode;

    @NotNull(message = "Price is required")
    @Column(nullable = false)
    private Double price;

    @NotBlank(message = "Brand name is required")
    @Column(nullable = false)
    private String brandName;

    @NotBlank(message = "Item name is required")
    @Column(nullable = false)
    private String itemName;

    // Stored as text so large base64 images fit. The original declared this
    // "unique", but a DB unique (btree) index over large image data is not
    // feasible on PostgreSQL, so the unique index is intentionally omitted.
    @NotBlank(message = "Product image is required")
    @Column(nullable = false, columnDefinition = "text")
    private String productImage;

    @NotBlank(message = "Category code is required")
    @Column(nullable = false)
    private String categoryCode;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Boolean isActive = true;

    @NotBlank(message = "Updated by is required")
    @Column(nullable = false)
    private String updatedBy;

    @NotBlank(message = "Manufacturer name is required")
    @Column(nullable = false)
    private String manufacturerName;

    @NotBlank(message = "Manufacturer code is required")
    @Column(nullable = false)
    private String manufacturerCode;

    @NotNull(message = "Warranty period is required")
    @Column(nullable = false)
    private Double warrantyPeriod;

    @NotBlank(message = "Warranty details are required")
    @Column(nullable = false)
    private String warrantyDetails;

    @NotBlank(message = "Warranty claim process is required")
    @Column(nullable = false)
    private String warrantyClaimProcess;

    @NotNull(message = "Manufacture date is required")
    @Column(nullable = false)
    private Instant manufactureDate;

    @NotNull(message = "Expire date is required")
    @Column(nullable = false)
    private Instant expireDate;

    @NotNull(message = "Stocks are required")
    @Column(nullable = false)
    private Double stocks;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public Double getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(Double warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getWarrantyDetails() {
        return warrantyDetails;
    }

    public void setWarrantyDetails(String warrantyDetails) {
        this.warrantyDetails = warrantyDetails;
    }

    public String getWarrantyClaimProcess() {
        return warrantyClaimProcess;
    }

    public void setWarrantyClaimProcess(String warrantyClaimProcess) {
        this.warrantyClaimProcess = warrantyClaimProcess;
    }

    public Instant getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Instant manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public Double getStocks() {
        return stocks;
    }

    public void setStocks(Double stocks) {
        this.stocks = stocks;
    }
}
