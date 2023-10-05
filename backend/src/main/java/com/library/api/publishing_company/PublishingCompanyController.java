package com.library.api.publishing_company;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/publishing_company")
@AllArgsConstructor
@Tag(name = "PublishingCompany", description = "Endpoints to Managing the Publishing Company")
public class PublishingCompanyController {

    private final PublishingCompanyService publishingCompanyService;

    @PostMapping
    @Operation(
            summary = "Create Publishing Company",
            description = "Endpoint to Create an Publishing Company",
            tags = {"PublishingCompany"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            description = "BadRequest",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "InternalServerError",
                            responseCode = "500",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Object> createPublishingCompany(
            @RequestBody PublishingCompanyRequest requestBody
    ) {
        PublishingCompany publishingCompany = publishingCompanyService.createPublishingCompany(requestBody);
        PublishingCompanyResponse responseBody = PublishingCompanyResponse.builder()
                .id(publishingCompany.getId())
                .name(publishingCompany.getName())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}
