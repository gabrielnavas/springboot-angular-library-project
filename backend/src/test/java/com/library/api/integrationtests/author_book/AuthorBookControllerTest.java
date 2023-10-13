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
import java.util.Date;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorBookControllerTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    public void testCreatedBookAuthor() throws IOException {
        AuthorBookRequest authorBookRequest = createMockAuthorBookRequest(null);

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

        Assertions.assertEquals(authorBookRequest.name(), authorBookResponse.getName());
    }

    private AuthorBookRequest createMockAuthorBookRequest(String name) {
        return new AuthorBookRequest(name == null ? Faker.instance().book().author() : name);
    }
}
