package com.autoflex.inventory.resource;

import com.autoflex.inventory.dto.ProductionResponseDTO;
import com.autoflex.inventory.service.OptimizationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/optimization")
@Produces(MediaType.APPLICATION_JSON)
public class OptimizationResource {

    @Inject
    OptimizationService optimizationService;

    @GET
    @Path("/suggestion")
    public Response getProductionSuggestion() {
        // O serviço retora o DTO com quantities e totalValue
        ProductionResponseDTO suggestion = optimizationService.calculateOptimalProduction();
        return Response.ok(suggestion).build();
    }
}