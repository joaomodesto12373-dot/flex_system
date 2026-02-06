package com.autoflex.inventory.resource;

import com.autoflex.inventory.entity.ProductRawMaterial;
import com.autoflex.inventory.service.ProductRawMaterialService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/product-raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRawMaterialResource {
    
    @Inject
    ProductRawMaterialService productRawMaterialService;
    

    @GET
    public Response getAllAssociations() {
        try {
            List<ProductRawMaterial> associations = productRawMaterialService.getAllAssociations();
            return Response.ok(associations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar associações: " + e.getMessage())
                    .build();
        }
    }
    

    @GET
    @Path("/{id}")
    public Response getAssociationById(@PathParam("id") Long id) {
        try {
            ProductRawMaterial association = productRawMaterialService.getAssociationById(id);
            if (association == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Associação não encontrada")
                        .build();
            }
            return Response.ok(association).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar associação: " + e.getMessage())
                    .build();
        }
    }
    

    @GET
    @Path("/product/{productId}")
    public Response getProductRawMaterials(@PathParam("productId") Long productId) {
        try {
            List<ProductRawMaterial> associations = productRawMaterialService.getProductRawMaterials(productId);
            return Response.ok(associations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar matérias-primas do produto: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/raw-material/{rawMaterialId}")
    public Response getProductsUsingRawMaterial(@PathParam("rawMaterialId") Long rawMaterialId) {
        try {
            List<ProductRawMaterial> associations = productRawMaterialService.getProductsUsingRawMaterial(rawMaterialId);
            return Response.ok(associations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar produtos: " + e.getMessage())
                    .build();
        }
    }
    

    @POST
    public Response createAssociation(ProductRawMaterial association) {
        try {
            ProductRawMaterial created = productRawMaterialService.createAssociation(
                    association.getProduct().getId(),
                    association.getRawMaterial().getId(),
                    association.getQuantityNeeded()
            );
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar associação: " + e.getMessage())
                    .build();
        }
    }
    

    @PUT
    @Path("/{id}")
    public Response updateAssociation(@PathParam("id") Long id, ProductRawMaterial association) {
        try {
            ProductRawMaterial updated = productRawMaterialService.updateAssociation(
                    id,
                    association.getQuantityNeeded()
            );
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar associação: " + e.getMessage())
                    .build();
        }
    }
    

    @DELETE
    @Path("/{id}")
    public Response deleteAssociation(@PathParam("id") Long id) {
        try {
            productRawMaterialService.deleteAssociation(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar associação: " + e.getMessage())
                    .build();
        }
    }
}
