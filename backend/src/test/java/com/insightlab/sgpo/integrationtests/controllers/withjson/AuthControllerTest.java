package com.insightlab.sgpo.integrationtests.controllers.withjson;

import com.insightlab.sgpo.config.TestConfig;
import com.insightlab.sgpo.data.dtos.v1.security.AuthenticationRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.security.CreateUserRequestDTO;
import com.insightlab.sgpo.data.dtos.v1.security.CreateUserResponseDTO;
import com.insightlab.sgpo.data.dtos.v1.security.TokenResponseDTO;
import com.insightlab.sgpo.integrationtests.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification requestSpecification;

    private static String createdUserId;

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

        createdUserId = content.id();
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
                .setBasePath("/api/v1/users")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(5)
    void shouldBeAbleToDeleteUser(){
        var response = given().spec(requestSpecification)
                .pathParams("id", createdUserId)
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

        assertNotNull(response);
    }

}
