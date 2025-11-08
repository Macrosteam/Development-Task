package com.omar.SpringTask.controller;

import com.omar.SpringTask.entity.Purchase;
import com.omar.SpringTask.entity.Refund;
import com.omar.SpringTask.report.ReportCache;
import com.omar.SpringTask.repository.CustomerRepository;
import com.omar.SpringTask.repository.ProductRepository;
import com.omar.SpringTask.repository.RefundRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.omar.SpringTask.repository.CustomerRepository;
import com.omar.SpringTask.repository.ProductRepository;
import com.omar.SpringTask.repository.PurchaseRepository;

import java.sql.Ref;

@RestController
@RequestMapping("/refund")
public class RefundController {

    private final RefundRepository refundRepository;

    @Autowired
    private ReportCache reportCache;
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public RefundController(RefundRepository refundRepository, PurchaseRepository purchaseRepository,
                            ProductRepository productRepository, CustomerRepository customerRepository) {

        this.refundRepository = refundRepository;
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity<Refund> create(@Valid @RequestBody Refund c) {
        if (!purchaseRepository.existsById(c.getPurchaseId())) {
            return ResponseEntity.notFound().build();
        }
        Purchase purchase = purchaseRepository.findById(c.getPurchaseId()).get();

        //check if the customerID and productID matches that of the purchase
        //so we don't get a contradiction
        if(purchase.getCustomerId()!=c.getCustomerId()||purchase.getProductId()!=c.getProductId())
        {
            return ResponseEntity.notFound().build();
        }

        //check if the total refunded amount do not exceed
        //the original purchase amount
        Long total_refunded = refundRepository.getTotalRefundAmountByPurchaseId(c.getPurchaseId());
        if(total_refunded+ c.getAmount()>purchase.getAmount())
        {
            return ResponseEntity.badRequest().build();
        }

        Refund saved = refundRepository.save(c);
        reportCache.newRefund(saved.getId());
        return ResponseEntity.ok().build();
    }
}
