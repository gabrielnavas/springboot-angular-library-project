package com.library.api.book.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private String isbn;
    private Integer pages;
    private List<String> keyWords;
    private Date publication;
    private UUID publishingCompanyId;
    private UUID classificationBookId;
    private UUID authorBookId;
}
