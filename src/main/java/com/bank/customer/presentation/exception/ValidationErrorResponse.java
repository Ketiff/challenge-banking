package com.bank.customer.presentation.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO para responses de errores de validación
 */
@Data                    // ⬅️ IMPORTANTE
@Builder                 // ⬅️ IMPORTANTE
@NoArgsConstructor       // ⬅️ IMPORTANTE
@AllArgsConstructor      // ⬅️ IMPORTANTE
public class ValidationErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private Integer status;

    private String error;

    private String message;

    private Map<String, String> validationErrors;
}