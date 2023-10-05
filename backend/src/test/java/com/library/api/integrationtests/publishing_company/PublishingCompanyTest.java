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
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

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
    public void testCreatePublishingCompany() throws IOException {
        publishingCompanyRequest = createMockPublishingCompanyRequest();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.CORS_VALID)
                .setBasePath("/api/v1/publishing_company")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        Response response = given().spec(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(publishingCompanyRequest)
                .when()
                .post()
                .then()
                .extract()
                .response();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        publishingCompanyResponse = objectMapper.readValue(response.body().asString(), PublishingCompanyResponse.class);

        Assertions.assertNotNull(publishingCompanyResponse.getKey());
        Assertions.assertNotNull(publishingCompanyResponse.getName());

        Assertions.assertEquals(publishingCompanyResponse.getName(), publishingCompanyRequest.name());

    }

    private PublishingCompanyRequest createMockPublishingCompanyRequest() {
        return new PublishingCompanyRequest(Faker.instance().book().publisher());
    }

}
