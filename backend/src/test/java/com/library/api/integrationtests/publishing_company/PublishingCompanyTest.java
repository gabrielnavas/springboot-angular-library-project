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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.function.BiFunction;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PublishingCompanyTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PublishingCompanyResponse publishingCompanyResponse;
    private static PublishingCompanyRequest publishingCompanyRequest;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        publishingCompanyResponse = null;
        publishingCompanyRequest = null;
    }

    @Test
    @Order(1)
    public void testCreatePublishingCompany() throws IOException {
        publishingCompanyRequest = createMockPublishingCompanyRequest();

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

        publishingCompanyResponse = objectMapper
                .readValue(response.body().asString(), PublishingCompanyResponse.class);

        Assertions.assertNotNull(publishingCompanyResponse.getKey());
        Assertions.assertNotNull(publishingCompanyResponse.getName());

        Assertions.assertEquals(publishingCompanyResponse.getName(), publishingCompanyRequest.name());
    }

    @Test
    @Order(2)
    public void testCreatePublishingCompanyWithWrongOrigin() throws IOException {
        publishingCompanyRequest = createMockPublishingCompanyRequest();

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
        Assertions.assertEquals(1, publishingCompanyResponses.length);

        for (var pc : publishingCompanyResponses) {
            System.out.println(pc.getName());
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
                if (length % 2 == 0) {
                    sb.append((s.charAt(index) + "").toLowerCase());
                } else {
                    sb.append((s.charAt(index) + "").toUpperCase());
                }
                index++;
            }
            return sb.toString();
        };

        int substringLength = 2;
        String url = String.format(
                "/api/v1/publishing-company?name=%s",
                upperLower.apply(
                        getSubString.apply(publishingCompanyResponse.getName(), substringLength),
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

        PublishingCompanyResponse[] publishingCompanyResponses = objectMapper.readValue(response.body().asString(), PublishingCompanyResponse[].class);

        Assertions.assertNotNull(publishingCompanyResponses);
        Assertions.assertEquals(1, publishingCompanyResponses.length);

        for (var pc : publishingCompanyResponses) {
            System.out.println(pc.getName());
            Assertions.assertNotNull(pc.getKey());
            Assertions.assertNotNull(pc.getName());
            Assertions.assertTrue(pc.getName().length() > 0);
        }
    }

    @Test
    @Order(5)
    public void testGetAllPublishingCompanyWithWrongCors() throws IOException {
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


    private PublishingCompanyRequest createMockPublishingCompanyRequest() {
        return new PublishingCompanyRequest(Faker.instance().book().publisher());
    }
}
