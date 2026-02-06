package com.autoflex.inventory.resource;

import com.autoflex.inventory.service.OptimizationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 * Resource REST para Otimização de Produção
 * Fornece o endpoint para calcular a melhor sugestão de produção
 */
@Path("/optimization")
@Produces(MediaType.APPLICATION_JSON)
public class OptimizationResource {

    @Inject
    OptimizationService optimizationService;

    /**
     * GET /optimization/suggest
     * Retorna a sugestão de produção ótima baseada no estoque atual
     */
    @GET
    @Path("/suggest")
    public Response getOptimalProduction() {
        try {
            Map<Long, Long> suggestion = optimizationService.calculateOptimalProduction();
            
            if (suggestion.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("Não foi possível gerar uma sugestão. Verifique se há produtos e matérias-primas cadastrados.")
                        .build();
            }
            
            return Response.ok(suggestion).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao processar otimização: " + e.getMessage())
                    .build();
        }
    }
}
