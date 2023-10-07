package com.library.api.publishing_company;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = PublishingCompanyController.REQUEST_MAPPING_PATH)
@AllArgsConstructor
@Tag(name = "PublishingCompany", description = "Endpoints to Managing the Publishing Company")
public class PublishingCompanyController {

    public static final String REQUEST_MAPPING_PATH = "/api/v1/publishing-company";

    private final PublishingCompanyService publishingCompanyService;
    private final Logger logger = Logger.getLogger(PublishingCompanyController.class.getName());

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(
            summary = "Create Publishing Company",
            description = "Endpoint to Create an Publishing Company",
            tags = {"PublishingCompany"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = PublishingCompany.class))
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
            @RequestBody @Valid PublishingCompanyRequest request
    ) {
        logger.info(String.format("HTTP POST %s", PublishingCompanyController.REQUEST_MAPPING_PATH));
        PublishingCompanyResponse response = publishingCompanyService.createPublishingCompany(request);

        response.add(linkTo(methodOn(PublishingCompanyController.class)
                .createPublishingCompany(request)).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(
            summary = "Get all Publishing Companies",
            description = "Endpoint to Get all Publishing Companies",
            tags = {"PublishingCompany"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = PublishingCompany.class))
                            )
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
    public ResponseEntity<Object> getAllPublishingCompany(
            Pageable pageable,
            @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        logger.info(String.format("HTTP GET %s", PublishingCompanyController.REQUEST_MAPPING_PATH));

        List<PublishingCompanyResponse> publishingCompanyResponses = publishingCompanyService
                .getAllPublishingCompany(new HashMap<>() {{
                    put("name", name);
                }}, pageable);
            

        publishingCompanyResponses.forEach(publishingCompanyResponse -> {
            publishingCompanyResponse.add(linkTo(methodOn(PublishingCompanyController.class)
                    .createPublishingCompany(new PublishingCompanyRequest(""))).withRel("create new publishing company"));

            publishingCompanyResponse.add(linkTo(methodOn(PublishingCompanyController.class)
                    .getAllPublishingCompany(pageable, name)).withSelfRel());
        });

        return ResponseEntity.status(HttpStatus.OK).body(publishingCompanyResponses);
    }
}
