package com.autoflex.inventory.Repository;

import com.autoflex.inventory.entity.RawMaterial;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// Repository para a entidade RawMaterial

 
@ApplicationScoped
public class RawMaterialRepository implements PanacheRepository<RawMaterial> {
}
