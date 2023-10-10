package com.library.api.integrationtests.publishing_company;

import com.github.javafaker.Faker;
import com.library.api.TestConfigs;
import com.library.api.classification_book.ClassificationBookRequest;
import com.library.api.classification_book.ClassificationBookResponse;
import com.library.api.integrationtests.testcontainers.AbstractIntegrationTest;
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
public class ClassificationBookControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static ClassificationBookRequest classificationBookRequest;
    private static ClassificationBookResponse classificationBookResponse;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        classificationBookRequest = null;
        classificationBookResponse = null;
    }

    @Test
    @Order(1)
    public void testCreateClassificationBook() throws IOException {
        classificationBookRequest = createMockClassificationBookRequest(null);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/classification-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        classificationBookResponse = objectMapper
                .readValue(response.body().asString(), ClassificationBookResponse.class);

        Assertions.assertNotNull(classificationBookResponse.getKey());
        Assertions.assertNotNull(classificationBookResponse.getName());

        Assertions.assertEquals(classificationBookRequest.name(), classificationBookResponse.getName());
    }

    @Test
    @Order(2)
    public void testCreateClassificationBookAlreadyExistsName() throws IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/classification-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }


    @Test
    @Order(3)
    public void testCreateClassificationBookWithWrongCors() {
        ClassificationBookRequest classificationBookRequest = createMockClassificationBookRequest(null);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath("/api/v1/classification-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }

    @Test
    @Order(4)
    public void testGetAllClassificationBooks() throws IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/classification-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        ClassificationBookResponse[] classificationBookResponses = objectMapper
                .readValue(response.body().asString(), ClassificationBookResponse[].class);

        Assertions.assertEquals(1, classificationBookResponses.length);

        Assertions.assertNotNull(classificationBookResponses[0].getKey());
        Assertions.assertNotNull(classificationBookResponses[0].getName());

        Assertions.assertEquals(classificationBookRequest.name(), classificationBookResponses[0].getName());
    }


    @Test
    @Order(5)
    public void testGetAllClassificationBooksWithName() throws IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/classification-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("name", classificationBookResponse.getName())
                .when()
                .get()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        ClassificationBookResponse[] classificationBookResponses = objectMapper
                .readValue(response.body().asString(), ClassificationBookResponse[].class);

        Assertions.assertEquals(1, classificationBookResponses.length);

        Assertions.assertNotNull(classificationBookResponses[0].getKey());
        Assertions.assertNotNull(classificationBookResponses[0].getName());

        Assertions.assertEquals(classificationBookRequest.name(), classificationBookResponses[0].getName());
    }


    @Test
    @Order(6)
    public void testGetAllClassificationBooksWithNameAndPageable() throws IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/classification-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("name", classificationBookResponse.getName())
                .queryParam("page", 0)
                .queryParam("page", 1)
                .when()
                .get()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        ClassificationBookResponse[] classificationBookResponses = objectMapper
                .readValue(response.body().asString(), ClassificationBookResponse[].class);

        Assertions.assertEquals(1, classificationBookResponses.length);

        Assertions.assertNotNull(classificationBookResponses[0].getKey());
        Assertions.assertNotNull(classificationBookResponses[0].getName());

        Assertions.assertEquals(classificationBookRequest.name(), classificationBookResponses[0].getName());
    }


    @Test
    @Order(7)
    public void testGetAllClassificationBooksWithWrongCors() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath("/api/v1/classification-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }


    @Test
    @Order(8)
    public void testGetClassificationBookById() throws IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/v1/classification-book/{id}", classificationBookResponse.getKey())
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        ClassificationBookResponse classificationBookResponse = objectMapper
                .readValue(response.body().asString(), ClassificationBookResponse.class);

        Assertions.assertNotNull(classificationBookResponse.getKey());
        Assertions.assertNotNull(classificationBookResponse.getName());

        Assertions.assertEquals(classificationBookRequest.name(), classificationBookResponse.getName());
    }


    @Test
    @Order(9)
    public void testGetClassificationBookByIdWithWrongCors() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/v1/classification-book/{id}", classificationBookResponse.getKey())
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }


    @Test
    @Order(10)
    public void testGetClassificationBookByIdNotFound() {
        UUID fakeId = UUID.randomUUID();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .get("/api/v1/classification-book/{id}", fakeId)
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    private ClassificationBookRequest createMockClassificationBookRequest(String name) {
        return new ClassificationBookRequest(name == null ? Faker.instance().book().publisher() : name);
    }
}
