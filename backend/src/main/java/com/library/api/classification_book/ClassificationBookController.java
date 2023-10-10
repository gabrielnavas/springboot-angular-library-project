package com.library.api.classification_book;

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
    @GetMapping
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
    @GetMapping("{id}")
    public ResponseEntity<Object> getClassificationBookById(
            @PathVariable(value = "id") UUID id
    ) {
        ClassificationBookResponse classificationBooksResponse =
                this.classificationBookService.getClassificationBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(classificationBooksResponse);
    }
}
