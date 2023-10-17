package com.library.api.author_book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(value = {"id", "name", "createdAt", "updatedAt"})
public class AuthorBookResponse extends RepresentationModel<AuthorBookResponse> {

    @JsonProperty(value = "id")
    private UUID key;
    private String name;
    private Date createdAt;
    private Date updatedAt;

    public static AuthorBookResponse modelToResponse(AuthorBook model) {
        return AuthorBookResponse.builder()
                .key(model.getId())
                .name(model.getName())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }
}
