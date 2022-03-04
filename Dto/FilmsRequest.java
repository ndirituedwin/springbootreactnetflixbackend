package com.ndirituedwin.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmsRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String genre;
    private int maturity;
    @NotBlank
    private String  slug;
}
