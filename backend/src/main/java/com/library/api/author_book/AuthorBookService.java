package com.library.api.author_book;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class AuthorBookService {

    private final AuthorBookRepository authorBookRepository;

    public AuthorBookResponse createAuthorBook(AuthorBookRequest data) {
        Date now = new Date();
        AuthorBook authorBook = AuthorBook.builder()
                .name(data.name())
                .createdAt(now)
                .updatedAt(now)
                .build();
        authorBook = authorBookRepository.save(authorBook);
        return AuthorBookResponse.builder()
                .key(authorBook.getId())
                .name(authorBook.getName())
                .createdAt(authorBook.getCreatedAt())
                .updatedAt(authorBook.getUpdatedAt())
                .build();
    }
}
