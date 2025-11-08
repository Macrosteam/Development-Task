package com.omar.SpringTask.controller;

import org.springframework.http.ResponseEntity;
import com.omar.SpringTask.entity.Customer;
import com.omar.SpringTask.repository.CustomerRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer c) {
        customerRepository.save(c);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id,
                                           @Valid @RequestBody Customer updated) {

        Optional<Customer> existing = customerRepository.findById(id);

        if (existing.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        Customer c = existing.get();
        c.setName(updated.getName());
        c.setPhone(updated.getPhone());
        customerRepository.save(c);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!customerRepository.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
