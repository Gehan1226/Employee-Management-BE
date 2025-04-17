package edu.icet.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PaginatedResponse<T> {
    private int status;
    private String message;
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int currentPage;
}
