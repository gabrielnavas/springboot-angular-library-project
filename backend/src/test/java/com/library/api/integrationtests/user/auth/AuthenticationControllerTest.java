package com.library.api.integrationtests.user.auth;

import com.github.javafaker.Faker;
import com.library.api.TestConfigs;
import com.library.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.library.api.user.auth.AuthenticationRequest;
import com.library.api.user.auth.AuthenticationResponse;
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
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationControllerTest extends AbstractIntegrationTest {


    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static AuthenticationRequest authenticationRequest;


    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    public void testRegisterUser() throws IOException {
        authenticationRequest = createAuthenticationRequest();

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
                .body(authenticationRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        AuthenticationResponse authenticationResponse = objectMapper.readValue(response.body().asString(), AuthenticationResponse.class);

        Assertions.assertDoesNotThrow(() -> {
            UUID.fromString(authenticationResponse.getId().toString());
        });

        Assertions.assertNotNull(authenticationResponse.getRole());
        Assertions.assertNotNull(authenticationResponse.getToken());
        Assertions.assertNotNull(authenticationResponse.getCreatedAt());
        Assertions.assertNotNull(authenticationResponse.getUpdatedAt());

        Assertions.assertEquals(authenticationRequest.getEmail(), authenticationResponse.getEmail());
        Assertions.assertEquals(authenticationRequest.getFirstName(), authenticationResponse.getFirstName());
        Assertions.assertEquals(authenticationRequest.getLastName(), authenticationResponse.getLastName());
        Assertions.assertEquals(authenticationRequest.getStreetName(), authenticationResponse.getStreetName());
        Assertions.assertEquals(authenticationRequest.getStreetNumber(), authenticationResponse.getStreetNumber());
    }

    private AuthenticationRequest createAuthenticationRequest() {
        Faker faker = Faker.instance();
        String password = faker.internet().password();
        return AuthenticationRequest.builder()
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
