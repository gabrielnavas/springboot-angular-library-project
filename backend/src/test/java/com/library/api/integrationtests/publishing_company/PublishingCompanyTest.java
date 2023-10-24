package com.library.api.integrationtests.publishing_company;

import com.github.javafaker.Faker;
import com.library.api.TestConfigs;
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
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublishingCompanyTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static final int numberOfCreatePublishingCompanyResponse = 2;

    public static PublishingCompanyResponse[] publishingCompanyResponses;
    private static PublishingCompanyRequest publishingCompanyRequest;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        publishingCompanyRequest = null;
        publishingCompanyResponses = new PublishingCompanyResponse[numberOfCreatePublishingCompanyResponse];
    }

    @Test
    @Order(1)
    public void testCreatePublishingCompany() throws IOException {
        for (int index = 0; index < numberOfCreatePublishingCompanyResponse; ++index) {
            publishingCompanyRequest = createMockPublishingCompanyRequest(null);

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

            publishingCompanyResponses[index] = objectMapper
                    .readValue(response.body().asString(), PublishingCompanyResponse.class);

            Assertions.assertNotNull(publishingCompanyResponses[index].getKey());
            Assertions.assertNotNull(publishingCompanyResponses[index].getName());
            Assertions.assertNotNull(publishingCompanyResponses[index].getCreatedAt());
            Assertions.assertNotNull(publishingCompanyResponses[index].getUpdatedAt());

            long seconds = 1000 * 30;
            Date dateLater = new Date(new Date().getTime() - seconds);

            Assertions.assertTrue(dateLater.before(publishingCompanyResponses[index].getCreatedAt()));
            Assertions.assertTrue(dateLater.before(publishingCompanyResponses[index].getUpdatedAt()));

            Assertions.assertEquals(publishingCompanyResponses[index].getName(), publishingCompanyRequest.name());
        }
    }

    @Test
    @Order(2)
    public void testCreatePublishingCompanyWithWrongOrigin() {
        publishingCompanyRequest = createMockPublishingCompanyRequest(null);

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
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

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }

    @Test
    @Order(3)
    public void testGetAllPublishingCompany() throws IOException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/publishing-company")
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

        PublishingCompanyResponse[] publishingCompanyResponses = objectMapper
                .readValue(response.body().asString(), PublishingCompanyResponse[].class);

        Assertions.assertNotNull(publishingCompanyResponses);
        Assertions.assertEquals(numberOfCreatePublishingCompanyResponse, publishingCompanyResponses.length);

        for (var pc : publishingCompanyResponses) {
            Assertions.assertNotNull(pc.getKey());
            Assertions.assertNotNull(pc.getName());
            Assertions.assertTrue(pc.getName().length() > 0);
        }
    }

    @Test
    @Order(4)
    public void testGetAllPublishingCompanyByName() throws IOException {
        BiFunction<String, Integer, String> getSubString = (s, length) -> s.substring(0, 2);
        BiFunction<String, Integer, String> upperLower = (s, length) -> {
            StringBuilder sb = new StringBuilder();
            int index = 0;
            while (index < s.length() && index < length) {
                String chr = String.format("%s", s.charAt(index));
                if (length % 2 == 0) {
                    sb.append(chr.toLowerCase());
                } else {
                    sb.append(chr.toUpperCase());
                }
                index++;
            }
            return sb.toString();
        };

        int substringLength = 2;
        String url = String.format(
                "/api/v1/publishing-company?name=%s",
                upperLower.apply(
                        getSubString.apply(publishingCompanyResponses[0].getName(), substringLength),
                        substringLength
                )
        );
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

        PublishingCompanyResponse[] publishingCompanyResponses = objectMapper
                .readValue(response.body().asString(), PublishingCompanyResponse[].class);

        Assertions.assertNotNull(publishingCompanyResponses);
        Assertions.assertEquals(numberOfCreatePublishingCompanyResponse, publishingCompanyResponses.length);

        for (var pc : publishingCompanyResponses) {
            System.out.println(pc.getName());
            Assertions.assertNotNull(pc.getKey());
            Assertions.assertNotNull(pc.getName());
            Assertions.assertNotNull(pc.getCreatedAt());
            Assertions.assertNotNull(pc.getUpdatedAt());
            Assertions.assertTrue(pc.getName().length() > 0);
        }
    }

    @Test
    @Order(5)
    public void testGetAllPublishingCompanyWithWrongCors() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath("/api/v1/publishing-company")
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
    public void testUpdatePublishingCompany() {
        PublishingCompanyRequest publishingCompanyRequestUpdated = createMockPublishingCompanyRequest(null);

        String urlUpdate = String.format("/api/v1/publishing-company/%s", publishingCompanyResponses[0].getKey());

        RequestSpecification specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(urlUpdate)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(publishingCompanyRequestUpdated)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    @Test
    @Order(7)
    public void testUpdatePublishingCompanyWithWrongCors() {
        String urlUpdate = String.format("/api/v1/publishing-company/%s", createMockPublishingCompanyRequest(null));

        RequestSpecification specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_WRONG)
                .setBasePath(urlUpdate)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createMockPublishingCompanyRequest(null))
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.statusCode());
    }

    @Test
    @Order(8)
    public void testUpdatePublishingCompanyWithSameName() throws IOException {
        var publishingCompanySameName = createMockPublishingCompanyRequest(publishingCompanyResponses[1].getName());
        UUID differentId = publishingCompanyResponses[0].getKey();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/publishing-company/%s", differentId))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(publishingCompanySameName)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());

        Map<String, Object> jsonMap = objectMapper
                .readValue(response.body().asString(), new TypeReference<>() {
                });


        Assertions.assertEquals(String.format("publishing company already exists with attribute name with value %s", publishingCompanySameName.name()), jsonMap.get("message"));
    }

    @Test
    @Order(9)
    public void testUpdatePublishingCompanyReturningNotFound() throws IOException {
        var randomObject = createMockPublishingCompanyRequest(null);
        UUID fakeId = UUID.randomUUID();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(String.format("/api/v1/publishing-company/%s", fakeId))
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(randomObject)
                .when()
                .patch()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());

        Map<String, Object> jsonMap = objectMapper
                .readValue(response.body().asString(), new TypeReference<>() {
                });


        Assertions.assertEquals("publishing company not found", jsonMap.get("message"));
    }

    @Test
    @Order(10)
    public void testGetPublishingCompanyById() throws IOException {
        PublishingCompanyResponse publishingCompanyResponseInput = publishingCompanyResponses[0];
        String urlUpdate = String.format("/api/v1/publishing-company/%s", publishingCompanyResponseInput.getKey());

        RequestSpecification specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(urlUpdate)
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

        PublishingCompanyResponse publishingCompanyResponse = objectMapper
                .readValue(response.body().asString(), PublishingCompanyResponse.class);

        Assertions.assertNotNull(publishingCompanyResponse);

        Assertions.assertNotNull(publishingCompanyResponse.getKey());
        Assertions.assertNotNull(publishingCompanyResponse.getName());
        Assertions.assertNotNull(publishingCompanyResponse.getCreatedAt());
        Assertions.assertNotNull(publishingCompanyResponse.getUpdatedAt());
    }

    @Test
    @Order(11)
    public void testGetPublishingCompanyByIdNotFound() throws IOException {
        String urlUpdate = String.format("/api/v1/publishing-company/%s", UUID.randomUUID());

        RequestSpecification specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(urlUpdate)
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
    @Order(12)
    public void testRemovePublishingCompanyById() {
        String urlUpdate = String.format("/api/v1/publishing-company/%s", publishingCompanyResponses[0].getKey());

        RequestSpecification specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(urlUpdate)
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
    @Order(13)
    public void testRemovePublishingCompanyByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        String urlUpdate = String.format("/api/v1/publishing-company/%s", randomId);

        RequestSpecification specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath(urlUpdate)
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

    private PublishingCompanyRequest createMockPublishingCompanyRequest(String name) {
        return new PublishingCompanyRequest(name == null ? Faker.instance().book().publisher() : name);
    }
}
