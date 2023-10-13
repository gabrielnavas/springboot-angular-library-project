package com.library.api.author_book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/author-book")
@AllArgsConstructor
@Tag(name = "AuthorBook", description = "Endpoints to Managing the Author Book")
public class AuthorBookController {

    private final AuthorBookService authorBookService;

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
        AuthorBookResponse response = authorBookService.createAuthorBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
