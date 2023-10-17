package com.library.api.integrationtests.book;

import com.github.javafaker.Faker;
import com.library.api.TestConfigs;
import com.library.api.author_book.AuthorBookRequest;
import com.library.api.author_book.AuthorBookResponse;
import com.library.api.book.controllers.BookRequest;
import com.library.api.book.controllers.BookResponse;
import com.library.api.classification_book.ClassificationBookRequest;
import com.library.api.classification_book.ClassificationBookResponse;
import com.library.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.library.api.publishing_company.PublishingCompanyRequest;
import com.library.api.publishing_company.PublishingCompanyResponse;
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
import java.util.*;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(4)
public class BookControllerTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static ClassificationBookResponse classificationBookResponse;

    private static AuthorBookResponse authorBookResponse;

    private static PublishingCompanyResponse publishingCompanyResponse;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(4)
    public void testCreatedBookAuthor() throws IOException {
        createClassificationBookResponse();
        createAuthorBookResponse();
        createPublishingCompanyResponse();

        BookRequest bookRequest = createBook(
                publishingCompanyResponse,
                classificationBookResponse,
                authorBookResponse
        );

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(bookRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        BookResponse bookResponse = objectMapper.readValue(response.body().asString(), BookResponse.class);

        // book
        Assertions.assertDoesNotThrow(() -> {
            UUID.fromString(bookResponse.getId().toString());
        });
        Assertions.assertEquals(bookRequest.getIsbn(), bookResponse.getIsbn());
        Assertions.assertEquals(bookRequest.getKeyWords(), bookResponse.getKeyWords());
        Assertions.assertEquals(bookRequest.getPublication(), bookResponse.getPublication());
        Assertions.assertEquals(bookRequest.getPages(), bookResponse.getPages());
        Date lastMinuteBeforeNow = new Date(new Date().getTime() - 1000 * 30);
        Assertions.assertTrue(lastMinuteBeforeNow.before(bookResponse.getCreatedAt()));
        Assertions.assertTrue(lastMinuteBeforeNow.before(bookResponse.getUpdatedAt()));

        // author
        Assertions.assertEquals(authorBookResponse.getKey(), bookResponse.getAuthorBookResponse().getKey());
        Assertions.assertEquals(authorBookResponse.getName(), bookResponse.getAuthorBookResponse().getName());
        Assertions.assertEquals(authorBookResponse.getCreatedAt(), bookResponse.getAuthorBookResponse().getCreatedAt());
        Assertions.assertEquals(authorBookResponse.getUpdatedAt(), bookResponse.getAuthorBookResponse().getUpdatedAt());

        // classification book
        Assertions.assertEquals(classificationBookResponse.getKey(), bookResponse.getClassificationBook().getKey());
        Assertions.assertEquals(classificationBookResponse.getName(), bookResponse.getClassificationBook().getName());
        Assertions.assertEquals(classificationBookResponse.getCreatedAt(), bookResponse.getClassificationBook().getCreatedAt());
        Assertions.assertEquals(classificationBookResponse.getUpdatedAt(), bookResponse.getClassificationBook().getUpdatedAt());


        // publishing company
        Assertions.assertEquals(publishingCompanyResponse.getKey(), bookResponse.getPublishingCompany().getKey());
        Assertions.assertEquals(publishingCompanyResponse.getName(), bookResponse.getPublishingCompany().getName());
        Assertions.assertEquals(publishingCompanyResponse.getCreatedAt(), bookResponse.getPublishingCompany().getCreatedAt());
        Assertions.assertEquals(publishingCompanyResponse.getCreatedAt(), bookResponse.getPublishingCompany().getUpdatedAt());
    }


    @Test
    @Order(5)
    public void testCreatedBookAuthorWithWrongCorsForbidden() throws IOException {
        BookRequest bookRequest = createBook(
                publishingCompanyResponse,
                classificationBookResponse,
                authorBookResponse
        );

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath("/api/v1/book")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(bookRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }

    private static BookRequest createBook(
            PublishingCompanyResponse publishingCompanyResponse,
            ClassificationBookResponse classificationBookResponse,
            AuthorBookResponse authorBookResponse
    ) {
        Faker faker = Faker.instance();

        List<String> keyWords = new ArrayList<>(7);
        for (int i = 0; i < faker.random().nextInt(3, 7); i++) {
            keyWords.add(faker.funnyName().name());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date twoMothAgora = calendar.getTime();

        return BookRequest.builder()
                .title(faker.book().title())
                .isbn(faker.random().hex(4))
                .pages(10)
                .publication(twoMothAgora)
                .keyWords(keyWords)
                .publishingCompanyId(publishingCompanyResponse.getKey())
                .classificationBookId(classificationBookResponse.getKey())
                .authorBookId(authorBookResponse.getKey())
                .build();
    }

    @Test
    @Order(1)
    public void createPublishingCompanyResponse() throws IOException {
        PublishingCompanyRequest publishingCompanyRequest = new PublishingCompanyRequest(Faker.instance().book().publisher());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/publishing-company")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(publishingCompanyRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        publishingCompanyResponse = objectMapper.readValue(response.body().asString(), PublishingCompanyResponse.class);
    }

    @Test
    @Order(2)
    public void createAuthorBookResponse() throws IOException {
        AuthorBookRequest authorBookRequest = new AuthorBookRequest(Faker.instance().book().author());

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

        authorBookResponse = objectMapper.readValue(response.body().asString(), AuthorBookResponse.class);
    }

    @Test
    @Order(3)
    public void createClassificationBookResponse() throws IOException {
        ClassificationBookRequest classificationBookRequest = new ClassificationBookRequest(Faker.instance().book().genre());

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

        classificationBookResponse = objectMapper.readValue(response.body().asString(), ClassificationBookResponse.class);
    }
}
