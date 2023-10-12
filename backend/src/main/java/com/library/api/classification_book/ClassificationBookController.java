package com.library.api.classification_book;

import com.library.api.classification_book.hateoas.ClassificationBookHateoasWithRel;
import com.library.api.classification_book.hateoas.ClassificationBookMapperHateoas;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/classification-book")
@AllArgsConstructor
@Tag(name = "ClassificationBook", description = "Endpoints to Managing the Classification Book")
public class ClassificationBookController {

    private final ClassificationBookService classificationBookService;

    @Operation(
            summary = "Create Classification Book",
            description = "Endpoint to Create an Classification Book",
            tags = {"ClassificationBook"},
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = ClassificationBookResponse.class))
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
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> createClassificationBook(
            @RequestBody ClassificationBookRequest request
    ) {
        ClassificationBookResponse response = classificationBookService.createClassificationBook(request);
        ClassificationBookMapperHateoas.set(
                response,
                ClassificationBookHateoasWithRel.CREATE_CLASSIFICATION_BOOK
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get All Classification Book",
            description = "Endpoint to Get All an Classification Book",
            tags = {"ClassificationBook"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ClassificationBookResponse.class)
                                    )
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
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> getAllClassificationBooks(
            Pageable pageable,
            @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        List<ClassificationBookResponse> classificationBooksResponse =
                this.classificationBookService.getAllClassificationBooks(
                        new HashMap<>() {{
                            put("name", name);
                        }},
                        pageable);
        ClassificationBookMapperHateoas.set(
                classificationBooksResponse,
                pageable,
                ClassificationBookHateoasWithRel.GET_ALL_CLASSIFICATION_BOOKS
        );
        return ResponseEntity.status(HttpStatus.OK).body(classificationBooksResponse);
    }


    @Operation(
            summary = "Get Classification Book By Id",
            description = "Endpoint to Get Classification Book By Id",
            tags = {"ClassificationBook"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = ClassificationBookResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "BadRequest",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Forbidden",
                            responseCode = "403",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "BadRequest",
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
    @GetMapping(
            value = "{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> getClassificationBookById(
            @PathVariable(value = "id") UUID id
    ) {
        ClassificationBookResponse classificationBookResponse =
                this.classificationBookService.getClassificationBookById(id);
        ClassificationBookMapperHateoas.set(
                classificationBookResponse,
                ClassificationBookHateoasWithRel.GET_CLASSIFICATION_BOOK_BY_ID
        );
        return ResponseEntity.status(HttpStatus.OK).body(classificationBookResponse);
    }


    @Operation(
            summary = "Update Partials Classification Book By Id",
            description = "Endpoint to Update Partials Classification Book By Id",
            tags = {"ClassificationBook"},
            responses = {
                    @ApiResponse(
                            description = "NoContent",
                            responseCode = "204",
                            content = @Content()
                    ),
                    @ApiResponse(
                            description = "BadRequest",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Forbidden",
                            responseCode = "403",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "BadRequest",
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
    @PatchMapping(
            value = "{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> updatePartialsClassificationBookById(
            @PathVariable(value = "id") UUID id,
            @RequestBody ClassificationBookRequest request
    ) {
        classificationBookService.updatePartialsClassificationBookById(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Remove Classification Book By Id",
            description = "Endpoint to Remove Classification Book By Id",
            tags = {"ClassificationBook"},
            responses = {
                    @ApiResponse(
                            description = "NoContent",
                            responseCode = "204",
                            content = @Content()
                    ),
                    @ApiResponse(
                            description = "BadRequest",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Forbidden",
                            responseCode = "403",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "BadRequest",
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
    @DeleteMapping(
            value = "{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> removeClassificationBookById(
            @PathVariable(value = "id") UUID id
    ) {
        classificationBookService.removeClassificationBookById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
