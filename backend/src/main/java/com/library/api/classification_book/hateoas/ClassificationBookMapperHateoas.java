package com.library.api.classification_book.hateoas;

import com.library.api.classification_book.ClassificationBookResponse;
import com.library.api.publishing_company.PublishingCompanyController;
import com.library.api.publishing_company.PublishingCompanyRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ClassificationBookMapperHateoas {
    static public void set(ClassificationBookResponse dto, ClassificationBookHateoasWithRel withRel) {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("id"));
        setWithPageable(dto, pageRequest, withRel);
        setWithoutPageable(dto, dto.getKey(), withRel);
    }


    static public void set(List<ClassificationBookResponse> list, Pageable pageable, ClassificationBookHateoasWithRel withRel) {
        for (var dto : list) {
            setWithPageable(dto, pageable, withRel);
            setWithoutPageable(dto, dto.getKey(), withRel);
        }
    }

    static private void setWithPageable(ClassificationBookResponse dto, Pageable pageable, ClassificationBookHateoasWithRel withRel) {
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).getAllPublishingCompany(pageable, "")).withRel(
                        withRel.equals(ClassificationBookHateoasWithRel.GET_ALL_CLASSIFICATION_BOOKS)
                                ? ClassificationBookHateoasWithRel.SELF.getDescription()
                                : ClassificationBookHateoasWithRel.GET_ALL_CLASSIFICATION_BOOKS.getDescription()
                )
        );
    }

    static private void setWithoutPageable(ClassificationBookResponse dto, UUID personId, ClassificationBookHateoasWithRel withRel) {
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).createPublishingCompany(new PublishingCompanyRequest(""))).withRel(
                        withRel.equals(ClassificationBookHateoasWithRel.CREATE_CLASSIFICATION_BOOK)
                                ? ClassificationBookHateoasWithRel.SELF.getDescription()
                                : ClassificationBookHateoasWithRel.CREATE_CLASSIFICATION_BOOK.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).updatePublishingCompany(personId, new PublishingCompanyRequest(""))).withRel(
                        withRel.equals(ClassificationBookHateoasWithRel.UPDATE_PARTIALS_CLASSIFICATION_BOOK)
                                ? ClassificationBookHateoasWithRel.SELF.getDescription()
                                : ClassificationBookHateoasWithRel.UPDATE_PARTIALS_CLASSIFICATION_BOOK.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).getPublishingCompanyById(personId)).withRel(
                        withRel.equals(ClassificationBookHateoasWithRel.GET_CLASSIFICATION_BOOK_BY_ID)
                                ? ClassificationBookHateoasWithRel.SELF.getDescription()
                                : ClassificationBookHateoasWithRel.GET_CLASSIFICATION_BOOK_BY_ID.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).removePublishingCompanyById(personId)).withRel(
                        withRel.equals(ClassificationBookHateoasWithRel.REMOVE_CLASSIFICATION_BOOK_BY_ID)
                                ? ClassificationBookHateoasWithRel.SELF.getDescription()
                                : ClassificationBookHateoasWithRel.REMOVE_CLASSIFICATION_BOOK_BY_ID.getDescription()
                )
        );
    }
}
