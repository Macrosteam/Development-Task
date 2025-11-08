package com.omar.SpringTask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.omar.SpringTask.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}