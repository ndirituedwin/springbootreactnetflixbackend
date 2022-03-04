package com.ndirituedwin.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedFilmsResponse<T> {
    private List<T> films;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

}
