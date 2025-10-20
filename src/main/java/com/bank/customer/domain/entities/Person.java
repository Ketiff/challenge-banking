// src/main/java/com/bank/customer/domain/entities/Person.java
package com.bank.customer.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad base Person - Contiene información personal básica
 * Usa estrategia JOINED para herencia con Customer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Size(max = 20, message = "Gender cannot exceed 20 characters")
    @Column(name = "genero", length = 20)
    private String gender;

    @NotBlank(message = "Identification is required")
    @Pattern(regexp = "^[0-9]{10,20}$", message = "Identification must contain between 10 and 20 digits")
    @Column(name = "identificacion", unique = true, nullable = false, length = 20)
    private String identification;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    @Column(name = "direccion", length = 200)
    private String address;

    @Pattern(regexp = "^[0-9+\\-\\s()]{7,15}$", message = "Invalid phone format")
    @Column(name = "telefono", length = 15)
    private String phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Callback antes de persistir
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Callback antes de actualizar
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ========== MÉTODOS DE NEGOCIO ==========

    public boolean hasValidBasicInfo() {
        return name != null && !name.trim().isEmpty()
                && identification != null && !identification.trim().isEmpty();
    }

    public String getFormattedName() {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        return name.trim().substring(0, 1).toUpperCase() +
                name.trim().substring(1).toLowerCase();
    }

    public boolean hasValidEcuadorianId() {
        if (identification == null || identification.length() != 10) {
            return false;
        }

        try {
            int[] digits = identification.chars().map(c -> c - '0').toArray();
            int province = digits[0] * 10 + digits[1];
            return province >= 1 && province <= 24;
        } catch (Exception e) {
            return false;
        }
    }
}