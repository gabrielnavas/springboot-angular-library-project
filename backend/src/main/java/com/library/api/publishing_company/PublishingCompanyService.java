package com.library.api.publishing_company;

import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import com.library.api.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class PublishingCompanyService {

    private final PublishingCompanyRepository publishingCompanyRepository;

    private final Logger logger = Logger.getLogger(PublishingCompanyService.class.getName());

    public PublishingCompanyResponse createPublishingCompany(PublishingCompanyRequest data) {
        logger.info("create Publishing Company Service");

        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository.findByName(data.name());
        if (optionalPublishingCompany.isPresent()) {
            throw new ObjectAlreadyExistsWithException("publishing company", "name", data.name());
        }

        Date now = new Date();

        var publishingCompany = PublishingCompany.builder()
                .id(UUID.randomUUID())
                .name(data.name())
                .createdAt(now)
                .updateAt(now)
                .build();
        publishingCompany = publishingCompanyRepository.save(publishingCompany);
        return PublishingCompanyResponse.modelToResponse(publishingCompany);

    }

    public void updatePublishingCompany(UUID publishingCompanyId, PublishingCompanyRequest data) {
        logger.info("update Publishing Company Service");

        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository
                .findById(publishingCompanyId);
        if (optionalPublishingCompany.isEmpty()) {
            throw new ObjectNotFoundException("publishing company");
        }

        Optional<PublishingCompany> optionalPublishingCompanyByName = publishingCompanyRepository
                .findByName(data.name());
        if (optionalPublishingCompanyByName.isPresent()
                && !optionalPublishingCompanyByName.get().getId().equals(publishingCompanyId)) {
            throw new ObjectAlreadyExistsWithException(
                    "publishing company",
                    "name",
                    optionalPublishingCompanyByName.get().getName()
            );
        }

        PublishingCompany publishingCompany = optionalPublishingCompany.get();
        publishingCompany.setName(data.name());
        publishingCompanyRepository.save(publishingCompany);
    }

    public List<PublishingCompanyResponse> getAllPublishingCompany(Map<String, String> filters, Pageable pageable) {
        logger.info("get all Publishing Company Service");

        Page<PublishingCompany> publishingCompanies;

        if (filters.get("name").length() > 0) {
            publishingCompanies = publishingCompanyRepository
                    .findAllByName(
                            filters.get("name"),
                            pageable
                    );
        } else {
            publishingCompanies = publishingCompanyRepository
                    .findAll(pageable);

        }

        return publishingCompanies.stream()
                .map(PublishingCompanyResponse::modelToResponse)
                .toList();
    }

    public PublishingCompanyResponse getPublishingCompanyById(UUID publishingCompaniesId) {
        logger.info("get Publishing Company By Id Service");

        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository.findById(publishingCompaniesId);
        if (optionalPublishingCompany.isEmpty()) {
            throw new ObjectNotFoundException("publishing company");
        }

        PublishingCompany publishingCompany = optionalPublishingCompany.get();
        return PublishingCompanyResponse.modelToResponse(publishingCompany);
    }


    public void removePublishingCompanyById(UUID publishingCompaniesId) {
        logger.info("remove Publishing Company By Id Service");

        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository.findById(publishingCompaniesId);
        if (optionalPublishingCompany.isEmpty()) {
            throw new ObjectNotFoundException("publishing company");
        }

        PublishingCompany publishingCompany = optionalPublishingCompany.get();
        publishingCompanyRepository.delete(publishingCompany);
    }
}
