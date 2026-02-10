package com.autoflex.inventory.resource;

import com.autoflex.inventory.entity.Product;
import com.autoflex.inventory.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;


@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    
    // Injeta o Service
    @Inject
    ProductService productService;
    

    @GET
    public Response getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return Response.ok(products).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar produtos: " + e.getMessage())
                    .build();
        }
    }
    

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Produto não encontrado")
                        .build();
            }
            return Response.ok(product).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar produto: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * POST 
     * esperado:
     * {
     *   "name": "Produto A",
     *   "value": 1000.00
     * }
     */
    @POST
    public Response createProduct(Product product) {
        try {
            Product created = productService.createProduct(product.getName(), product.getValue());
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar produto: " + e.getMessage())
                    .build();
        }
    }
    

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product) {
        try {
            Product updated = productService.updateProduct(id, product.getName(), product.getValue());
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar produto: " + e.getMessage())
                    .build();
        }
    }
    

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            productService.deleteProduct(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar produto: " + e.getMessage())
                    .build();
        }
    }
}
