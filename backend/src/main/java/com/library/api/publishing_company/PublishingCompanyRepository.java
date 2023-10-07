package com.library.api.publishing_company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PublishingCompanyRepository extends JpaRepository<PublishingCompany, UUID> {
    Optional<PublishingCompany> findByName(String name);

    @Query("SELECT c FROM PublishingCompany c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%'))")
    Page<PublishingCompany> findAllByName(@Param("name") String name, Pageable pageable);
}
