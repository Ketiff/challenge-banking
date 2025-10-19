package com.bank.customer.application.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos al actualizar un Customer existente.
 *
 * Características:
 * - TODOS los campos son opcionales (actualización parcial)
 * - Solo se actualizan los campos que vienen en el request
 * - Validaciones solo si el campo está presente
 * - NO permite cambiar identificación (campo inmutable)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {

    /**
     * Nombre completo del cliente
     * Opcional, pero si viene debe tener entre 2 y 100 caracteres
     */
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    /**
     * Género del cliente
     * Opcional, máximo 20 caracteres
     */
    @Size(max = 20, message = "El género no puede exceder 20 caracteres")
    private String genero;

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
            message = "Formato de teléfono inválido"
    )
    private String telefono;

    /**
     * Nueva contraseña del cliente
     * Opcional, pero si viene debe tener entre 4 y 255 caracteres
     * TODO: En producción, usar hash bcrypt
     */
    @Size(min = 4, max = 255, message = "La contraseña debe tener entre 4 y 255 caracteres")
    private String contrasena;

    /**
     * Estado del cliente (activo/inactivo)
     * Opcional, permite activar/desactivar
     */
    private Boolean estado;

    // NOTAS IMPORTANTES:
    // - NO incluimos 'id' (viene en la URL: PUT /customers/{id})
    // - NO incluimos 'identificacion' (campo inmutable, no se puede cambiar)
    // - NO incluimos timestamps (se actualizan automáticamente)
    // - Todos los campos son opcionales (actualización parcial)
}