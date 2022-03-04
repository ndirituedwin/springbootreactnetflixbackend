package com.ndirituedwin.Dto.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAvatarResponse {

    private String username;
    private String avatar;
    private String message;
}
