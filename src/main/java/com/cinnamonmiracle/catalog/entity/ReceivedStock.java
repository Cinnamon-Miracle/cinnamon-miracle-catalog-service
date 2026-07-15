package com.cinnamonmiracle.catalog.entity;

import com.cinnamonmiracle.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Port of models/ReceivedStock.js */
@Entity
@Table(name = "received_stocks")
public class ReceivedStock extends BaseEntity {

    @NotBlank(message = "Received product id is required")
    @Column(nullable = false, unique = true)
    private String receivedProductID;

    @NotBlank(message = "Received product name is required")
    @Size(max = 100, message = "Received product name cannot exceed 100 characters")
    @Column(nullable = false, unique = true)
    private String receivedProductName;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @NotNull(message = "Qty is required")
    @Column(nullable = false)
    private Double qty;

    @NotBlank(message = "Reason is required")
    @Column(nullable = false)
    private String remark;

    @NotBlank(message = "Created by is required")
    @Column(nullable = false)
    private String createdBy;

    public String getReceivedProductID() {
        return receivedProductID;
    }

    public void setReceivedProductID(String receivedProductID) {
        this.receivedProductID = receivedProductID;
    }

    public String getReceivedProductName() {
        return receivedProductName;
    }

    public void setReceivedProductName(String receivedProductName) {
        this.receivedProductName = receivedProductName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
