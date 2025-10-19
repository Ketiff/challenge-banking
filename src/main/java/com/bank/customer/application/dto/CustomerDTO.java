package com.bank.customer.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferir datos de Customer en las respuestas de la API.
 *
 * Características:
 * - NO incluye contraseña (seguridad)
 * - Formato JSON controlado
 * - Independiente del modelo de dominio
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluir campos null en JSON
public class CustomerDTO {

    /**
     * ID único del cliente
     */
    private Long id;

    /**
     * Nombre completo del cliente
     * Ejemplo: "Jose Lema"
     */
    private String nombre;

    /**
     * Género del cliente
     * Valores esperados: "Masculino", "Femenino", "Otro"
     */
    private String genero;

    /**
     * Número de identificación (cédula, pasaporte)
     * Ejemplo: "1234567890"
     */
    private String identificacion;

    /**
     * Dirección de residencia
     * Ejemplo: "Otavalo sn y principal"
     */
    private String direccion;

    /**
     * Número de teléfono
     * Ejemplo: "098254785"
     */
    private String telefono;

    /**
     * Estado del cliente (activo/inactivo)
     * true = activo, false = inactivo
     */
    private Boolean estado;

    /**
     * Fecha de creación del registro
     * Formato: "2025-10-14T10:30:00"
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización
     * Formato: "2025-10-14T10:30:00"
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // ========== MÉTODOS DE UTILIDAD ==========

    /**
     * Verifica si el cliente está activo
     * @return true si estado es true
     */
    public boolean isActive() {
        return estado != null && estado;
    }

    /**
     * Obtiene resumen del cliente para logs
     * @return string con información básica (sin datos sensibles)
     */
    public String getSummary() {
        return String.format("Customer[id=%d, nombre=%s, activo=%s]",
                id, nombre, isActive());
    }
}