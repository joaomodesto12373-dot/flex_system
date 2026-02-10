package com.autoflex.inventory.resource;

import com.autoflex.inventory.entity.RawMaterial;
import com.autoflex.inventory.service.RawMaterialService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialResource {
    
    @Inject
    RawMaterialService rawMaterialService;
    

    @GET
    public Response getAllRawMaterials() {
        try {
            List<RawMaterial> rawMaterials = rawMaterialService.getAllRawMaterials();
            return Response.ok(rawMaterials).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar matérias-primas: " + e.getMessage())
                    .build();
        }
    }
    

    @GET
    @Path("/{id}")
    public Response getRawMaterialById(@PathParam("id") Long id) {
        try {
            RawMaterial rawMaterial = rawMaterialService.getRawMaterialById(id);
            if (rawMaterial == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Matéria-prima não encontrada")
                        .build();
            }
            return Response.ok(rawMaterial).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar matéria-prima: " + e.getMessage())
                    .build();
        }
    }
    

    @POST
    public Response createRawMaterial(RawMaterial rawMaterial) {
        try {
            RawMaterial created = rawMaterialService.createRawMaterial(
                    rawMaterial.getName(),
                    rawMaterial.getQuantityInStock()
            );
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar matéria-prima: " + e.getMessage())
                    .build();
        }
    }
    

    @PUT
    @Path("/{id}")
    public Response updateRawMaterial(@PathParam("id") Long id, RawMaterial rawMaterial) {
        try {
            RawMaterial updated = rawMaterialService.updateRawMaterial(
                    id,
                    rawMaterial.getName(),
                    rawMaterial.getQuantityInStock()
            );
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar matéria-prima: " + e.getMessage())
                    .build();
        }
    }
    

    @DELETE
    @Path("/{id}")
    public Response deleteRawMaterial(@PathParam("id") Long id) {
        try {
            rawMaterialService.deleteRawMaterial(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar matéria-prima: " + e.getMessage())
                    .build();
        }
    }
}
