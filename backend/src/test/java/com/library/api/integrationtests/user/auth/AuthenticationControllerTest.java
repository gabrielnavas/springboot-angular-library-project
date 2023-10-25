package com.library.api.integrationtests.user.auth;

import com.github.javafaker.Faker;
import com.library.api.TestConfigs;
import com.library.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.library.api.user.UserRequest;
import com.library.api.user.UserResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationControllerTest extends AbstractIntegrationTest {


    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static UserRequest userRequest;


    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    public void testRegisterUser() throws IOException {
        userRequest = createAuthenticationRequest();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/auth/register")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        UserResponse userResponse = objectMapper.readValue(response.body().asString(), UserResponse.class);

        Assertions.assertDoesNotThrow(() -> {
            UUID.fromString(userResponse.getId().toString());
        });

        Assertions.assertTrue(userResponse.getRole().toString().length() > 0);
        Assertions.assertTrue(userResponse.getToken().length() > 0);
        Assertions.assertEquals(3, userResponse.getToken().split("\\.").length);

        long seconds = 1000 * 30;
        Date dateLater = new Date(new Date().getTime() - seconds);

        Assertions.assertTrue(dateLater.before(userResponse.getCreatedAt()));
        Assertions.assertTrue(dateLater.before(userResponse.getUpdatedAt()));

        Assertions.assertEquals(userRequest.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(userRequest.getFirstName(), userResponse.getFirstName());
        Assertions.assertEquals(userRequest.getLastName(), userResponse.getLastName());
        Assertions.assertEquals(userRequest.getStreetName(), userResponse.getStreetName());
        Assertions.assertEquals(userRequest.getStreetNumber(), userResponse.getStreetNumber());
    }

    @Test
    @Order(2)
    public void testAuthenticateUser() throws IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/auth/authenticate")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(userRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        UserResponse userResponse = objectMapper.readValue(response.body().asString(), UserResponse.class);

        Assertions.assertDoesNotThrow(() -> {
            UUID.fromString(userResponse.getId().toString());
        });

        Assertions.assertTrue(userResponse.getRole().toString().length() > 0);
        Assertions.assertTrue(userResponse.getToken().length() > 0);
        Assertions.assertEquals(3, userResponse.getToken().split("\\.").length);

        long seconds = 1000 * 30;
        Date dateLater = new Date(new Date().getTime() - seconds);

        Assertions.assertTrue(dateLater.before(userResponse.getCreatedAt()));
        Assertions.assertTrue(dateLater.before(userResponse.getUpdatedAt()));

        Assertions.assertEquals(userRequest.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(userRequest.getFirstName(), userResponse.getFirstName());
        Assertions.assertEquals(userRequest.getLastName(), userResponse.getLastName());
        Assertions.assertEquals(userRequest.getStreetName(), userResponse.getStreetName());
        Assertions.assertEquals(userRequest.getStreetNumber(), userResponse.getStreetNumber());
    }

    private UserRequest createAuthenticationRequest() {
        Faker faker = Faker.instance();
        String password = faker.internet().password();
        return UserRequest.builder()
                .email(faker.internet().emailAddress())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .password(password)
                .passwordConfirmation(password)
                .streetName(faker.address().streetName())
                .streetNumber(faker.address().streetAddressNumber())
                .telephone(faker.phoneNumber().cellPhone())
                .build();
    }
}
