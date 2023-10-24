package com.library.api.integrationtests.author_book;

import com.github.javafaker.Faker;
import com.library.api.TestConfigs;
import com.library.api.author_book.AuthorBookRequest;
import com.library.api.author_book.AuthorBookResponse;
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
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorBookControllerTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static final int numberOfAuthorBookResponses = 2;
    public static AuthorBookResponse[] authorBookResponses;

    private static AuthorBookRequest authorBookRequest;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        authorBookResponses = new AuthorBookResponse[numberOfAuthorBookResponses];
    }

    @Test
    @Order(1)
    public void testCreatedBookAuthor() throws IOException {
        for (int index = 0; index < numberOfAuthorBookResponses; ++index) {
            authorBookRequest = createMockAuthorBookRequest(null);

            specification = new RequestSpecBuilder()
                    .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                    .setBasePath("/api/v1/author-book")
                    .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();

            Response response = given().spec(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(authorBookRequest)
                    .when()
                    .post()
                    .then()
                    .extract()
                    .response();

            Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());

            authorBookResponses[index] = objectMapper
                    .readValue(response.body().asString(), AuthorBookResponse.class);

            Assertions.assertNotNull(authorBookResponses[index].getKey());
            Assertions.assertNotNull(authorBookResponses[index].getName());
            Assertions.assertNotNull(authorBookResponses[index].getCreatedAt());
            Assertions.assertNotNull(authorBookResponses[index].getUpdatedAt());

            long seconds = 1000 * 30;
            Date dateLater = new Date(new Date().getTime() - seconds);

            Assertions.assertTrue(dateLater.before(authorBookResponses[index].getCreatedAt()));
            Assertions.assertTrue(dateLater.before(authorBookResponses[index].getUpdatedAt()));

            Assertions.assertEquals(authorBookRequest.name(), authorBookResponses[index].getName());
        }
    }

    @Test
    @Order(2)
    public void testCreatedBookAuthorWithWrongCors() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath("/api/v1/author-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(authorBookRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }

    @Test
    @Order(3)
    public void testCreatedBookAuthorAlreadyExistsAuthorBookName() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/author-book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(authorBookRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @Test
    @Order(4)
    public void testGetAllAuthorBooks() throws IOException {

        String[] paths = {
                "/api/v1/author-book",
                "/api/v1/author-book?page=0&size=1",
                String.format("/api/v1/author-book?name=%s", authorBookResponses[0].getName()),
                String.format("/api/v1/author-book?page=0&size=1&name=%s", authorBookResponses[0].getName()),
                String.format("/api/v1/author-book?page=0&size=1&name=%s", authorBookResponses[0].getName().substring(0, 3)),
        };

        for (String path : paths) {
            specification = new RequestSpecBuilder()
                    .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                    .setBasePath(path)
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

            AuthorBookResponse[] authorBookResponses = objectMapper
                    .readValue(response.body().asString(), AuthorBookResponse[].class);

            Assertions.assertEquals(numberOfAuthorBookResponses, authorBookResponses.length);

            Arrays.stream(authorBookResponses).forEach(authorBookResponse -> {
                Assertions.assertNotNull(authorBookResponse.getKey());
                Assertions.assertNotNull(authorBookResponse.getName());
                Assertions.assertNotNull(authorBookResponse.getCreatedAt());
                Assertions.assertNotNull(authorBookResponse.getUpdatedAt());

                long seconds = 1000 * 30;
                Date dateLater = new Date(new Date().getTime() - seconds);

                Assertions.assertTrue(dateLater.before(authorBookResponse.getCreatedAt()));
                Assertions.assertTrue(dateLater.before(authorBookResponse.getUpdatedAt()));
            });
        }
    }

    @Test
    @Order(5)
    public void testGetAllAuthorBooksWithWrongCors() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath("/api/v1/author-book")
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
    @Order(6)
    public void testGetAuthorBookById() throws IOException {
        AuthorBookResponse authorBookResponseParam = authorBookResponses[0];

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/author-book/%s", authorBookResponseParam.getKey()))
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

        AuthorBookResponse authorBookResponse = objectMapper
                .readValue(response.body().asString(), AuthorBookResponse.class);

        Assertions.assertNotNull(authorBookResponse.getKey());
        Assertions.assertNotNull(authorBookResponse.getName());
        Assertions.assertNotNull(authorBookResponse.getCreatedAt());
        Assertions.assertNotNull(authorBookResponse.getUpdatedAt());

        long seconds = 1000 * 30;
        Date dateLater = new Date(new Date().getTime() - seconds);

        Assertions.assertTrue(dateLater.before(authorBookResponse.getCreatedAt()));
        Assertions.assertTrue(dateLater.before(authorBookResponse.getUpdatedAt()));

        Assertions.assertEquals(authorBookResponseParam.getName(), authorBookResponse.getName());
    }

    @Test
    @Order(7)
    public void testGetAuthorBookByIdWithWrongCors() {
        AuthorBookResponse authorBookResponseParam = authorBookResponses[0];

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath(String.format("/api/v1/author-book/%s", authorBookResponseParam.getKey()))
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
    public void testGetAuthorBookByIdNotFound() {
        UUID randomId = UUID.randomUUID();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/author-book/%s", randomId))
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

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }

    @Test
    @Order(9)
    public void testUpdatePartialsAuthorBookById() {
        AuthorBookRequest newAuthorBookResponseParam = createMockAuthorBookRequest(null);
        AuthorBookResponse authorBookResponseToUpdate = authorBookResponses[0];

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/author-book/%s", authorBookResponseToUpdate.getKey()))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(newAuthorBookResponseParam)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test
    @Order(10)
    public void testUpdatePartialsAuthorBookByIdWithWrongCors() {
        AuthorBookRequest newAuthorBookResponseParam = createMockAuthorBookRequest(null);
        AuthorBookResponse authorBookResponseToUpdate = authorBookResponses[0];

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath(String.format("/api/v1/author-book/%s", authorBookResponseToUpdate.getKey()))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(newAuthorBookResponseParam)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }

    @Test
    @Order(11)
    public void testUpdatePartialsAuthorBookByIdNotFound() {
        AuthorBookRequest newAuthorBookResponseParam = createMockAuthorBookRequest(null);
        UUID randomId = UUID.randomUUID();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/author-book/%s", randomId))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(newAuthorBookResponseParam)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }


    @Test
    @Order(12)
    public void testUpdatePartialsAuthorBookByIdAlreadyExistsAuthorBookName() {
        AuthorBookRequest newAuthorBookResponseParam =
                createMockAuthorBookRequest(authorBookResponses[1].getName());
        AuthorBookResponse authorBookResponseToUpdate = authorBookResponses[0];

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/author-book/%s", authorBookResponseToUpdate.getKey()))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(newAuthorBookResponseParam)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }


    @Test
    @Order(13)
    public void testRemoveAuthorBookById() {
        AuthorBookResponse authorBookResponseToUpdate = authorBookResponses[0];

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/author-book/%s", authorBookResponseToUpdate.getKey()))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .when()
                .delete()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test
    @Order(14)
    public void testRemoveAuthorBookByIdWithWrongCors() {
        AuthorBookResponse authorBookResponseToUpdate = authorBookResponses[0];

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath(String.format("/api/v1/author-book/%s", authorBookResponseToUpdate.getKey()))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .when()
                .delete()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }

    @Test
    @Order(15)
    public void testRemoveAuthorBookByIdNotFound() {
        UUID randomId = UUID.randomUUID();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/author-book/%s", randomId))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .when()
                .delete()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }


    private AuthorBookRequest createMockAuthorBookRequest(String name) {
        if (name == null) {
            return new AuthorBookRequest(Faker.instance().book().author());
        }
        return new AuthorBookRequest(name);
    }
}
