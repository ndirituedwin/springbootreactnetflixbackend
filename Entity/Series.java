package com.ndirituedwin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "series")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Series extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title may not be blank")
    private String title;
    @NotBlank(message = " description may not be blank")
    private String description;
    @NotBlank(message = "genre may not be blank")
    private String genre;
    private int maturity;
    @NotBlank(message = "slug may not be blank")
    private String slug;
}
