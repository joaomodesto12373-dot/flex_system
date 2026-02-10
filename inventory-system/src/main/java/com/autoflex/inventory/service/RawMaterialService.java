package com.autoflex.inventory.service;

import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.Repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


@ApplicationScoped
public class RawMaterialService {
    
    // Injeta o Repository
    @Inject
    RawMaterialRepository rawMaterialRepository;
    
    /**
     * Busca todas as matérias-primas
     * 
     * @return Lista de todas as matérias-primas
     */
    public List<RawMaterial> getAllRawMaterials() {
        return rawMaterialRepository.findAll().list();
    }
    
    /**
     * Busca uma matéria-prima por ID
     * 
     * @param id ID da matéria-prima
     * @return A matéria-prima encontrada
     */
    public RawMaterial getRawMaterialById(Long id) {
        return rawMaterialRepository.findById(id);
    }
    
    /**
     * Cria uma nova matéria-prima
     * Valida os dados antes de salvar
     * 
     * @param name Nome da matéria-prima
     * @param quantityInStock Quantidade em estoque
     * @return A matéria-prima criada
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    @Transactional
    public RawMaterial createRawMaterial(String name, BigDecimal quantityInStock) {
        // Validações
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da matéria-prima não pode ser vazio");
        }
        
        if (quantityInStock == null || quantityInStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa");
        }
        
        // Criar e salvar
        RawMaterial rawMaterial = new RawMaterial(name, quantityInStock);
        rawMaterialRepository.persist(rawMaterial);
        
        return rawMaterial;
    }
    
    /**
     * Atualiza uma matéria-prima existente
     * 
     * @param id ID da matéria-prima
     * @param name Novo nome
     * @param quantityInStock Nova quantidade em estoque
     * @return A matéria-prima atualizada
     * @throws IllegalArgumentException se a matéria-prima não existir
     */
    @Transactional
    public RawMaterial updateRawMaterial(Long id, String name, BigDecimal quantityInStock) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id);
        
        if (rawMaterial == null) {
            throw new IllegalArgumentException("Matéria-prima com ID " + id + " não encontrada");
        }
        
        // Validações
        if (name != null && !name.trim().isEmpty()) {
            rawMaterial.setName(name);
        }
        
        if (quantityInStock != null && quantityInStock.compareTo(BigDecimal.ZERO) >= 0) {
            rawMaterial.setQuantityInStock(quantityInStock);
        }
        
        rawMaterialRepository.persist(rawMaterial);
        
        return rawMaterial;
    }
    
    /**
     * Deleta uma matéria-prima
     * 
     * @param id ID da matéria-prima a deletar
     * @throws IllegalArgumentException se a matéria-prima não existir
     */
    @Transactional
    public void deleteRawMaterial(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id);
        
        if (rawMaterial == null) {
            throw new IllegalArgumentException("Matéria-prima com ID " + id + " não encontrada");
        }
        
        rawMaterialRepository.delete(rawMaterial);
    }
}
