package com.library.api.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.library.api.author_book.AuthorBookResponse;
import com.library.api.classification_book.ClassificationBookResponse;
import com.library.api.publishing_company.PublishingCompanyResponse;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder(value = {"id", "title", "isbn", "pages", "keyWords", "publication",
        "createdAt", "updatedAt", "publishingCompany", "classificationBook", "authorBookResponse"})
public class BookResponse extends RepresentationModel<BookResponse> {

    @JsonProperty(value = "id")
    private UUID key;
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
                .key(model.getId())
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
