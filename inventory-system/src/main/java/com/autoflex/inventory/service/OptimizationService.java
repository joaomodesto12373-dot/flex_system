package com.autoflex.inventory.service;

import com.autoflex.inventory.dto.ProductionResponseDTO;
import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.Repository.ProductRawMaterialRepository;
import com.autoflex.inventory.Repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import java.util.*;

@ApplicationScoped
public class OptimizationService {
    
    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;
    
    @Inject
    RawMaterialRepository rawMaterialRepository;

    public ProductionResponseDTO calculateOptimalProduction() {
        try {
            List<ProductRawMaterial> associations = productRawMaterialRepository.findAll().list();
            List<RawMaterial> rawMaterials = rawMaterialRepository.findAll().list();
            
            if (associations.isEmpty() || rawMaterials.isEmpty()) {
                return new ProductionResponseDTO(new HashMap<>(), 0.0);
            }
            
            Set<Long> productIds = new HashSet<>();
            for (ProductRawMaterial assoc : associations) {
                productIds.add(assoc.getProduct().getId());
            }
            
            List<Long> productsList = new ArrayList<>(productIds);
            int numProducts = productsList.size();
            
            // Z = c1*x1 + c2*x2 + ...
            double[] objectiveCoefficients = new double[numProducts];
            for (int i = 0; i < numProducts; i++) {
                Long productId = productsList.get(i);
                Product product = associations.stream()
                        .filter(a -> a.getProduct().getId().equals(productId))
                        .map(ProductRawMaterial::getProduct)
                        .findFirst()
                        .orElse(null);
                
                if (product != null) {
                    objectiveCoefficients[i] = product.getValue().doubleValue();
                }
            }
            
            List<LinearConstraint> constraints = new ArrayList<>();
            for (RawMaterial rawMaterial : rawMaterials) {
                double[] coefficients = new double[numProducts];
                for (int i = 0; i < numProducts; i++) {
                    Long productId = productsList.get(i);
                    double quantityNeeded = associations.stream()
                            .filter(a -> a.getProduct().getId().equals(productId) 
                                    && a.getRawMaterial().getId().equals(rawMaterial.getId()))
                            .mapToDouble(a -> a.getQuantityNeeded().doubleValue())
                            .findFirst()
                            .orElse(0.0);
                    coefficients[i] = quantityNeeded;
                }
                double availableQuantity = rawMaterial.getQuantityInStock().doubleValue();
                constraints.add(new LinearConstraint(coefficients, Relationship.LEQ, availableQuantity));
            }
            
            LinearObjectiveFunction objective = new LinearObjectiveFunction(objectiveCoefficients, 0);
            LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
            
            SimplexSolver solver = new SimplexSolver();
            PointValuePair solution = solver.optimize(
                    new MaxIter(1000),
                    objective,
                    constraintSet,
                    GoalType.MAXIMIZE,
                    new NonNegativeConstraint(true)
            );
            
            Map<Long, Long> quantities = new HashMap<>();
            double[] point = solution.getPoint();
            double totalValue = 0;
            
            for (int i = 0; i < numProducts; i++) {
                long qty = (long) Math.floor(point[i]);
                quantities.put(productsList.get(i), qty);
                totalValue += qty * objectiveCoefficients[i];
            }
            
            return new ProductionResponseDTO(quantities, totalValue);
            
        } catch (Exception e) {
            System.err.println("Error calculating production: " + e.getMessage());
            return new ProductionResponseDTO(new HashMap<>(), 0.0);
        }
    }
}