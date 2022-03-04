package com.ndirituedwin.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String usernameorEmail ;
    private Instant expiresAt;

    
}
