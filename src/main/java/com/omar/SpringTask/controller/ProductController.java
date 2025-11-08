package com.omar.SpringTask.controller;

import com.omar.SpringTask.entity.Product;
import com.omar.SpringTask.repository.ProductRepository;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product c) {
        productRepository.save(c);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product updated) {
        Optional<Product> existing = productRepository.findById(id);

        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        Product c = existing.get();
        c.setName(updated.getName());
        c.setPrice(updated.getPrice());
        productRepository.save(c);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productRepository.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
