package com.library.api.author_book;

import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import com.library.api.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public AuthorBookResponse getAuthorBookById(UUID id) {
        Optional<AuthorBook> optionalAuthorBook = authorBookRepository.findById(id);
        if (optionalAuthorBook.isEmpty()) {
            throw new ObjectNotFoundException("author book");
        }
        AuthorBook authorBook = optionalAuthorBook.get();
        return modelToResponse(authorBook);
    }

    private static AuthorBookResponse modelToResponse(AuthorBook authorBook) {
        return AuthorBookResponse.builder()
                .key(authorBook.getId())
                .name(authorBook.getName())
                .createdAt(authorBook.getCreatedAt())
                .updatedAt(authorBook.getUpdatedAt())
                .build();
    }

    public void updatePartialsAuthorBook(UUID id, AuthorBookRequest request) {
        Optional<AuthorBook> optionalAuthorBook = authorBookRepository.findById(id);
        if (optionalAuthorBook.isEmpty()) {
            throw new ObjectNotFoundException("author book");
        }

        Optional<AuthorBook> optionalAuthorBookByName = authorBookRepository.findByName(request.name());
        if (optionalAuthorBookByName.isPresent()
                && !optionalAuthorBookByName.get().getId().equals(id)) {
            throw new ObjectAlreadyExistsWithException("author book", "name", request.name());
        }

        AuthorBook authorBook = optionalAuthorBook.get();
        authorBook.setName(request.name());
        authorBookRepository.save(authorBook);
    }

    public void removeAuthorBookById(UUID id) {
        Optional<AuthorBook> optionalAuthorBook = authorBookRepository.findById(id);
        if (optionalAuthorBook.isEmpty()) {
            throw new ObjectNotFoundException("author book");
        }
        AuthorBook authorBook = optionalAuthorBook.get();
        authorBookRepository.delete(authorBook);
    }
}
