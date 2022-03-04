package com.ndirituedwin.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeriesResponse {

    private String title;
    private String description;
    private String genre;
    private int maturity;
    private String slug;
    private Instant createdAt;
    private Instant updatedAt;
    private UserSummary createdBy;
}
