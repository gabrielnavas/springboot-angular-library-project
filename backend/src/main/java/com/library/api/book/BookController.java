package com.library.api.book;

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
@RequestMapping(value = BookController.REQUEST_MAPPING_PATH)
@AllArgsConstructor
@Tag(name = "Book", description = "Endpoints to Managing the Book")
public class BookController {
    public static final String REQUEST_MAPPING_PATH = "/api/v1/book";

    private final BookService bookService;


    @Operation(
            summary = "Create a Book",
            description = "Endpoint to Create an Book",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))
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
    public ResponseEntity<Object> createBook(
            @RequestBody BookRequest request
    ) {
        BookResponse bookResponse = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse);
    }


    @Operation(
            summary = "Get All Books",
            description = "Endpoint to Get All Books",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BookResponse.class)
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
    public ResponseEntity<Object> getAllBooks(
            Pageable pageable,
            @RequestParam(value = "title", defaultValue = "", required = false) String title,
            @RequestParam(value = "isbn", defaultValue = "", required = false) String isbn
    ) {
        List<BookResponse> bookResponses = bookService.findAllBook(new HashMap<>() {{
            put("title", title);
            put("isbn", isbn);
        }}, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(bookResponses);
    }


    @Operation(
            summary = "Get a Book By Id",
            description = "Endpoint to Get a Book By Id",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "NoContent",
                            responseCode = "204",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BookResponse.class)
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
    @GetMapping(
            value = "{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> getBookById(
            @PathVariable("id") UUID id
    ) {
        BookResponse bookResponse = bookService.findBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookResponse);
    }


    @Operation(
            summary = "Update Partials a Book By Id",
            description = "Endpoint to Update Partials a Book By Id",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "NoContent",
                            responseCode = "204",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BookResponse.class)
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
    @PatchMapping(
            value = "{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> updatePartialsBookById(
            @PathVariable("id") UUID id,
            @RequestBody BookRequest request
    ) {
        bookService.updatePartialsBookById(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
