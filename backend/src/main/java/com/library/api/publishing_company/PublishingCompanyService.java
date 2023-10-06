package com.library.api.publishing_company;

import com.library.api.exceptions.ObjectAlreadyExistsWith;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class PublishingCompanyService {

    private final PublishingCompanyRepository publishingCompanyRepository;

    private final Logger logger = Logger.getLogger(PublishingCompanyService.class.getName());

    public PublishingCompanyResponse createPublishingCompany(PublishingCompanyRequest data) {
        logger.info("create post Publishing Company Service");

        Optional<PublishingCompany> optionalPublishingCompany = publishingCompanyRepository.findByName(data.name());
        if (optionalPublishingCompany.isPresent()) {
            throw new ObjectAlreadyExistsWith("publishing company", data.name());
        }
        var publishingCompany = PublishingCompany.builder()
                .id(UUID.randomUUID())
                .name(data.name())
                .build();
        publishingCompany = publishingCompanyRepository.save(publishingCompany);
        return PublishingCompanyResponse.builder()
                .key(publishingCompany.getId())
                .name(publishingCompany.getName())
                .build();

    }
}
