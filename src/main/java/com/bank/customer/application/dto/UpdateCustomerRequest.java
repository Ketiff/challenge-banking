package com.bank.customer.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para actualizar un cliente existente (actualización parcial)")
public class UpdateCustomerRequest {

    @Schema(description = "Nombre completo del cliente", example = "Maria Lopez Updated", minLength = 2, maxLength = 100)
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Género del cliente", example = "Female", maxLength = 20)
    @Size(max = 20, message = "Gender cannot exceed 20 characters")
    private String gender;

    @Schema(description = "Dirección de residencia", example = "Quito, La Carolina", maxLength = 200)
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @Schema(description = "Número de teléfono", example = "0988888888", pattern = "^[0-9+\\-\\s()]{7,15}$")
    @Pattern(
            regexp = "^[0-9+\\-\\s()]{7,15}$",
            message = "Invalid phone format"
    )
    private String phone;

    @Schema(description = "Nueva contraseña", example = "newPassword456", minLength = 4, maxLength = 255, format = "password")
    @Size(min = 4, max = 255, message = "Password must be between 4 and 255 characters")
    private String password;

    @Schema(description = "Estado del cliente", example = "true")
    private Boolean status;
}