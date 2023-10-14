package com.library.api.author_book;

import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
        return modelToResponse(authorBook);
    }

    public List<AuthorBookResponse> getAllAuthorBooks(Map<String, Object> filters, Pageable pageable) {
        if (filters.containsKey("name") && ((String) filters.get("name")).length() > 0) {
            String name = (String) filters.get("name");
            return authorBookRepository.findAllByLikeName(name, pageable)
                    .stream().map(AuthorBookService::modelToResponse)
                    .toList();
        }

        return authorBookRepository.findAll(pageable)
                .stream()
                .map(AuthorBookService::modelToResponse)
                .toList();
    }

    private static AuthorBookResponse modelToResponse(AuthorBook authorBook) {
        return AuthorBookResponse.builder()
                .key(authorBook.getId())
                .name(authorBook.getName())
                .createdAt(authorBook.getCreatedAt())
                .updatedAt(authorBook.getUpdatedAt())
                .build();
    }
}
