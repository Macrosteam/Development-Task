package com.omar.SpringTask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.omar.SpringTask.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}