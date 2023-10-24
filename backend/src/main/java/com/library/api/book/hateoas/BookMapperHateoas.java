package com.library.api.book.hateoas;

import com.library.api.book.BookController;
import com.library.api.book.BookRequest;
import com.library.api.book.BookResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class BookMapperHateoas {
    static public void set(BookResponse dto, BookHateoasWithRel withRel) {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("id"));
        setWithPageable(dto, pageRequest, withRel);
        setWithoutPageable(dto, dto.getKey(), withRel);
    }


    static public void set(List<BookResponse> list, Pageable pageable, BookHateoasWithRel withRel) {
        for (var dto : list) {
            setWithPageable(dto, pageable, withRel);
            setWithoutPageable(dto, dto.getKey(), withRel);
        }
    }

    static private void setWithPageable(BookResponse dto, Pageable pageable, BookHateoasWithRel withRel) {
        dto.add(linkTo(methodOn(BookController.class).getAllBooks(pageable, "", "")).withRel(
                withRel.equals(BookHateoasWithRel.GET_ALL_BOOKS)
                        ? BookHateoasWithRel.SELF.getDescription()
                        : BookHateoasWithRel.GET_ALL_BOOKS.getDescription()
        ));
    }

    static private void setWithoutPageable(BookResponse dto, UUID personId, BookHateoasWithRel withRel) {
        dto.add(
                linkTo(methodOn(BookController.class).createBook(new BookRequest())).withRel(
                        withRel.equals(BookHateoasWithRel.CREATE_BOOK)
                                ? BookHateoasWithRel.SELF.getDescription()
                                : BookHateoasWithRel.CREATE_BOOK.getDescription()
                )
        );
        linkTo(methodOn(BookController.class).updatePartialsBookById(personId, new BookRequest())).withRel(
                withRel.equals(BookHateoasWithRel.UPDATE_PARTIALS_BOOK)
                        ? BookHateoasWithRel.SELF.getDescription()
                        : BookHateoasWithRel.UPDATE_PARTIALS_BOOK.getDescription()
        );
        dto.add(
                linkTo(methodOn(BookController.class).getBookById(personId)).withRel(
                        withRel.equals(BookHateoasWithRel.GET_BOOK_BY_ID)
                                ? BookHateoasWithRel.SELF.getDescription()
                                : BookHateoasWithRel.GET_BOOK_BY_ID.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(BookController.class).removeBookById(personId)).withRel(
                        withRel.equals(BookHateoasWithRel.REMOVE_BOOK_BY_ID)
                                ? BookHateoasWithRel.SELF.getDescription()
                                : BookHateoasWithRel.REMOVE_BOOK_BY_ID.getDescription()
                )
        );
    }
}
