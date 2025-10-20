// src/main/java/com/bank/customer/domain/repositories/CustomerRepository.java
package com.bank.customer.domain.repositories;

import com.bank.customer.domain.entities.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface para Customer en el dominio.
 * Define el contrato de acceso a datos sin depender de JPA.
 *
 * CAMBIO: Retorna tipos síncronos (Optional, List) en vez de reactivos (Mono, Flux)
 */
public interface CustomerRepository {

    // Guarda un cliente (crea o actualiza)
    Customer save(Customer customer);

     // Busca un cliente por ID
    Optional<Customer> findById(Long id);

    // Obtiene todos los clientes
    List<Customer> findAll();

    // Actualiza un cliente existente
    Customer update(Customer customer);

    // Elimina un cliente por ID
    void deleteById(Long id);

    // Verifica si existe un cliente con la identificación dada
    boolean existsByIdentificacion(String identificacion);

    //  Busca un cliente por identificación
    Optional<Customer> findByIdentificacion(String identificacion);

    boolean existsByIdentification(String identification);

    Optional<Customer> findByIdentification(String identification);
}