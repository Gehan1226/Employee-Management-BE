package edu.icet.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessResponse<T> {
    private int status;
    private String message;
    private T data;
}