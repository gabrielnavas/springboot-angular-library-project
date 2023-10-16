package com.library.api.author_book;

import com.library.api.author_book.hateoas.AuthorBookHateoasWithRel;
import com.library.api.author_book.hateoas.AuthorBookMapperHateoas;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = AuthorBookController.REQUEST_MAPPING_PATH)
@AllArgsConstructor
@Tag(name = "AuthorBook", description = "Endpoints to Managing the Author Book")
public class AuthorBookController {

    private final AuthorBookService authorBookService;

    private final Logger logger = Logger.getLogger(AuthorBookController.class.getName());

    public static final String REQUEST_MAPPING_PATH = "/api/v1/author-book";

    @Operation(
            summary = "Create Author Book",
            description = "Endpoint to Create an Author Book",
            tags = {"AuthorBook"},
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = AuthorBookResponse.class))
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
    @PostMapping
    public ResponseEntity<Object> createdAuthorBook(
            @RequestBody AuthorBookRequest request
    ) {
        logger.info(
                String.format(
                        "HTTP POST %s",
                        AuthorBookController.REQUEST_MAPPING_PATH
                )
        );

        AuthorBookResponse response = authorBookService.createAuthorBook(request);
        AuthorBookMapperHateoas.set(response, AuthorBookHateoasWithRel.CREATE_AUTHOR_BOOK);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get All Author Books",
            description = "Endpoint to Get All Author Books",
            tags = {"AuthorBook"},
            responses = {
                    @ApiResponse(
                            description = "OK",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = AuthorBookResponse.class)
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
                            description = "InternalServerError",
                            responseCode = "500",
                            content = @Content
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Object> getAllAuthorBooks(
            Pageable pageable,
            @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        logger.info(
                String.format(
                        "HTTP GET %s - page number %d - page size %d - sort %s",
                        AuthorBookController.REQUEST_MAPPING_PATH,
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort()
                )
        );

        Map<String, Object> filters = new HashMap<>() {{
            put("name", name);
        }};
        List<AuthorBookResponse> authorBookResponses =
                authorBookService.getAllAuthorBooks(filters, pageable);

        AuthorBookMapperHateoas.set(
                authorBookResponses,
                pageable,
                AuthorBookHateoasWithRel.GET_ALL_AUTHOR_BOOKS
        );
        return ResponseEntity.status(HttpStatus.OK).body(authorBookResponses);
    }

    @Operation(
            summary = "Get an Author Books",
            description = "Endpoint to Get an Author Books",
            tags = {"AuthorBook"},
            responses = {
                    @ApiResponse(
                            description = "OK",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthorBookResponse.class)
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
                            description = "InternalServerError",
                            responseCode = "500",
                            content = @Content
                    )
            }
    )
    @GetMapping(value = "{id}")
    public ResponseEntity<Object> getAuthorBookById(
            @PathVariable(value = "id") UUID id
    ) {
        logger.info(
                String.format(
                        "HTTP GET %s/%s",
                        AuthorBookController.REQUEST_MAPPING_PATH,
                        id
                )
        );

        AuthorBookResponse authorBookResponse =
                authorBookService.getAuthorBookById(id);

        AuthorBookMapperHateoas.set(
                authorBookResponse,
                AuthorBookHateoasWithRel.GET_AUTHOR_BOOK_BY_ID
        );
        return ResponseEntity.status(HttpStatus.OK).body(authorBookResponse);
    }

    @Operation(
            summary = "Update Partials an Author Book",
            description = "Endpoint to Update partials an Author Book",
            tags = {"AuthorBook"},
            responses = {
                    @ApiResponse(
                            description = "NoContent",
                            responseCode = "204",
                            content = @Content(
                                    schema = @Schema(implementation = AuthorBookResponse.class)
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
                            description = "InternalServerError",
                            responseCode = "500",
                            content = @Content
                    )
            }
    )
    @PatchMapping(value = "{id}")
    public ResponseEntity<Object> updatePartialsAuthorBookById(
            @PathVariable(value = "id") UUID id,
            @RequestBody AuthorBookRequest request
    ) {
        logger.info(
                String.format(
                        "HTTP PATCH %s/%s",
                        AuthorBookController.REQUEST_MAPPING_PATH,
                        id
                )
        );

        authorBookService.updatePartialsAuthorBook(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(
            summary = "Remove an Author Book",
            description = "Endpoint to Remove an Author Book",
            tags = {"AuthorBook"},
            responses = {
                    @ApiResponse(
                            description = "NoContent",
                            responseCode = "204",
                            content = @Content(
                                    schema = @Schema(implementation = AuthorBookResponse.class)
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
                            description = "InternalServerError",
                            responseCode = "500",
                            content = @Content
                    )
            }
    )
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Object> deleteAuthorBookById(
            @PathVariable(value = "id") UUID id
    ) {
        logger.info(
                String.format(
                        "HTTP DELETE %s/%s",
                        AuthorBookController.REQUEST_MAPPING_PATH,
                        id
                )
        );

        authorBookService.removeAuthorBookById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
