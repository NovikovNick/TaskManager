package com.metalheart.test.integration.runninglist;

import com.metalheart.EndPoint;
import com.metalheart.model.rest.response.RunningListViewModel;
import com.metalheart.test.integration.BaseIntegrationTest;
import java.net.HttpURLConnection;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;


@EnableAutoConfiguration
public class RestApiIntegrationTest extends BaseIntegrationTest{

    @LocalServerPort
    private int port;

    @Test
    public void simpleTest() {

        given()
            .port(port)
        .when()
            .get(EndPoint.RUNNING_LIST)
        .then()
            .statusCode(HttpURLConnection.HTTP_OK)
            .extract().body().as(RunningListViewModel.class);
    }
}
