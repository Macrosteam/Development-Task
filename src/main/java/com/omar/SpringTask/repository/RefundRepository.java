package com.omar.SpringTask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.omar.SpringTask.entity.Refund;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface RefundRepository extends JpaRepository<Refund, Long> {
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Refund r WHERE r.purchaseId = :purchaseId")
    Long getTotalRefundAmountByPurchaseId(@Param("purchaseId") Long purchaseId);
    List<Refund> findByIdBetween(Long startId, Long endId);

}