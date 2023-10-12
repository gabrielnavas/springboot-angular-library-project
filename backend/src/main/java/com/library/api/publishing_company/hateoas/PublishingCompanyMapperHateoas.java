package com.library.api.publishing_company.hateoas;

import com.library.api.publishing_company.PublishingCompanyController;
import com.library.api.publishing_company.PublishingCompanyRequest;
import com.library.api.publishing_company.PublishingCompanyResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PublishingCompanyMapperHateoas {
    static public void set(
            PublishingCompanyResponse dto,
            PublishingCompanyHateoasWithRel withRel) {
        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("id"));
        setWithPageable(dto, pageRequest, withRel);
        setWithoutPageable(dto, dto.getKey(), withRel);
    }


    static public void set(List<PublishingCompanyResponse> list, Pageable pageable, PublishingCompanyHateoasWithRel withRel) {
        for (var dto : list) {
            setWithPageable(dto, pageable, withRel);
            setWithoutPageable(dto, dto.getKey(), withRel);
        }
    }

    static private void setWithPageable(PublishingCompanyResponse dto, Pageable pageable, PublishingCompanyHateoasWithRel withRel) {
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).getAllPublishingCompany(pageable, "")).withRel(
                        withRel.equals(PublishingCompanyHateoasWithRel.GET_ALL_PUBLISHING_COMPANIES)
                                ? PublishingCompanyHateoasWithRel.SELF.getDescription()
                                : PublishingCompanyHateoasWithRel.GET_ALL_PUBLISHING_COMPANIES.getDescription()
                )
        );
    }

    static private void setWithoutPageable(PublishingCompanyResponse dto, UUID personId, PublishingCompanyHateoasWithRel withRel) {
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).createPublishingCompany(new PublishingCompanyRequest(""))).withRel(
                        withRel.equals(PublishingCompanyHateoasWithRel.CREATE_PUBLISHING_COMPANY)
                                ? PublishingCompanyHateoasWithRel.SELF.getDescription()
                                : PublishingCompanyHateoasWithRel.CREATE_PUBLISHING_COMPANY.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).updatePublishingCompany(personId, new PublishingCompanyRequest(""))).withRel(
                        withRel.equals(PublishingCompanyHateoasWithRel.UPDATE_PARTIALS_PUBLISHING_COMPANY)
                                ? PublishingCompanyHateoasWithRel.SELF.getDescription()
                                : PublishingCompanyHateoasWithRel.UPDATE_PARTIALS_PUBLISHING_COMPANY.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).getPublishingCompanyById(personId)).withRel(
                        withRel.equals(PublishingCompanyHateoasWithRel.GET_PUBLISHING_COMPANY_BY_ID)
                                ? PublishingCompanyHateoasWithRel.SELF.getDescription()
                                : PublishingCompanyHateoasWithRel.GET_PUBLISHING_COMPANY_BY_ID.getDescription()
                )
        );
        dto.add(
                linkTo(methodOn(PublishingCompanyController.class).removePublishingCompanyById(personId)).withRel(
                        withRel.equals(PublishingCompanyHateoasWithRel.REMOVE_PUBLISHING_COMPANY_BY_ID)
                                ? PublishingCompanyHateoasWithRel.SELF.getDescription()
                                : PublishingCompanyHateoasWithRel.REMOVE_PUBLISHING_COMPANY_BY_ID.getDescription()
                )
        );
    }
}
