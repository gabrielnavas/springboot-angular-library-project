package com.library.api.book;

import com.library.api.author_book.AuthorBook;
import com.library.api.classification_book.ClassificationBook;
import com.library.api.publishing_company.PublishingCompany;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(length = 200, unique = true)
    private String title;

    @NotBlank
    @Column(length = 200)
    private String isbn;

    @NotNull
    @Column(length = 200)
    private Integer pages;

    @NotBlank
    @Column(length = 1000)
    private String keyWords;

    @NotNull
    private Date publication;

    @NotNull
    @Column(name = "created_at")
    private Date createdAt;

    @NotNull
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_publishing_company")
    private PublishingCompany publishingCompany;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_classification_books")
    private ClassificationBook classificationBook;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_author_books")
    private AuthorBook authorBook;
}
