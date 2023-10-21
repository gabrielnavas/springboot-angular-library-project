package com.library.api.book;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = BookController.REQUEST_MAPPING_PATH)
@AllArgsConstructor
public class BookController {
    public static final String REQUEST_MAPPING_PATH = "/api/v1/book";

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Object> createBook(
            @RequestBody BookRequest request
    ) {
        BookResponse bookResponse = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse);
    }

    @GetMapping
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

    @GetMapping("{id}")
    public ResponseEntity<Object> getBookById(
            @PathVariable("id") UUID id
    ) {
        BookResponse bookResponse = bookService.findBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookResponse);
    }
}
