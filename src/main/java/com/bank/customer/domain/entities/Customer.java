// src/main/java/com/bank/customer/domain/entities/Customer.java
package com.bank.customer.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Customer - Extiende Person con información específica de cliente bancario
 * Usa herencia JOINED: datos de Person en tabla 'personas', datos propios en tabla 'clientes'
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "id")
public class Customer extends Person {

    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 255, message = "Password must be between 4 and 255 characters")
    @Column(name = "contrasena", nullable = false)
    private String password;

    @NotNull(message = "Status is required")
    @Column(name = "estado", nullable = false)
    private Boolean status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime customerCreatedAt;

    @Column(name = "updated_at")
    private LocalDateTime customerUpdatedAt;

    /**
     * Callback antes de persistir
     */
    @PrePersist
    protected void onCustomerCreate() {
        customerCreatedAt = LocalDateTime.now();
        customerUpdatedAt = LocalDateTime.now();
        if (status == null) {
            status = true;
        }
    }

    /**
     * Callback antes de actualizar
     */
    @PreUpdate
    protected void onCustomerUpdate() {
        customerUpdatedAt = LocalDateTime.now();
    }

    // ========== MÉTODOS DE NEGOCIO ==========

    public boolean isActive() {
        return status != null && status;
    }

    public void activate() {
        this.status = true;
        this.customerUpdatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = false;
        this.customerUpdatedAt = LocalDateTime.now();
    }

    public boolean canPerformBankingOperations() {
        return isActive() && hasValidBasicInfo();
    }

    public void updatePassword(String newPassword) {
        if (newPassword != null && newPassword.length() >= 4) {
            this.password = newPassword;
            this.customerUpdatedAt = LocalDateTime.now();
        }
    }

    public String getCustomerSummary() {
        return String.format("Customer[id=%d, name=%s, identification=%s, active=%s]",
                getId(), getFormattedName(), getIdentification(), isActive());
    }
}