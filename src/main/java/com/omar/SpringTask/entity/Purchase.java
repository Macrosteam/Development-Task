package com.omar.SpringTask.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long customerId;
    private Long productId;

    @Positive()
    private Long amount;

    public Purchase() {}

    public Purchase(Long customerId, Long productId, Long amount) {
        this.customerId = customerId;
        this.productId = productId;
        this.amount=amount;
    }

    public Long getId() {
        return id;
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
