package com.library.api.publishing_company;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublishingCompanyRequest(
        @NotBlank
        @Size(max = 25, message = "name is too large")
        String name
) {
}
