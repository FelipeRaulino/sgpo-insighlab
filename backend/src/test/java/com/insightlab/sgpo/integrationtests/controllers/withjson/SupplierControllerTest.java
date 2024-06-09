package com.insightlab.sgpo.integrationtests.controllers.withjson;

import com.insightlab.sgpo.config.TestConfig;
import com.insightlab.sgpo.data.dtos.v1.security.AuthenticationRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.security.CreateUserRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.security.CreateUserResponseDTO;
import com.insightlab.sgpo.data.dtos.v1.security.TokenResponseDTO;
import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.supplier.SupplierResponseDTO;
import com.insightlab.sgpo.integrationtests.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SupplierControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;

    private static String createdSupplierId;

    @Test
    @Order(0)
    public void shouldBeAbleToCreateANewUser(){
        List<String> userRoles = new ArrayList<>();
        userRoles.add("ROLE_ADMIN");
        CreateUserRequestDTO userRequestDTO = new CreateUserRequestDTO("usertest", "teste123", userRoles);

        RequestSpecification customRequestSpec = new RequestSpecBuilder()
                .setBasePath("/auth/signup")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        CreateUserResponseDTO content = given().spec(customRequestSpec)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(userRequestDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(CreateUserResponseDTO.class);

        assertNotNull(content);
        assertNotNull(content.id());
        assertNotNull(content.username());
        assertNotNull(content.password());
        assertNotNull(content.roles());

        assertEquals("usertest", content.username());

        assertFalse(content.roles().isEmpty());
    }

    @Test
    @Order(1)
    void shouldBeAbleToAuthenticateUser(){
        AuthenticationRequestDTO authenticationRequest = new AuthenticationRequestDTO(
                "usertest",
                "teste123"
        );

        var accessToken = given()
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .port(TestConfig.SERVER_PORT)
                .basePath("/auth/signin")
                .body(authenticationRequest)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenResponseDTO.class)
                .accessToken();

        requestSpecification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/v1/suppliers")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(2)
    void shouldBeAbleToCreateANewSupplier(){
        SupplierRequestDTO supplierRequest = new SupplierRequestDTO(
                "Tech Solutions LLC",
                "43.859.224/0001-66",
                "(75) 99655-0194",
                "info@techsolutions.com",
                true
        );

        SupplierResponseDTO newSupplier = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(supplierRequest)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(SupplierResponseDTO.class);

        assertNotNull(newSupplier);
        assertNotNull(newSupplier.id());
        assertNotNull(newSupplier.name());
        assertNotNull(newSupplier.email());
        assertNotNull(newSupplier.phone());
        assertNotNull(newSupplier.createdAt());
        assertNotNull(newSupplier.taxId());
        assertNotNull(newSupplier.status());

        assertEquals("Tech Solutions LLC", newSupplier.name());
        assertEquals("43.859.224/0001-66", newSupplier.taxId());
        assertEquals("(75) 99655-0194", newSupplier.phone());
        assertEquals("info@techsolutions.com", newSupplier.email());

        assertTrue(newSupplier.status());

        createdSupplierId = newSupplier.id();
    }

    @Test
    @Order(3)
    void shouldBeAbleToGetSupplierById(){
        SupplierResponseDTO newSupplier = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParams("id", createdSupplierId)
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(SupplierResponseDTO.class);

        assertNotNull(newSupplier);
        assertNotNull(newSupplier.id());
        assertNotNull(newSupplier.name());
        assertNotNull(newSupplier.email());
        assertNotNull(newSupplier.phone());
        assertNotNull(newSupplier.createdAt());
        assertNotNull(newSupplier.taxId());
        assertNotNull(newSupplier.status());

        assertEquals(createdSupplierId, newSupplier.id());
        assertEquals("Tech Solutions LLC", newSupplier.name());
        assertEquals("43.859.224/0001-66", newSupplier.taxId());
        assertEquals("(75) 99655-0194", newSupplier.phone());
        assertEquals("info@techsolutions.com", newSupplier.email());

        assertTrue(newSupplier.status());

    }

    @Test
    @Order(4)
    void shouldBeAbleToUpdateASupplier(){
        SupplierRequestDTO supplierRequest = new SupplierRequestDTO(
                "Tech Solutions LLC",
                "99.999.0000/0000-00",
                "(99) 99999-9999",
                "info@techsolutions.com",
                true
        );

        SupplierResponseDTO updatedSupplier = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(supplierRequest)
                .pathParams("id", createdSupplierId)
                .when()
                .put("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(SupplierResponseDTO.class);

        assertNotNull(updatedSupplier);
        assertNotNull(updatedSupplier.id());
        assertNotNull(updatedSupplier.name());
        assertNotNull(updatedSupplier.email());
        assertNotNull(updatedSupplier.phone());
        assertNotNull(updatedSupplier.createdAt());
        assertNotNull(updatedSupplier.taxId());
        assertNotNull(updatedSupplier.status());

        assertEquals("Tech Solutions LLC", updatedSupplier.name());
        assertEquals("99.999.0000/0000-00", updatedSupplier.taxId());
        assertEquals("(99) 99999-9999", updatedSupplier.phone());
        assertEquals("info@techsolutions.com", updatedSupplier.email());

        assertTrue(updatedSupplier.status());
    }

    @Test
    @Order(5)
    void shouldBeAbleToDeleteASupplier(){
        given().spec(requestSpecification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", createdSupplierId)
            .when()
            .delete("{id}")
            .then()
            .statusCode(204);
    }

    @Test
    @Order(6)
    void shouldBeAbleToGetAllSuppliers(){
        List<SupplierResponseDTO> suppliers = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<List<SupplierResponseDTO>>() {});

        assertNotNull(suppliers);
        assertFalse(suppliers.isEmpty());
    }
}
