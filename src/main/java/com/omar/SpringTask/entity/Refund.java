package com.omar.SpringTask.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long purchaseId;

    //The following two columns are extra information. They are stored in the purchase table
    //Optimally, they should not be stored
    //I thought that if products in the refund column are meant to be a list or (subset) from
    //the list of products in the purchase column it would make more sense
    //but that is not indicated in requirements
    private Long customerId;
    private Long productId;

    @Positive()
    private Long amount;

    public Refund() {}

    public Refund(Long purchaseId,Long customerId, Long productId, Long amount) {
        this.purchaseId=purchaseId;
        this.customerId = customerId;
        this.productId = productId;
        this.amount=amount;
    }

    public Long getId() {
        return id;
    }
    public Long getPurchaseId() {
        return purchaseId;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public Long getProductId() {
        return productId;
    }
    public Long getAmount() {
        return amount;
    }
    public void setPurchaseIde(Long purchaseId) {
        this.purchaseId = purchaseId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
