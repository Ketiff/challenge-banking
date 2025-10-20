package com.bank.customer.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear un nuevo cliente")
public class CreateCustomerRequest {

    @Schema(description = "Nombre completo del cliente", example = "Maria Lopez", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 100)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Género del cliente", example = "Female", allowableValues = {"Male", "Female", "Other"}, maxLength = 20)
    @Size(max = 20, message = "Gender cannot exceed 20 characters")
    private String gender;

    @Schema(description = "Número de identificación (10-20 dígitos)", example = "2222222222", requiredMode = Schema.RequiredMode.REQUIRED, pattern = "^[0-9]{10,20}$")
    @NotBlank(message = "Identification is required")
    @Pattern(
            regexp = "^[0-9]{10,20}$",
            message = "Identification must contain between 10 and 20 numeric digits"
    )
    private String identification;

    @Schema(description = "Dirección de residencia", example = "Quito, La Mariscal", maxLength = 200)
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @Schema(description = "Número de teléfono", example = "0999999999", pattern = "^[0-9+\\-\\s()]{7,15}$")
    @Pattern(
            regexp = "^[0-9+\\-\\s()]{7,15}$",
            message = "Invalid phone format. Use numbers, +, -, spaces or parentheses"
    )
    private String phone;

    @Schema(description = "Contraseña del cliente", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 4, maxLength = 255, format = "password")
    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 255, message = "Password must be between 4 and 255 characters")
    private String password;
}