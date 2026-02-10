package com.autoflex.inventory.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "products")
public class Product {
    
    // ID da entidade 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Nome do produto
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    // Valor/preço do produto
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    
    
    // Construtor vazio para busca
    public Product() {
    }
    
    // Construtor 
    public Product(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
    }
    
    // Getters e Setters

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
    
    public BigDecimal getValue() {
        return value;
    }
    
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
