package com.autoflex.inventory;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ProductResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/products")
          .then()
             .statusCode(200);
    }
}
