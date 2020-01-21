package com.metalheart.test.integration.rest;

import com.metalheart.config.AppProperties;
import com.metalheart.EndPoint;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.net.HttpURLConnection;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;


@EnableAutoConfiguration
public class ValidationIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AppProperties properties;

    @Test
    public void testValidation() {

        given()
            .auth().preemptive().basic(properties.getSecurity().getDefaultUsername(), properties.getSecurity().getDefaultPassword())
            .port(port)
        .when()
            .queryParam("year", 2000)
            .queryParam("week", 11)
            .get(EndPoint.RUNNING_LIST_ARCHIVE_NEXT)
        .then()
            .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }
}
