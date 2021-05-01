package com.bug.influx;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.InfluxDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {InfluxDBIntegrationTest.Initializer.class})
public class InfluxDBIntegrationTest {

    @LocalServerPort
    int localPort;

    @Container
    public static InfluxDBContainer<?> influxDBContainer = new InfluxDBContainer<>(DockerImageName.parse("influxdb:1.4.3"));

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            influxDBContainer.start();

            TestPropertyValues
                    .of("influxdb.url=" + influxDBContainer.getUrl(),
                            "influxdb.password=" + "password",
                            "influxdb.username=" + "admin")
                    .applyTo(configurableApplicationContext.getEnvironment());

        }

    }

    @Before
    public void setUp() {
        influxDBContainer.isRunning();
        RestAssured.port = localPort;
    }

    @Test
    public void test() {
        influxDBContainer.isRunning();
        System.out.println("context loads");

        given()
                .basePath("/app")
                .post("/" + 0.6)
                .then()
                .statusCode(200);

        Response response = given()
                .basePath("/app")
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().print());
        Assert.assertTrue(response.getBody().print().contains("0.6"));
    }
}
