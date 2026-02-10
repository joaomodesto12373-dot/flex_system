package com.autoflex.inventory.dto;

import java.util.Map;

public class ProductionResponseDTO {
    public Map<Long, Long> quantities;
    public Double totalValue;

    public ProductionResponseDTO(Map<Long, Long> quantities, Double totalValue) {
        this.quantities = quantities;
        this.totalValue = totalValue;
    }
}