package com.ndirituedwin.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor

public class imageresponse {
    private Resource image;
}
