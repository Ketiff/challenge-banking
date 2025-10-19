// src/main/java/com/bank/customer/presentation/exception/ErrorResponse.java
package com.bank.customer.presentation.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para responses de error estándar
 */
@Data                    // ⬅️ IMPORTANTE: Debe estar presente
@Builder                 // ⬅️ IMPORTANTE: Genera el método builder()
@NoArgsConstructor       // ⬅️ IMPORTANTE: Constructor vacío
@AllArgsConstructor      // ⬅️ IMPORTANTE: Constructor con todos los campos
public class ApiErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private Integer status;

    private String error;

    private String message;
}