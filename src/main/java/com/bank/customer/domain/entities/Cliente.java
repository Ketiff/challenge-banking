package com.bank.customer.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entidad Cliente - Representa solo los datos de la tabla clientes
 * Los datos de Persona se manejan por separado
 *
 * IMPORTANTE: NO extendemos Persona aquí para evitar problemas con R2DBC
 * Conceptualmente Cliente "tiene" datos de Persona, no "es" una Persona en la BD
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("clientes")
public class Cliente {

    @Id
    private Long id;

    // ========== CAMPOS DE LA TABLA CLIENTES ==========

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 255, message = "La contraseña debe tener entre 4 y 255 caracteres")
    @Column("contrasena")
    private String contrasena;

    @NotNull(message = "El estado es obligatorio")
    @Column("estado")
    private Boolean estado;

    @Column("created_at")
    private LocalDateTime clientCreatedAt;

    @Column("updated_at")
    private LocalDateTime clientUpdatedAt;

    // ========== CAMPOS TRANSIENTES (NO SE PERSISTEN EN CLIENTES) ==========
    // Estos campos vienen de la tabla personas, pero los guardamos temporalmente aquí

    @Transient  // No se persiste en la tabla clientes
    private String nombre;

    @Transient
    private String genero;

    @Transient
    private String identificacion;

    @Transient
    private String direccion;

    @Transient
    private String telefono;

    @Transient
    private LocalDateTime createdAt;

    @Transient
    private LocalDateTime updatedAt;

    // ========== MÉTODOS DE NEGOCIO ==========

    public boolean isActive() {
        return estado != null && estado;
    }

    public void activate() {
        this.estado = true;
        this.clientUpdatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.estado = false;
        this.clientUpdatedAt = LocalDateTime.now();
    }

    public boolean canPerformBankingOperations() {
        return isActive() && hasValidBasicInfo();
    }

    public void updatePassword(String newPassword) {
        if (newPassword != null && newPassword.length() >= 4) {
            this.contrasena = newPassword;
            this.clientUpdatedAt = LocalDateTime.now();
        }
    }

    public String getClientSummary() {
        return String.format("Cliente[id=%d, nombre=%s, identificacion=%s, activo=%s]",
                id, nombre, identificacion, isActive());
    }

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
}