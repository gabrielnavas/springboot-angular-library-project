package com.library.api.book.controllers;

import com.library.api.author_book.AuthorBookResponse;
import com.library.api.classification_book.ClassificationBookResponse;
import com.library.api.publishing_company.PublishingCompanyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private UUID id;
    private String title;
    private String isbn;
    private Integer pages;
    private List<String> keyWords;
    private Date publication;
    private Date createdAt;
    private Date updatedAt;
    private PublishingCompanyResponse publishingCompany;
    private ClassificationBookResponse classificationBook;
    private AuthorBookResponse authorBookResponse;

    public static BookResponse modelToResponse(Book model) {
        return BookResponse.builder()
                .id(model.getId())
                .title(model.getTitle())
                .isbn(model.getIsbn())
                .keyWords(Arrays.stream(model.getKeyWords().split(",")).toList())
                .publication(model.getPublication())
                .pages(model.getPages())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .authorBookResponse(AuthorBookResponse.modelToResponse(model.getAuthorBook()))
                .classificationBook(ClassificationBookResponse.modelToResponse(model.getClassificationBook()))
                .publishingCompany(PublishingCompanyResponse.modelToResponse(model.getPublishingCompany()))
                .build();

    }
}
