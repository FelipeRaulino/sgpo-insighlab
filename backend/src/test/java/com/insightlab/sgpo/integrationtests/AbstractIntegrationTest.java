package com.insightlab.sgpo.integrationtests;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public static final PostgreSQLContainer<?> postgreSQLContainer =
                new PostgreSQLContainer<>("postgres:16.3");

        private static void startContainer(){
            Startables.deepStart(Stream.of(postgreSQLContainer)).join();
        }

        private static Map<String, Object> createConnectionConfig(){
            Map<String, Object> connectionConfig = new HashMap<>();

            connectionConfig.put("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
            connectionConfig.put("spring.datasource.username", postgreSQLContainer.getUsername());
            connectionConfig.put("spring.datasource.password", postgreSQLContainer.getPassword());

            return connectionConfig;
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainer();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers",
                    createConnectionConfig()
            );

            environment.getPropertySources().addFirst(testcontainers);
        }
    }

}
