package com.library.api.book;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByTitle(String title);

    @Query("""
            SELECT b FROM 
            Book b 
            WHERE 
                LOWER(b.title) LIKE LOWER(CONCAT('%',:title,'%')) OR COALESCE(:title ,'') = '' 
                OR 
                LOWER(b.isbn) LIKE LOWER(CONCAT('%',:isbn,'%')) OR COALESCE(:isbn ,'') = ''
            """)
    List<Book> findAllBooksWithParams(@Param("title") String title, @Param("isbn") String isbn, Pageable pageable);
}
