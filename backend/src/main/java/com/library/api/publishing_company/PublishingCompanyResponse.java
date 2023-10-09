package com.library.api.publishing_company;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "name"})
public class PublishingCompanyResponse extends RepresentationModel<PublishingCompanyResponse> {

    @JsonProperty("id")
    private UUID key;
    private String name;
}
