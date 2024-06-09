package com.insightlab.sgpo.integrationtests.swagger;

import com.insightlab.sgpo.config.TestConfig;
import com.insightlab.sgpo.integrationtests.AbstractIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private String port;

    void setup(){
        RestAssured.port = Integer.parseInt(port);
    }

    @Test
    void shouldBeAbleToEstablishedConnection(){
        assertTrue(Initializer.postgreSQLContainer.isCreated());
        assertTrue(Initializer.postgreSQLContainer.isRunning());
    }

    @Test
    void shouldBeAbleToOpenSwaggerHomePage(){
        var content = given()
                .basePath("/swagger-ui/index.html")
                .port(TestConfig.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("Swagger UI"));
    }

}
