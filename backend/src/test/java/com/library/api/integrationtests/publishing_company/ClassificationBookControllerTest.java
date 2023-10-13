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
import java.util.Date;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassificationBookControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static ClassificationBookRequest classificationBookRequest;
    private static ClassificationBookResponse[] classificationBookResponses;

    private static final int numberOfClassificationsBooksResponses = 2;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        classificationBookRequest = null;
        classificationBookResponses = new ClassificationBookResponse[numberOfClassificationsBooksResponses];
    }

    @Test
    @Order(1)
    public void testCreateClassificationBook() throws IOException {
        for (int index = 0; index < numberOfClassificationsBooksResponses; index++) {
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

            classificationBookResponses[index] = objectMapper
                    .readValue(response.body().asString(), ClassificationBookResponse.class);

            Assertions.assertNotNull(classificationBookResponses[index].getKey());
            Assertions.assertNotNull(classificationBookResponses[index].getName());
            Assertions.assertNotNull(classificationBookResponses[index].getCreatedAt());
            Assertions.assertNotNull(classificationBookResponses[index].getUpdatedAt());

            long seconds = 1000 * 30;
            Date dateLater = new Date(new Date().getTime() - seconds);

            Assertions.assertTrue(dateLater.before(classificationBookResponses[index].getCreatedAt()));
            Assertions.assertTrue(dateLater.before(classificationBookResponses[index].getUpdatedAt()));

            Assertions.assertEquals(classificationBookRequest.name(), classificationBookResponses[index].getName());
        }
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

        Assertions.assertEquals(numberOfClassificationsBooksResponses, classificationBookResponses.length);

        Assertions.assertNotNull(classificationBookResponses[0].getKey());
        Assertions.assertNotNull(classificationBookResponses[0].getName());
        Assertions.assertNotNull(classificationBookResponses[0].getCreatedAt());
        Assertions.assertNotNull(classificationBookResponses[0].getUpdatedAt());

        Assertions.assertEquals(classificationBookResponses[0].getName(), classificationBookResponses[0].getName());
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
                .queryParam("name", classificationBookResponses[0].getName())
                .when()
                .get()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        ClassificationBookResponse[] allClassificationBookResponses = objectMapper
                .readValue(response.body().asString(), ClassificationBookResponse[].class);

        Assertions.assertEquals(1, allClassificationBookResponses.length);

        Assertions.assertNotNull(classificationBookResponses[0].getKey());
        Assertions.assertNotNull(classificationBookResponses[0].getName());
        Assertions.assertNotNull(classificationBookResponses[0].getCreatedAt());
        Assertions.assertNotNull(classificationBookResponses[0].getUpdatedAt());

        Assertions.assertEquals(classificationBookResponses[0].getName(), allClassificationBookResponses[0].getName());
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
                .queryParam("name", classificationBookResponses[0].getName())
                .queryParam("page", 0)
                .queryParam("page", 1)
                .when()
                .get()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        ClassificationBookResponse[] allClassificationBookResponses = objectMapper
                .readValue(response.body().asString(), ClassificationBookResponse[].class);

        Assertions.assertEquals(1, allClassificationBookResponses.length);

        Assertions.assertNotNull(allClassificationBookResponses[0].getKey());
        Assertions.assertNotNull(allClassificationBookResponses[0].getName());

        Assertions.assertEquals(classificationBookResponses[0].getName(), allClassificationBookResponses[0].getName());
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
                .get("/api/v1/classification-book/{id}", classificationBookResponses[0].getKey())
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());

        ClassificationBookResponse classificationBookResponse = objectMapper
                .readValue(response.body().asString(), ClassificationBookResponse.class);

        Assertions.assertNotNull(classificationBookResponse.getKey());
        Assertions.assertNotNull(classificationBookResponse.getName());
        Assertions.assertNotNull(classificationBookResponse.getCreatedAt());
        Assertions.assertNotNull(classificationBookResponse.getUpdatedAt());

        Assertions.assertEquals(classificationBookResponses[0].getName(), classificationBookResponse.getName());
        Assertions.assertTrue(new Date().after(classificationBookResponse.getCreatedAt()));
        Assertions.assertTrue(new Date().after(classificationBookResponse.getUpdatedAt()));
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
                .get("/api/v1/classification-book/{id}", classificationBookResponses[0].getKey())
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

    @Test
    @Order(11)
    public void testUpdatePartialsClassificationBookById() {
        ClassificationBookRequest classificationBookRequest =
                createMockClassificationBookRequest(null);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .patch(
                        "/api/v1/classification-book/{id}",
                        classificationBookResponses[0].getKey()
                )
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test
    @Order(12)
    public void testUpdatePartialsClassificationBookByIdWithWrongCors() {
        ClassificationBookRequest classificationBookRequest =
                createMockClassificationBookRequest(null);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .patch(
                        "/api/v1/classification-book/{id}",
                        classificationBookResponses[0].getKey()
                )
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }


    @Test
    @Order(13)
    public void testUpdatePartialsClassificationBookByIdBadRequestAlreadyExistsWithName() {
        ClassificationBookRequest classificationBookRequest =
                createMockClassificationBookRequest(classificationBookResponses[1].getName());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .patch(
                        "/api/v1/classification-book/{id}",
                        classificationBookResponses[0].getKey()
                )
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }


    @Test
    @Order(14)
    public void testUpdatePartialsClassificationBookByIdNotFound() {
        UUID randomId = UUID.randomUUID();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .patch("/api/v1/classification-book/{id}", randomId)
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @Test
    @Order(15)
    public void testRemoveClassificationBookById() {
        UUID id = classificationBookResponses[0].getKey();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .delete("/api/v1/classification-book/{id}", id)
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test
    @Order(16)
    public void testRemoveClassificationBookByIdWithWrongCors() {
        UUID id = classificationBookResponses[0].getKey();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .delete("/api/v1/classification-book/{id}", id)
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }

    @Test
    @Order(17)
    public void testRemoveClassificationBookByIdNotFound() {
        UUID randomId = UUID.randomUUID();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(classificationBookRequest)
                .when()
                .delete("/api/v1/classification-book/{id}", randomId)
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    private ClassificationBookRequest createMockClassificationBookRequest(String name) {
        return new ClassificationBookRequest(name == null ? Faker.instance().book().publisher() : name);
    }
}
