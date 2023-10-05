package com.library.api.publishing_company;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Table(name = "publishing_company", schema = "public")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishingCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Length(max = 25)
    @Column(unique = true)
    private String name;
}
