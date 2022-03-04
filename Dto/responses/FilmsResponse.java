package com.ndirituedwin.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmsResponse {

    private String title;
    private String description;
    private String genre;
    private int maturity;
    private String  slug;
    private UserSummary createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
