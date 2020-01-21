package com.metalheart.test.integration.rest;

import com.metalheart.config.AppProperties;
import com.metalheart.EndPoint;
import com.metalheart.model.request.AuthenticationRequest;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.test.integration.BaseIntegrationTest;
import io.restassured.http.ContentType;
import java.net.HttpURLConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import static io.restassured.RestAssured.given;


@EnableAutoConfiguration
public class LoginIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AppProperties properties;

    @Test
    public void successLoginTest() {

        AuthenticationRequest request = AuthenticationRequest.builder()
            .username(properties.getSecurity().getDefaultUsername())
            .password(properties.getSecurity().getDefaultPassword())
            .build();

        String sessionId = given()
            .port(port)
            .contentType(ContentType.JSON).accept(ContentType.JSON)
            .body(request)
        .when()
            .post(EndPoint.AUTH_SIGN_IN)
        .then()
            .statusCode(HttpURLConnection.HTTP_OK)
            .extract().cookie("JSESSIONID");

        Assert.assertNotNull(sessionId);

        given()
            .cookie("JSESSIONID", sessionId)
            .port(port)
        .when()
            .get(EndPoint.RUNNING_LIST)
        .then()
            .statusCode(HttpURLConnection.HTTP_OK)
            .extract().body().as(RunningListViewModel.class);

    }

    @Test
    public void forbiddenLoginTest() {

        given()
            .port(port)
        .when()
            .get(EndPoint.RUNNING_LIST)
        .then()
            .statusCode(HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test
    public void failLoginTest() {
        AuthenticationRequest request = AuthenticationRequest.builder()
            .username(properties.getSecurity().getDefaultUsername())
            .password(RandomStringUtils.random(8))
            .build();

        given()
            .port(port)
            .contentType(ContentType.JSON).accept(ContentType.JSON)
            .body(request)
        .when()
            .post(EndPoint.AUTH_SIGN_IN)
        .then()
            .statusCode(HttpURLConnection.HTTP_FORBIDDEN);
    }
}
