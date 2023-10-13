package com.library.api.author_book;

import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorBookService {

    private final AuthorBookRepository authorBookRepository;

    public AuthorBookResponse createAuthorBook(AuthorBookRequest data) {
        Optional<AuthorBook> optionalAuthorBook = authorBookRepository.findByName(data.name());
        if (optionalAuthorBook.isPresent()) {
            throw new ObjectAlreadyExistsWithException("author book", "name", data.name());
        }

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
