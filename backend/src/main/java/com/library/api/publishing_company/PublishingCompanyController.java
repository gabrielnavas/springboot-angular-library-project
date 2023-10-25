package com.library.api.publishing_company;

import com.library.api.publishing_company.hateoas.PublishingCompanyHateoasWithRel;
import com.library.api.publishing_company.hateoas.PublishingCompanyMapperHateoas;
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
import java.util.UUID;
import java.util.logging.Logger;

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
                            content = @Content(schema = @Schema(implementation = PublishingCompanyResponse.class))
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

        PublishingCompanyMapperHateoas.set(response, PublishingCompanyHateoasWithRel.CREATE_PUBLISHING_COMPANY);

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
                                    array = @ArraySchema(schema = @Schema(implementation = PublishingCompanyResponse.class))
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
        logger.info(
                String.format(
                        "HTTP GET %s - page number %d - page size %d - sort %s",
                        PublishingCompanyController.REQUEST_MAPPING_PATH,
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort()
                )
        );

        List<PublishingCompanyResponse> publishingCompanyResponses = publishingCompanyService
                .getAllPublishingCompany(new HashMap<>() {{
                    put("name", name);
                }}, pageable);

        PublishingCompanyMapperHateoas.set(
                publishingCompanyResponses,
                pageable,
                PublishingCompanyHateoasWithRel.GET_ALL_PUBLISHING_COMPANIES
        );

        return ResponseEntity.status(HttpStatus.OK).body(publishingCompanyResponses);
    }

    @PatchMapping("{publishingCompanyId}")
    @Operation(
            summary = "Patch a Publishing Companies",
            description = "Endpoint to Patch a Publishing Companies",
            tags = {"PublishingCompany"},
            responses = {
                    @ApiResponse(
                            description = "NoContent",
                            responseCode = "204",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "BadRequest",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "NotFound",
                            responseCode = "404",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "InternalServerError",
                            responseCode = "500",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<?> updatePublishingCompany(
            @PathVariable("publishingCompanyId") UUID publishingCompanyId,
            @RequestBody PublishingCompanyRequest PublishingCompanyRequest
    ) {
        logger.info(
                String.format(
                        "HTTP PATCH %s/%s",
                        PublishingCompanyController.REQUEST_MAPPING_PATH,
                        publishingCompanyId
                )
        );

        publishingCompanyService.updatePublishingCompany(publishingCompanyId, PublishingCompanyRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(
            value = "{publishingCompanyId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(
            summary = "Get Publishing Companies By Id",
            description = "Endpoint to Get Publishing Companies By Id",
            tags = {"PublishingCompany"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = PublishingCompanyResponse.class))
                            )
                    ),

                    @ApiResponse(
                            description = "NotFound",
                            responseCode = "404",
                            content = @Content
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
    public ResponseEntity<Object> getPublishingCompanyById(
            @PathVariable("publishingCompanyId") UUID publishingCompanyId
    ) {
        logger.info(
                String.format(
                        "HTTP GET %s/%s",
                        PublishingCompanyController.REQUEST_MAPPING_PATH,
                        publishingCompanyId
                )
        );

        PublishingCompanyResponse publishingCompanyResponse = publishingCompanyService
                .getPublishingCompanyById(publishingCompanyId);

        PublishingCompanyMapperHateoas.set(
                publishingCompanyResponse,
                PublishingCompanyHateoasWithRel.GET_PUBLISHING_COMPANY_BY_ID
        );

        return ResponseEntity.status(HttpStatus.OK).body(publishingCompanyResponse);
    }


    @DeleteMapping(
            value = "{publishingCompanyId}"
    )
    @Operation(
            summary = "Remove Publishing Companies By Id",
            description = "Endpoint to Remove Publishing Companies By Id",
            tags = {"PublishingCompany"},
            responses = {
                    @ApiResponse(
                            description = "NoContent",
                            responseCode = "204",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "NotFound",
                            responseCode = "404",
                            content = @Content
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
    public ResponseEntity<Object> removePublishingCompanyById(
            @PathVariable("publishingCompanyId") UUID publishingCompanyId
    ) {
        logger.info(
                String.format(
                        "HTTP DELETE %s/%s",
                        PublishingCompanyController.REQUEST_MAPPING_PATH,
                        publishingCompanyId
                )
        );

        publishingCompanyService
                .removePublishingCompanyById(publishingCompanyId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
