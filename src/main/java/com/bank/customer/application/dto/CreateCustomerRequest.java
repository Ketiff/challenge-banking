package com.bank.customer.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos al crear un nuevo Customer.
 *
 * Características:
 * - Todos los campos necesarios son obligatorios (@NotBlank)
 * - Validaciones específicas para creación
 * - NO incluye ID (se genera automáticamente)
 * - NO incluye timestamps (se generan automáticamente)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    /**
     * Nombre completo del cliente
     * Obligatorio, entre 2 y 100 caracteres
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    /**
     * Género del cliente
     * Opcional, máximo 20 caracteres
     */
    @Size(max = 20, message = "El género no puede exceder 20 caracteres")
    private String genero;

    /**
     * Número de identificación (cédula, pasaporte)
     * Obligatorio, formato numérico de 10-20 dígitos
     */
    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(
            regexp = "^[0-9]{10,20}$",
            message = "La identificación debe contener entre 10 y 20 dígitos numéricos"
    )
    private String identificacion;

    /**
     * Dirección de residencia
     * Opcional, máximo 200 caracteres
     */
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;

    /**
     * Número de teléfono
     * Opcional, formato flexible
     */
    @Pattern(
            regexp = "^[0-9+\\-\\s()]{7,15}$",
            message = "Formato de teléfono inválido. Use números, +, -, espacios o paréntesis"
    )
    private String telefono;

    /**
     * Contraseña del cliente
     * Obligatorio, entre 4 y 255 caracteres
     * TODO: En producción, usar hash bcrypt
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 255, message = "La contraseña debe tener entre 4 y 255 caracteres")
    private String contrasena;

    // NOTA: NO incluimos 'id', 'estado', 'createdAt', 'updatedAt'
    // Estos campos se generan automáticamente en el backend
}