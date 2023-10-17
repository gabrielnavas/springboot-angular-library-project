package com.library.api.book.controllers;

import com.library.api.author_book.AuthorBook;
import com.library.api.author_book.AuthorBookRepository;
import com.library.api.classification_book.ClassificationBook;
import com.library.api.classification_book.ClassificationBookRepository;
import com.library.api.exceptions.ObjectNotFoundException;
import com.library.api.publishing_company.PublishingCompany;
import com.library.api.publishing_company.PublishingCompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PublishingCompanyRepository publishingCompanyRepository;
    private final AuthorBookRepository authorBookRepository;
    private final ClassificationBookRepository classificationBookRepository;

    public BookResponse createBook(BookRequest data) {
        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository.findById(data.getPublishingCompanyId());
        if (optionalPublishingCompany.isEmpty()) {
            throw new ObjectNotFoundException("publishing company");
        }

        Optional<AuthorBook> optionalAuthorBook = authorBookRepository.findById(data.getAuthorBookId());
        if (optionalAuthorBook.isEmpty()) {
            throw new ObjectNotFoundException("author book");
        }

        Optional<ClassificationBook> optionalClassificationBook = classificationBookRepository.findById(data.getClassificationBookId());
        if (optionalClassificationBook.isEmpty()) {
            throw new ObjectNotFoundException("classification book");
        }

        Date now = new Date();

        Book book = Book.builder()
                .title(data.getTitle())
                .isbn(data.getIsbn())
                .keyWords(String.join(",", data.getKeyWords()))
                .pages(data.getPages())
                .publication(data.getPublication())
                .publishingCompany(optionalPublishingCompany.get())
                .authorBook(optionalAuthorBook.get())
                .classificationBook(optionalClassificationBook.get())
                .createdAt(now)
                .updatedAt(now)
                .build();

        book = bookRepository.save(book);

        return BookResponse.modelToResponse(book);
    }
}
