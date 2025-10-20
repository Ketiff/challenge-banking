// src/main/java/com/bank/customer/infrastructure/persistence/CustomerJpaRepository.java
package com.bank.customer.infrastructure.persistence;

import com.bank.customer.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository para Customer
 * Hereda TODOS los métodos de JpaRepository
 */
@Repository
public interface CustomerJpaRepository extends JpaRepository<Customer, Long> {

    /**
     * Busca cliente por identificación (campo heredado de Person)
     * JPA hace el JOIN automáticamente gracias a la herencia
     */
    Optional<Customer> findByIdentification(String identification);

    /**
     * Verifica si existe cliente con esa identificación
     */
    boolean existsByIdentification(String identification);

    /**
     * Busca clientes activos
     */
    @Query("SELECT c FROM Customer c WHERE c.status = true")
    Optional<Customer> findByStatusTrue();
}