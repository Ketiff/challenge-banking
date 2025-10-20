package com.bank.customer.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO de respuesta con información del cliente")
public class CustomerDTO {

    @Schema(description = "ID único del cliente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre completo del cliente", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Género del cliente", example = "Male", allowableValues = {"Male", "Female", "Other"})
    private String gender;

    @Schema(description = "Número de identificación (cédula/pasaporte)", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String identification;

    @Schema(description = "Dirección de residencia", example = "123 Main St, City")
    private String address;

    @Schema(description = "Número de teléfono", example = "0999999999", pattern = "^[0-9+\\-\\s()]{7,15}$")
    private String phone;

    @Schema(description = "Estado del cliente (activo/inactivo)", example = "true")
    private Boolean status;

    @Schema(description = "Fecha de creación del registro", example = "2025-10-19T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización", example = "2025-10-19T15:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // Métodos sin cambios
    public boolean isActive() {
        return status != null && status;
    }

    public String getSummary() {
        return String.format("Customer[id=%d, name=%s, active=%s]",
                id, name, isActive());
    }
}