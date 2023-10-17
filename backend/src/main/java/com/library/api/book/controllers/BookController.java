package com.library.api.book.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
