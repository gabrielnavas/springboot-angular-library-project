package com.library.api.author_book;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, UUID> {

    @Query("SELECT ab FROM AuthorBook ab WHERE ab.name = :name")
    Optional<AuthorBook> findByName(@Param("name") String name);

    @Query("SELECT ab FROM AuthorBook ab WHERE LOWER(ab.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AuthorBook> findAllByLikeName(@Param("name") String name, Pageable pageable);
}