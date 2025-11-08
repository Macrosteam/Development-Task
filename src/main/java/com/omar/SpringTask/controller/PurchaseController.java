package com.omar.SpringTask.controller;

import com.omar.SpringTask.entity.Purchase;
import com.omar.SpringTask.report.ReportCache;

import com.omar.SpringTask.repository.PurchaseRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;

import com.omar.SpringTask.repository.CustomerRepository;
import com.omar.SpringTask.repository.ProductRepository;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;

    @Autowired
    private ReportCache reportCache;

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public PurchaseController(PurchaseRepository purchaseRepository,CustomerRepository customerRepository,
    ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<Purchase> create(@Valid @RequestBody Purchase c) {
        if (!customerRepository.existsById(c.getCustomerId())) {
            return ResponseEntity.notFound().build();
        }
        if (!productRepository.existsById(c.getProductId())) {
            return ResponseEntity.notFound().build();
        }
        Purchase saved = purchaseRepository.save(c);
        reportCache.newPurchase(saved.getId());
        return ResponseEntity.ok().build();
    }
}