package com.library.api.publishing_company;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "createdAt", "updatedAt"})
public class PublishingCompanyResponse
        extends RepresentationModel<PublishingCompanyResponse> {

    @JsonProperty("id")
    private UUID key;
    private String name;
    private Date createdAt;
    private Date updatedAt;

    public static PublishingCompanyResponse modelToResponse(PublishingCompany publishingCompany) {
        return PublishingCompanyResponse.builder()
                .key(publishingCompany.getId())
                .name(publishingCompany.getName())
                .createdAt(publishingCompany.getCreatedAt())
                .updatedAt(publishingCompany.getUpdateAt())
                .build();
    }
}
