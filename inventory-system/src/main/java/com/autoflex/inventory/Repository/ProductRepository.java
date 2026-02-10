package com.autoflex.inventory.Repository;

import com.autoflex.inventory.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// Repository para a entidade Product

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    
    // Métodos usaveis 
    // - findById(Long id)
    // - findAll()
    // - persist(Product p)
    // - delete(Product p)
    // - deleteById(Long id)
    // - add novos depois se necessário
}
