package com.metalheart.test.integration.rest;

import com.metalheart.EndPoint;
import com.metalheart.model.User;
import com.metalheart.model.request.AuthenticationRequest;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.service.UserService;
import com.metalheart.test.integration.BaseIntegrationTest;
import io.restassured.http.ContentType;
import java.net.HttpURLConnection;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import static com.metalheart.HTTPConstants.HEADER_TIMEZONE_OFFSET;
import static io.restassured.RestAssured.given;


@EnableAutoConfiguration
public class LoginIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Test
    public void successLoginTest() {

        User user = createUser("user1");
        userService.createUser(user);

        AuthenticationRequest request = AuthenticationRequest.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .build();

        String sessionId = given()
            .header(HEADER_TIMEZONE_OFFSET, 0)
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
            .header(HEADER_TIMEZONE_OFFSET, 0)
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
            .header(HEADER_TIMEZONE_OFFSET, 0)
        .when()
            .get(EndPoint.RUNNING_LIST)
        .then()
            .statusCode(HttpURLConnection.HTTP_FORBIDDEN);
    }

    @Test
    public void failLoginTest() {


        User user = createUser("user1");
        userService.createUser(user);

        AuthenticationRequest request = AuthenticationRequest.builder()
            .username(user.getUsername())
            .password(user.getPassword() + RandomStringUtils.random(1))
            .build();

        given()
            .header(HEADER_TIMEZONE_OFFSET, 0)
            .port(port)
            .contentType(ContentType.JSON).accept(ContentType.JSON)
            .body(request)
        .when()
            .post(EndPoint.AUTH_SIGN_IN)
        .then()
            .statusCode(HttpURLConnection.HTTP_FORBIDDEN);
    }
}
