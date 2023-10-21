package com.library.api.book;

import com.library.api.author_book.AuthorBook;
import com.library.api.author_book.AuthorBookRepository;
import com.library.api.classification_book.ClassificationBook;
import com.library.api.classification_book.ClassificationBookRepository;
import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import com.library.api.exceptions.ObjectNotFoundException;
import com.library.api.publishing_company.PublishingCompany;
import com.library.api.publishing_company.PublishingCompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PublishingCompanyRepository publishingCompanyRepository;
    private final AuthorBookRepository authorBookRepository;
    private final ClassificationBookRepository classificationBookRepository;

    public BookResponse createBook(BookRequest data) {
        Optional<Book> optionalBook = bookRepository.findByTitle(data.getTitle());
        if (optionalBook.isPresent()) {
            throw new ObjectAlreadyExistsWithException("book", "title", data.getTitle());
        }

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

    public List<BookResponse> findAllBook(Map<String, String> filters, Pageable pageable) {
        Book book = Book.builder()
                .title(filters.get("title"))
                .isbn(filters.get("isbn"))
                .build();

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("title", contains().ignoreCase())
                .withMatcher("isbn", contains().ignoreCase());

        Example<Book> bookFilterExample = Example.of(book, exampleMatcher);

        return bookRepository.findAll(bookFilterExample)
                .stream()
                .map(BookResponse::modelToResponse)
                .toList();
    }

    public BookResponse findBookById(UUID id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new ObjectNotFoundException("book");
        }
        Book book = optionalBook.get();
        return BookResponse.modelToResponse(book);
    }

    public void updatePartialsBookById(UUID id, BookRequest bookRequest) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new ObjectNotFoundException("book");
        }

        Optional<Book> optionalBookByTitle = bookRepository.findByTitle(bookRequest.getTitle());
        if (optionalBookByTitle.isPresent()) {
            throw new ObjectAlreadyExistsWithException("book", "title", bookRequest.getTitle());
        }


        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository.findById(bookRequest.getPublishingCompanyId());
        if (optionalPublishingCompany.isEmpty()) {
            throw new ObjectNotFoundException("publishing company");
        }

        Optional<AuthorBook> optionalAuthorBook = authorBookRepository.findById(bookRequest.getAuthorBookId());
        if (optionalAuthorBook.isEmpty()) {
            throw new ObjectNotFoundException("author book");
        }

        Optional<ClassificationBook> optionalClassificationBook = classificationBookRepository.findById(bookRequest.getClassificationBookId());
        if (optionalClassificationBook.isEmpty()) {
            throw new ObjectNotFoundException("classification book");
        }

        Date now = new Date();

        Book book = optionalBook.get();
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setPages(bookRequest.getPages());
        book.setKeyWords(String.join(",", bookRequest.getKeyWords()));
        book.setPublication(bookRequest.getPublication());
        book.setPublishingCompany(optionalPublishingCompany.get());
        book.setAuthorBook(optionalAuthorBook.get());
        book.setClassificationBook(optionalClassificationBook.get());
        book.setUpdatedAt(now);

        bookRepository.save(book);
    }

    public void removeBookById(UUID id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new ObjectNotFoundException("book");
        }
        Book book = optionalBook.get();
        bookRepository.delete(book);
    }
}
