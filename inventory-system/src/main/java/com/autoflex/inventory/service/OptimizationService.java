package com.autoflex.inventory.service;

import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.repository.ProductRawMaterialRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.MaxIter;

import java.math.BigDecimal;
import java.util.*;

// Service para otimização de produção usando Programação Linear (Simplex)

@ApplicationScoped
public class OptimizationService {
    
    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;
    
    @Inject
    RawMaterialRepository rawMaterialRepository;
    
    /**
     * Calcula a produção ótima usando Simplex
     * 
     * @return Mapa com produto ID → quantidade a produzir
     */
/**
 * Calcula a produção ótima usando Simplex
 * Retorna quantidades (arredondadas para baixo)
 * 
 * @return Mapa com produto ID → quantidade inteira a produzir
 */
    public Map<Long, Long> calculateOptimalProduction() {
        try {
            //Buscar todos os dados
            List<ProductRawMaterial> associations = productRawMaterialRepository.findAll().list();
            List<RawMaterial> rawMaterials = rawMaterialRepository.findAll().list();
            
            //Validar dados
            if (associations.isEmpty() || rawMaterials.isEmpty()) {
                return new HashMap<>();
            }
            
            //Extrair produtos únicos
            Set<Long> productIds = new HashSet<>();
            for (ProductRawMaterial assoc : associations) {
                productIds.add(assoc.getProduct().getId());
            }
            
            List<Long> products = new ArrayList<>(productIds);
            int numProducts = products.size();
            
            //Construir função objetivo Z = c1*x1 + c2*x2 + ... + cn*xn
            double[] objectiveCoefficients = new double[numProducts];
            for (int i = 0; i < numProducts; i++) {
                Long productId = products.get(i);
                Product product = associations.stream()
                        .filter(a -> a.getProduct().getId().equals(productId))
                        .map(ProductRawMaterial::getProduct)
                        .findFirst()
                        .orElse(null);
                
                if (product != null) {
                    objectiveCoefficients[i] = product.getValue().doubleValue();
                }
            }
            
            //Construir restrições 
            List<LinearConstraint> constraints = new ArrayList<>();
            
            for (RawMaterial rawMaterial : rawMaterials) {
                double[] coefficients = new double[numProducts];
                
                for (int i = 0; i < numProducts; i++) {
                    Long productId = products.get(i);
                    
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
            
            //Adicionei restrições de não-negatividade (x >= 0)
            for (int i = 0; i < numProducts; i++) {
                double[] coefficients = new double[numProducts];
                coefficients[i] = 1.0;
                constraints.add(new LinearConstraint(coefficients, Relationship.GEQ, 0.0));
            }
            
            //Simplex
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
            
            //resultado para produto ID → quantidade 
            Map<Long, Long> result = new HashMap<>();
            double[] point = solution.getPoint();
            
            for (int i = 0; i < numProducts; i++) {
                //Arredondar para baixo 
                long quantity = (long) Math.floor(point[i]);
                result.put(products.get(i), quantity);
            }
            
            return result;
            
        } catch (Exception e) {
            //se der erro retorna vazio (simplex falhou)
            System.err.println("Erro ao calcular produção ótima: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
