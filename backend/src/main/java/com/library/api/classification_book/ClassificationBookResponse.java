package com.library.api.classification_book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(value = {"id", "name", "createdAt", "updatedAt"})
public class ClassificationBookResponse extends RepresentationModel<ClassificationBookResponse> {

    @JsonProperty(value = "id")
    private UUID key;
    private String name;

    private Date createdAt;
    private Date updatedAt;

    public static ClassificationBookResponse modelToResponse(ClassificationBook classificationBook) {
        return ClassificationBookResponse.builder()
                .key(classificationBook.getId())
                .name(classificationBook.getName())
                .createdAt(classificationBook.getCreatedAt())
                .updatedAt(classificationBook.getUpdatedAt())
                .build();
    }
}
