package com.autoflex.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "product_raw_materials")
public class ProductRawMaterial {
    
    // ID 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Ref Produto
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    // Ref Matéria-Prima 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;
    
    // Quantidade necessária
    @Column(name = "quantity_needed", nullable = false)
    private BigDecimal quantityNeeded;
    
    
    public ProductRawMaterial() {
    }
    
    // Construtor
    public ProductRawMaterial(Product product, RawMaterial rawMaterial, BigDecimal quantityNeeded) {
        this.product = product;
        this.rawMaterial = rawMaterial;
        this.quantityNeeded = quantityNeeded;
    }
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }
    
    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }
    
    public BigDecimal getQuantityNeeded() {
        return quantityNeeded;
    }
    
    public void setQuantityNeeded(BigDecimal quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }
    
    //toString
    @Override
    public String toString() {
        return "ProductRawMaterial{" +
                "id=" + id +
                ", product=" + product +
                ", rawMaterial=" + rawMaterial +
                ", quantityNeeded=" + quantityNeeded +
                '}';
    }
}
