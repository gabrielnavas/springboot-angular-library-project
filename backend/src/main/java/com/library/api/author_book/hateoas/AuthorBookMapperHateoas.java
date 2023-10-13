package com.library.api.author_book.hateoas;

import com.library.api.author_book.AuthorBookResponse;
import com.library.api.publishing_company.PublishingCompanyController;
import com.library.api.publishing_company.PublishingCompanyRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class AuthorBookMapperHateoas {
    static public void set(AuthorBookResponse dto, AuthorBookHateoasWithRel withRel) {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("id"));
        setWithPageable(dto, pageRequest, withRel);
        setWithoutPageable(dto, dto.getKey(), withRel);
    }


    static public void set(List<AuthorBookResponse> list, Pageable pageable, AuthorBookHateoasWithRel withRel) {
        for (var dto : list) {
            setWithPageable(dto, pageable, withRel);
            setWithoutPageable(dto, dto.getKey(), withRel);
        }
    }

    static private void setWithPageable(AuthorBookResponse dto, Pageable pageable, AuthorBookHateoasWithRel withRel) {
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class)
                        .getAllPublishingCompany(pageable, "")).withRel(
                        withRel.equals(AuthorBookHateoasWithRel.GET_ALL_AUTHOR_BOOKS)
                                ? AuthorBookHateoasWithRel.SELF.getDescription()
                                : AuthorBookHateoasWithRel.GET_ALL_AUTHOR_BOOKS.getDescription()
                )
        );
    }

    static private void setWithoutPageable(AuthorBookResponse dto, UUID personId, AuthorBookHateoasWithRel withRel) {
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class)
                        .createPublishingCompany(new PublishingCompanyRequest(""))).withRel(
                        withRel.equals(AuthorBookHateoasWithRel.CREATE_AUTHOR_BOOK)
                                ? AuthorBookHateoasWithRel.SELF.getDescription()
                                : AuthorBookHateoasWithRel.CREATE_AUTHOR_BOOK.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class)
                        .updatePublishingCompany(personId, new PublishingCompanyRequest(""))).withRel(
                        withRel.equals(AuthorBookHateoasWithRel.UPDATE_PARTIALS_AUTHOR_BOOK)
                                ? AuthorBookHateoasWithRel.SELF.getDescription()
                                : AuthorBookHateoasWithRel.UPDATE_PARTIALS_AUTHOR_BOOK.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).getPublishingCompanyById(personId)).withRel(
                        withRel.equals(AuthorBookHateoasWithRel.GET_AUTHOR_BOOK_BY_ID)
                                ? AuthorBookHateoasWithRel.SELF.getDescription()
                                : AuthorBookHateoasWithRel.GET_AUTHOR_BOOK_BY_ID.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).removePublishingCompanyById(personId)).withRel(
                        withRel.equals(AuthorBookHateoasWithRel.REMOVE_AUTHOR_BOOK_BY_ID)
                                ? AuthorBookHateoasWithRel.SELF.getDescription()
                                : AuthorBookHateoasWithRel.REMOVE_AUTHOR_BOOK_BY_ID.getDescription()
                )
        );
    }
}
