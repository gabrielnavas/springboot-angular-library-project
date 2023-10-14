package com.library.api.author_book;

import com.library.api.author_book.hateoas.AuthorBookHateoasWithRel;
import com.library.api.author_book.hateoas.AuthorBookMapperHateoas;
import io.swagger.v3.oas.annotations.Operation;
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
        AuthorBookMapperHateoas.set(response, AuthorBookHateoasWithRel.CREATE_AUTHOR_BOOK);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Object> getAllAuthorBooks(
            Pageable pageable,
            @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
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
}
