package com.library.api.author_book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, UUID> {
}