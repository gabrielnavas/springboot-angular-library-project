package com.library.api.publishing_company;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PublishingCompanyService {

    private final PublishingCompanyRepository publishingCompanyRepository;

    public PublishingCompany createPublishingCompany(PublishingCompanyRequest data) {
        var publishingCompany = PublishingCompany.builder()
                .id(UUID.randomUUID())
                .name(data.name())
                .build();
        return publishingCompanyRepository.save(publishingCompany);
    }
}
