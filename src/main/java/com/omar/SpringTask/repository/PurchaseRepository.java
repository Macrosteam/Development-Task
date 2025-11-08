package com.omar.SpringTask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.omar.SpringTask.entity.Purchase;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByIdBetween(Long startId, Long endId);
}