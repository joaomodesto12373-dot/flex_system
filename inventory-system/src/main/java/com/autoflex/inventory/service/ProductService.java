package com.autoflex.inventory.service;

import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.Repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


@ApplicationScoped
public class ProductService {
    
    // Injeta o Repository 
    @Inject
    ProductRepository productRepository;
    
    /**
     * Busca todos os produtos
     * 
     * @return Lista de todos os produtos
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll().list();
    }
    
    /**
     * Busca um produto por ID
     * 
     * @param id ID do produto
     * @return O produto encontrado
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    /**
     * Cria um novo produto
     * Valida os dados antes de salvar
     * 
     * @param name Nome do produto
     * @param value Valor/preço do produto
     * @return O produto criado
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    @Transactional
    public Product createProduct(String name, BigDecimal value) {
        // Validações
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio");
        }
        
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do produto deve ser maior que zero");
        }
        
        // Criar e salvar
        Product product = new Product(name, value);
        productRepository.persist(product);
        
        return product;
    }
    
    /**
     * Atualiza um produto existente
     * 
     * @param id ID do produto
     * @param name Novo nome
     * @param value Novo valor
     * @return O produto atualizado
     * @throws IllegalArgumentException se o produto não existir
     */
    @Transactional
    public Product updateProduct(Long id, String name, BigDecimal value) {
        Product product = productRepository.findById(id);
        
        if (product == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " não encontrado");
        }
        
        // Validações
        if (name != null && !name.trim().isEmpty()) {
            product.setName(name);
        }
        
        if (value != null && value.compareTo(BigDecimal.ZERO) > 0) {
            product.setValue(value);
        }
        
        productRepository.persist(product);
        
        return product;
    }
    
    /**
     * Deleta um produto
     * 
     * @param id ID do produto a deletar
     * @throws IllegalArgumentException se o produto não existir
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id);
        
        if (product == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " não encontrado");
        }
        
        productRepository.delete(product);
    }
}
