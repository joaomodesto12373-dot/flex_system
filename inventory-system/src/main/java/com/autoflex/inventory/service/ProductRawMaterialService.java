package com.autoflex.inventory.service;

import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.Repository.ProductRawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


@ApplicationScoped
public class ProductRawMaterialService {
    
    // Injeta o Repository
    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;
    
    @Inject
    ProductService productService;
    
    @Inject
    RawMaterialService rawMaterialService;
    
    /**
     * Busca todas as associações
     * 
     * @return Lista de todas as associações
     */
    public List<ProductRawMaterial> getAllAssociations() {
        return productRawMaterialRepository.findAll().list();
    }
    
    /**
     * Busca uma associação por ID
     * 
     * @param id ID da associação
     * @return A associação encontrada
     */
    public ProductRawMaterial getAssociationById(Long id) {
        return productRawMaterialRepository.findById(id);
    }
    
    /**
     * Busca todas as matérias-primas necessárias para um produto
     * 
     * @param productId ID do produto
     * @return Lista de associações do produto
     */
    public List<ProductRawMaterial> getProductRawMaterials(Long productId) {
        return productRawMaterialRepository.findByProductId(productId);
    }
    
    /**
     * Busca todos os produtos que usam uma matéria-prima
     * 
     * @param rawMaterialId ID da matéria-prima
     * @return Lista de associações da matéria-prima
     */
    public List<ProductRawMaterial> getProductsUsingRawMaterial(Long rawMaterialId) {
        return productRawMaterialRepository.findByRawMaterialId(rawMaterialId);
    }
    
    /**
     * Cria uma nova associação entre Produto e Matéria-Prima
     * Valida os dados antes de salvar
     * 
     * @param productId ID do produto
     * @param rawMaterialId ID da matéria-prima
     * @param quantityNeeded Quantidade de matéria-prima necessária
     * @return A associação criada
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    @Transactional
    public ProductRawMaterial createAssociation(Long productId, Long rawMaterialId, BigDecimal quantityNeeded) {
        // Validações
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Produto com ID " + productId + " não encontrado");
        }
        
        RawMaterial rawMaterial = rawMaterialService.getRawMaterialById(rawMaterialId);
        if (rawMaterial == null) {
            throw new IllegalArgumentException("Matéria-prima com ID " + rawMaterialId + " não encontrada");
        }
        
        if (quantityNeeded == null || quantityNeeded.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantidade necessária deve ser maior que zero");
        }
        
        // Criar e salvar
        ProductRawMaterial association = new ProductRawMaterial(product, rawMaterial, quantityNeeded);
        productRawMaterialRepository.persist(association);
        
        return association;
    }
    
    /**
     * Atualiza uma associação existente
     * 
     * @param id ID da associação
     * @param quantityNeeded Nova quantidade necessária
     * @return A associação atualizada
     * @throws IllegalArgumentException se a associação não existir
     */
    @Transactional
    public ProductRawMaterial updateAssociation(Long id, BigDecimal quantityNeeded) {
        ProductRawMaterial association = productRawMaterialRepository.findById(id);
        
        if (association == null) {
            throw new IllegalArgumentException("Associação com ID " + id + " não encontrada");
        }
        
        if (quantityNeeded != null && quantityNeeded.compareTo(BigDecimal.ZERO) > 0) {
            association.setQuantityNeeded(quantityNeeded);
        }
        
        productRawMaterialRepository.persist(association);
        
        return association;
    }
    
    /**
     * Deleta uma associação
     * 
     * @param id ID da associação a deletar
     * @throws IllegalArgumentException se a associação não existir
     */
    @Transactional
    public void deleteAssociation(Long id) {
        ProductRawMaterial association = productRawMaterialRepository.findById(id);
        
        if (association == null) {
            throw new IllegalArgumentException("Associação com ID " + id + " não encontrada");
        }
        
        productRawMaterialRepository.delete(association);
    }
}
