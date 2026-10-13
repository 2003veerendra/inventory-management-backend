
package com.veerendra.inventorymanagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veerendra.inventorymanagement.repository.ProductRepository;
@CrossOrigin(origins = {
	    "http://localhost:5173",
	    "http://localhost:5174",
	    "http://localhost:5175"
	})
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Product> create(
            @RequestBody Product product) {

        product.setId(null);
        Product saved = repository.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // READ ALL
    @GetMapping
    public List<Product> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable Long id,
            @RequestBody Product details) {

        return repository.findById(id)
                .map(existing -> {
                    existing.setName(details.getName());
                    existing.setCategory(details.getCategory());
                    existing.setSupplier(details.getSupplier());
                    existing.setPrice(details.getPrice());
                    existing.setStockQuantity(details.getStockQuantity());

                    Product updated = repository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
