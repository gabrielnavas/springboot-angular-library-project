package com.library.api.classification_book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationBookResponse extends RepresentationModel<ClassificationBookResponse> {

    @JsonProperty(value = "id")
    private UUID key;
    private String name;
}
