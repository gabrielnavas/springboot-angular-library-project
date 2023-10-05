package com.library.api.publishing_company;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/publishing_company")
@AllArgsConstructor
public class PublishingCompanyController {

    private final PublishingCompanyService publishingCompanyService;

    @PostMapping
    public ResponseEntity<Object> createPublishingCompany(
            @RequestBody PublishingCompanyRequest requestBody
    ) {
        PublishingCompany publishingCompany = publishingCompanyService.createPublishingCompany(requestBody);
        PublishingCompanyResponse responseBody = PublishingCompanyResponse.builder()
                .id(publishingCompany.getId())
                .name(publishingCompany.getName())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}
