package com.insightlab.sgpo.integrationtests.controllers.withjson;

import com.insightlab.sgpo.config.TestConfig;
import com.insightlab.sgpo.data.dtos.v1.security.*;
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
public class UserControllerTest extends AbstractIntegrationTest {

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
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/v1/users")
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(3)
    void shouldBeAbleToGetAllUsers(){
        List<UserResponseDTO> users = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<List<UserResponseDTO>>() {});

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    @Order(4)
    void shouldBeAbleToUpdateUser(){
        List<String> userRoles = new ArrayList<>();
        userRoles.add("ROLE_ADMIN");
        userRoles.add("ROLE_EMPLOYEE");

        UserUpdateRequestDTO userUpdateRequest = new UserUpdateRequestDTO(
          "usertestupdated",
                    userRoles
        );

        UserUpdateResponseDTO userUpdateResponse = given().spec(requestSpecification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(userUpdateRequest)
                .pathParams("id", createdUserId)
                .when()
                .put("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(UserUpdateResponseDTO.class);

        assertNotNull(userUpdateResponse);
        assertNotNull(userUpdateResponse.id());
        assertNotNull(userUpdateResponse.username());
        assertNotNull(userUpdateResponse.roles());

        assertEquals("usertestupdated", userUpdateResponse.username());

        assertFalse(userUpdateResponse.roles().isEmpty());

        requestSpecification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + userUpdateResponse.token())
                .setPort(TestConfig.SERVER_PORT)
                .setBasePath("/api/v1/users")
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
