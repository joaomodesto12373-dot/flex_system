package com.autoflex.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "raw_materials")
public class RawMaterial {
    
    // ID da matéria-prima 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Nome 
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    // Quantidade em estoque
    @Column(name = "quantity_in_stock", nullable = false)
    private BigDecimal quantityInStock;
    
    
    // Construtor vazio 
    public RawMaterial() {
    }
    
    // Construtor 
    public RawMaterial(String name, BigDecimal quantityInStock) {
        this.name = name;
        this.quantityInStock = quantityInStock;
    }
    
    // getters e setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getQuantityInStock() {
        return quantityInStock;
    }
    
    public void setQuantityInStock(BigDecimal quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    
    // toString 
    
    @Override
    public String toString() {
        return "RawMaterial{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantityInStock=" + quantityInStock +
                '}';
    }
}
