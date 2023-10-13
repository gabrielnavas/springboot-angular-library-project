package com.library.api.author_book;

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
@Table(name = "author_books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBook {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotNull
    @Column(name = "created_at")
    private Date createdAt;

    @NotNull
    @Column(name = "updated_at")
    private Date updatedAt;
}
