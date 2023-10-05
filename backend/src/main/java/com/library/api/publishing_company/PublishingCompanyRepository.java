package com.library.api.publishing_company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublishingCompanyRepository extends JpaRepository<PublishingCompany, UUID> {
}
