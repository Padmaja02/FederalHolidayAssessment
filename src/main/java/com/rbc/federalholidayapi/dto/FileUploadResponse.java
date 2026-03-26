package com.rbc.federalholidayapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FileUploadResponse {
    private int successCount;
    private int failureCount;
    private List<String> errors;
}
