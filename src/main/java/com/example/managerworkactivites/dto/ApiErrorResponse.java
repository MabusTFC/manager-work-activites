package com.example.managerworkactivites.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
