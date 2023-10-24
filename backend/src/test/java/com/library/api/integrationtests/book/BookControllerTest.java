package com.library.api.integrationtests.book;

import com.github.javafaker.Faker;
import com.library.api.TestConfigs;
import com.library.api.author_book.AuthorBookRequest;
import com.library.api.author_book.AuthorBookResponse;
import com.library.api.book.BookRequest;
import com.library.api.book.BookResponse;
import com.library.api.classification_book.ClassificationBookRequest;
import com.library.api.classification_book.ClassificationBookResponse;
import com.library.api.integrationtests.testcontainers.AbstractIntegrationTest;
import com.library.api.publishing_company.PublishingCompanyRequest;
import com.library.api.publishing_company.PublishingCompanyResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
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

    private static BookRequest bookRequest;

    private static ClassificationBookResponse classificationBookResponse;

    private static AuthorBookResponse authorBookResponse;

    private static PublishingCompanyResponse publishingCompanyResponse;

    private static BookResponse[] bookResponses;

    private static BookRequest newBookRequest;

    private static final int numberOfBookResponses = 2;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        bookResponses = new BookResponse[numberOfBookResponses];
    }


    /**
     * create a Publishing Company, needed to create a book
     *
     * @throws IOException
     */
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

    /**
     * create an Author Book, needed to create a book
     *
     * @throws IOException
     */
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

    /**
     * create a ClassificationBook, needed to create a book
     *
     * @throws IOException
     */
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

    @Test
    @Order(4)
    public void testCreatedBook() throws IOException {

        for (int i = 0; i < numberOfBookResponses; i++) {
            createClassificationBookResponse();
            createAuthorBookResponse();
            createPublishingCompanyResponse();

            bookRequest = createBook(
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

            bookResponses[i] = objectMapper.readValue(response.body().asString(), BookResponse.class);

            // book
            String id = bookResponses[i].getKey().toString();
            Assertions.assertDoesNotThrow(() -> {
                UUID.fromString(id);
            });
            Assertions.assertEquals(bookRequest.getIsbn(), bookResponses[i].getIsbn());
            Assertions.assertEquals(bookRequest.getKeyWords(), bookResponses[i].getKeyWords());
            Assertions.assertEquals(bookRequest.getPublication(), bookResponses[i].getPublication());
            Assertions.assertEquals(bookRequest.getPages(), bookResponses[i].getPages());
            Date lastMinuteBeforeNow = new Date(new Date().getTime() - 1000 * 30);
            Assertions.assertTrue(lastMinuteBeforeNow.before(bookResponses[i].getCreatedAt()));
            Assertions.assertTrue(lastMinuteBeforeNow.before(bookResponses[i].getUpdatedAt()));

            // author
            Assertions.assertEquals(authorBookResponse.getKey(), bookResponses[i].getAuthorBookResponse().getKey());
            Assertions.assertEquals(authorBookResponse.getName(), bookResponses[i].getAuthorBookResponse().getName());
            Assertions.assertEquals(authorBookResponse.getCreatedAt(), bookResponses[i].getAuthorBookResponse().getCreatedAt());
            Assertions.assertEquals(authorBookResponse.getUpdatedAt(), bookResponses[i].getAuthorBookResponse().getUpdatedAt());

            // classification book
            Assertions.assertEquals(classificationBookResponse.getKey(), bookResponses[i].getClassificationBook().getKey());
            Assertions.assertEquals(classificationBookResponse.getName(), bookResponses[i].getClassificationBook().getName());
            Assertions.assertEquals(classificationBookResponse.getCreatedAt(), bookResponses[i].getClassificationBook().getCreatedAt());
            Assertions.assertEquals(classificationBookResponse.getUpdatedAt(), bookResponses[i].getClassificationBook().getUpdatedAt());


            // publishing company
            Assertions.assertEquals(publishingCompanyResponse.getKey(), bookResponses[i].getPublishingCompany().getKey());
            Assertions.assertEquals(publishingCompanyResponse.getName(), bookResponses[i].getPublishingCompany().getName());
            Assertions.assertEquals(publishingCompanyResponse.getCreatedAt(), bookResponses[i].getPublishingCompany().getCreatedAt());
            Assertions.assertEquals(publishingCompanyResponse.getCreatedAt(), bookResponses[i].getPublishingCompany().getUpdatedAt());
        }
    }


    @Test
    @Order(5)
    public void testCreatedBookWithWrongCorsForbidden() {
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

    @Test
    @Order(6)
    public void testGetAllBooks() throws IOException {

        String[] paths = {
                "/api/v1/book",
                String.format("/api/v1/book?title=%s", bookRequest.getTitle().substring(1, 3)),
                String.format("/api/v1/book?isbn=%s", bookRequest.getIsbn().charAt(1)),
                String.format("/api/v1/book?title=%s&isbn=%s",
                        bookRequest.getTitle().substring(1, 3), bookRequest.getIsbn().charAt(1)),
                String.format("/api/v1/book?page=0&size=1&sort=title,ASC&title=%s&isbn=%s",
                        bookRequest.getTitle().substring(1, 3), bookRequest.getIsbn().charAt(1))
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

            BookResponse[] books = objectMapper.readValue(response.body().asString(), BookResponse[].class);

            Assertions.assertEquals(numberOfBookResponses, bookResponses.length);

            for (int i = 0; i < numberOfBookResponses; i++) {
                // book
                String id = bookResponses[i].getKey().toString();
                Assertions.assertDoesNotThrow(() -> {
                    UUID.fromString(id);
                });
                Assertions.assertEquals(bookResponses[i].getIsbn(), books[i].getIsbn());
                Assertions.assertEquals(bookResponses[i].getKeyWords(), books[i].getKeyWords());

                // verify book.publication
                Calendar bookRequestCalendar = Calendar.getInstance();
                Calendar bookResponseCalendar = Calendar.getInstance();
                bookRequestCalendar.setTime(books[i].getPublication());
                bookResponseCalendar.setTime(books[i].getPublication());
                boolean sameDay = bookRequestCalendar.get(Calendar.YEAR) == bookResponseCalendar.get(Calendar.YEAR) &&
                        bookRequestCalendar.get(Calendar.DAY_OF_YEAR) == bookResponseCalendar.get(Calendar.DAY_OF_YEAR);
                Assertions.assertTrue(sameDay);

                Assertions.assertEquals(bookResponses[i].getPages(), books[i].getPages());
                Date lastMinuteBeforeNow = new Date(new Date().getTime() - 1000 * 30);
                Assertions.assertTrue(lastMinuteBeforeNow.before(books[i].getCreatedAt()));
                Assertions.assertTrue(lastMinuteBeforeNow.before(books[i].getUpdatedAt()));
            }
        }
    }

    @Test
    @Order(7)
    public void testGetAllBooksWithWrongCors() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath("/api/v1/book")
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
    public void testGetBookById() throws IOException {
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
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

        BookResponse bookResponse = objectMapper.readValue(response.body().asString(), BookResponse.class);

        // book
        Assertions.assertDoesNotThrow(() -> {
            UUID.fromString(bookResponse.getKey().toString());
        });
        Assertions.assertEquals(bookResponses[0].getIsbn(), bookResponse.getIsbn());
        Assertions.assertEquals(bookResponses[0].getKeyWords(), bookResponse.getKeyWords());

        // verify book.publication
        Calendar bookRequestCalendar = Calendar.getInstance();
        Calendar bookResponseCalendar = Calendar.getInstance();
        bookRequestCalendar.setTime(bookResponses[0].getPublication());
        bookResponseCalendar.setTime(bookResponse.getPublication());
        boolean sameDay = bookRequestCalendar.get(Calendar.YEAR) == bookResponseCalendar.get(Calendar.YEAR) &&
                bookRequestCalendar.get(Calendar.DAY_OF_YEAR) == bookResponseCalendar.get(Calendar.DAY_OF_YEAR);
        Assertions.assertTrue(sameDay);

        Assertions.assertEquals(bookResponses[0].getPages(), bookResponse.getPages());
        Date lastMinuteBeforeNow = new Date(new Date().getTime() - 1000 * 30);
        Assertions.assertTrue(lastMinuteBeforeNow.before(bookResponse.getCreatedAt()));
        Assertions.assertTrue(lastMinuteBeforeNow.before(bookResponse.getUpdatedAt()));
    }

    @Test
    @Order(9)
    public void testGetBookByIdWithWrongCors() {
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath(url)
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
    @Order(10)
    public void testGetBookByIdNotFound() throws IOException {
        UUID randomId = UUID.randomUUID();
        String url = String.format("/api/v1/book/%s", randomId);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
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

        Map<String, Object> body = objectMapper.readValue(response.body().asString(), Map.class);
        Assertions.assertEquals("book not found", body.get("message"));
    }

    @Test
    @Order(11)
    public void testUpdatePartialsBookById() {
        newBookRequest = createBook(
                publishingCompanyResponse,
                classificationBookResponse,
                authorBookResponse
        );
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(newBookRequest)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }


    @Test
    @Order(12)
    public void testUpdatePartialsBookByIdWithSameTitle() {
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(newBookRequest)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }


    @Test
    @Order(13)
    public void testUpdatePartialsBookByIdWithWrongCors() {
        BookRequest newBookRequest = createBook(
                publishingCompanyResponse,
                classificationBookResponse,
                authorBookResponse
        );
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath(url)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(ContentType.JSON)
                .body(newBookRequest)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }


    @Test
    @Order(14)
    public void testUpdatePartialsBookByIdWithNotFoundPublishingCompany() throws IOException {
        UUID publishingCompanyResponseOriginalId = publishingCompanyResponse.getKey();
        publishingCompanyResponse.setKey(UUID.randomUUID());

        BookRequest newBookRequest = createBook(
                publishingCompanyResponse,
                classificationBookResponse,
                authorBookResponse
        );
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(ContentType.JSON)
                .body(newBookRequest)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());

        Map<String, Object> body = objectMapper.readValue(response.body().asString(), Map.class);
        Assertions.assertEquals("publishing company not found", body.get("message"));

        publishingCompanyResponse.setKey(publishingCompanyResponseOriginalId);
    }

    @Test
    @Order(15)
    public void testUpdatePartialsBookByIdWithNotFoundClassificationBookResponse() throws IOException {
        UUID classificationBookResponseOriginalId = classificationBookResponse.getKey();
        classificationBookResponse.setKey(UUID.randomUUID());

        BookRequest newBookRequest = createBook(
                publishingCompanyResponse,
                classificationBookResponse,
                authorBookResponse
        );
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(ContentType.JSON)
                .body(newBookRequest)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());

        Map<String, Object> body = objectMapper.readValue(response.body().asString(), Map.class);
        Assertions.assertEquals("classification book not found", body.get("message"));

        classificationBookResponse.setKey(classificationBookResponseOriginalId);
    }

    @Test
    @Order(16)
    public void testUpdatePartialsBookByIdWithNotFoundAuthorBookResponse() throws IOException {
        UUID authorBookResponseResponseOriginalId = authorBookResponse.getKey();
        authorBookResponse.setKey(UUID.randomUUID());

        BookRequest newBookRequest = createBook(
                publishingCompanyResponse,
                classificationBookResponse,
                authorBookResponse
        );
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(ContentType.JSON)
                .body(newBookRequest)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());

        Map<String, Object> body = objectMapper.readValue(response.body().asString(), Map.class);
        Assertions.assertEquals("author book not found", body.get("message"));

        authorBookResponse.setKey(authorBookResponseResponseOriginalId);
    }

    @Test
    @Order(17)
    public void testUpdatePartialsBookByIdAlreadyExistsWithTitle() throws IOException {
        BookRequest newBookRequestWithTitleAlreadyExists = createBook(
                publishingCompanyResponse,
                classificationBookResponse,
                authorBookResponse
        );
        newBookRequestWithTitleAlreadyExists.setTitle(bookResponses[1].getTitle());

        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(ContentType.JSON)
                .body(newBookRequestWithTitleAlreadyExists)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());

        Map<String, Object> body = objectMapper.readValue(response.body().asString(), Map.class);
        String msgError = String.format("book already exists with attribute title with value %s", newBookRequestWithTitleAlreadyExists.getTitle());
        Assertions.assertEquals(msgError, body.get("message"));
    }

    @Test
    @Order(18)
    public void testRemoveBookById() {
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
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
    @Order(19)
    public void testRemoveBookByIdWithWrongCors() {
        String url = String.format("/api/v1/book/%s", bookResponses[0].getKey());

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath(url)
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
    @Order(20)
    public void testRemoveBookByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        String url = String.format("/api/v1/book/%s", randomId);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(url)
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
}
