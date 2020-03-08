package com.metalheart.test.integration.rest;

import com.metalheart.EndPoint;
import com.metalheart.model.request.AuthenticationRequest;
import com.metalheart.model.request.UserRegistrationRequest;
import com.metalheart.model.response.RunningListViewModel;
import com.metalheart.test.integration.BaseIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.commons.mail.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import static com.metalheart.HTTPConstants.HEADER_TIMEZONE_OFFSET;
import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static javax.mail.Message.RecipientType.TO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class RegistrationIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private JavaMailSenderImpl sender;

    @Test
    public void registrationTest() throws Exception {

        // arrange
        Properties props = System.getProperties();
        props.put("mail.host", "smtp.dummydomain.com");
        props.put("mail.transport.protocol", "smtp");
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        when(sender.createMimeMessage()).thenReturn(email);
        when(sender.getSession()).thenReturn(session);

        UserRegistrationRequest request = getRegistrationRequest();

        // act send registration request
        given()
            .port(port)
            .contentType(ContentType.JSON).accept(ContentType.JSON)
            .body(request)
        .when()
            .post(EndPoint.USER_REGISTRATION)
        .then()
            .statusCode(HttpURLConnection.HTTP_OK);


        // assert received email with confirmation link
        assertNotNull(email.getRecipients(TO));
        assertEquals(request.getEmail(), email.getRecipients(TO)[0].toString());

        String confirmationLink = getConfirmationLink(email);

        // act confirm registration
        given()
            .config(RestAssured.config().redirect(redirectConfig().followRedirects(false)))
            .port(port)
        .when()
            .get(confirmationLink)
        .then().
            statusCode(HttpURLConnection.HTTP_MOVED_TEMP);


        // try to login
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
            .username(request.getUsername())
            .password(request.getPassword())
            .build();

        String sessionId = given()
            .header(HEADER_TIMEZONE_OFFSET, 0)
            .port(port)
            .contentType(ContentType.JSON).accept(ContentType.JSON)
            .body(authRequest)
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


    private UserRegistrationRequest getRegistrationRequest() {

        String username = "nick";
        String email = "nicknovikov10@gmail.com";
        String password = RandomStringUtils.random(8);

        return UserRegistrationRequest.builder()
            .username(username)
            .email(email)
            .password(password)
            .confirmPassword(password)
            .build();
    }

    private String getConfirmationLink(MimeMessage mimeMessage) throws Exception {

        MimeMessageParser parser = new MimeMessageParser(mimeMessage);
        parser.parse();
        String html = parser.getHtmlContent();

        assertNotNull("Confirmation email content", html);

        Document doc = Jsoup.parse(html);

        Element elem = doc.select("a#complite_link").get(0);
        String href = elem.attr("href");
        URL url = new URL(href);
        return url.getPath();
    }
}
