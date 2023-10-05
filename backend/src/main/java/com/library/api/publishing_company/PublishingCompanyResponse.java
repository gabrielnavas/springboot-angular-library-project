package com.library.api.publishing_company;


import lombok.Builder;

import java.util.UUID;

@Builder
public record PublishingCompanyResponse(UUID id, String name) {
}
