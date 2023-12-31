package com.library.api.classification_book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClassificationBookRepository extends JpaRepository<ClassificationBook, UUID> {
    @Query("SELECT c FROM ClassificationBook c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:name,'%'))")
    Page<ClassificationBook> findAllByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT c FROM ClassificationBook c WHERE c.name = :name")
    Optional<ClassificationBook> findByName(@Param("name") String name);
}
