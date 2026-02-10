package com.autoflex.inventory.Repository;

import com.autoflex.inventory.entity.ProductRawMaterial;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;


@ApplicationScoped
public class ProductRawMaterialRepository implements PanacheRepository<ProductRawMaterial> {

    
    /**
     * 1 busca todas as matérias-primas necessárias para um produto específico
     * 
     * @param productId ID do produto
     * @return Lista de ProductRawMaterial associados ao produto
     */
    public List<ProductRawMaterial> findByProductId(Long productId) {
        return find("product.id", productId).list();
    }
    
    /**
     * 2 busca todos os produtos que usam uma matéria-prima específica
     * 
     * @param rawMaterialId ID da matéria-prima
     * @return Lista de ProductRawMaterial que usam a matéria-prima
     */
    public List<ProductRawMaterial> findByRawMaterialId(Long rawMaterialId) {
        return find("rawMaterial.id", rawMaterialId).list();
    }
}
