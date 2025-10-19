// src/main/java/com/bank/customer/domain/entities/Persona.java
package com.bank.customer.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entidad base Persona - Contiene información personal básica
 * Esta clase representa el concepto de dominio de una persona en el sistema bancario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("personas")
public class Persona {

    @Id
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column("nombre")
    private String nombre;

    @Size(max = 20, message = "El género no puede exceder 20 caracteres")
    @Column("genero")
    private String genero;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[0-9]{10,20}$", message = "La identificación debe contener entre 10 y 20 dígitos")
    @Column("identificacion")
    private String identificacion;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Column("direccion")
    private String direccion;

    @Pattern(regexp = "^[0-9+\\-\\s()]{7,15}$", message = "Formato de teléfono inválido")
    @Column("telefono")
    private String telefono;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    // Métodos de negocio (mantener los que ya tienes)

    public boolean hasValidBasicInfo() {
        return nombre != null && !nombre.trim().isEmpty()
                && identificacion != null && !identificacion.trim().isEmpty();
    }

    public String getFormattedName() {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "";
        }
        return nombre.trim().substring(0, 1).toUpperCase() +
                nombre.trim().substring(1).toLowerCase();
    }

    public boolean hasValidEcuadorianId() {
        if (identificacion == null || identificacion.length() != 10) {
            return false;
        }

        try {
            int[] digits = identificacion.chars().map(c -> c - '0').toArray();
            int province = digits[0] * 10 + digits[1];
            return province >= 1 && province <= 24;
        } catch (Exception e) {
            return false;
        }
    }
}