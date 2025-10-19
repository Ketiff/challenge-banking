// src/main/java/com/bank/customer/infrastructure/persistence/ClienteR2dbcRepository.java
package com.bank.customer.infrastructure.persistence;

import com.bank.customer.domain.entities.Cliente;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository R2DBC para la tabla clientes con queries manuales
 * para manejar la herencia Persona → Cliente
 */
@Repository
public interface ClienteR2dbcRepository extends R2dbcRepository<Cliente, Long> {

    /**
     * Query con JOIN para obtener Cliente con datos de Persona
     */
    @Query("""
        SELECT 
            p.id, p.nombre, p.genero, p.identificacion, p.direccion, p.telefono,
            p.created_at as persona_created_at, p.updated_at as persona_updated_at,
            c.contrasena, c.estado,
            c.created_at as cliente_created_at, c.updated_at as cliente_updated_at
        FROM clientes c
        INNER JOIN personas p ON c.id = p.id
        WHERE c.id = :id
    """)
    Mono<Cliente> findCustomerById(@Param("id") Long id);

    /**
     * Query con JOIN para obtener todos los clientes
     */
    @Query("""
        SELECT 
            p.id, p.nombre, p.genero, p.identificacion, p.direccion, p.telefono,
            p.created_at as persona_created_at, p.updated_at as persona_updated_at,
            c.contrasena, c.estado,
            c.created_at as cliente_created_at, c.updated_at as cliente_updated_at
        FROM clientes c
        INNER JOIN personas p ON c.id = p.id
    """)
    Flux<Cliente> findAllCustomers();

    /**
     * Query con JOIN por identificación
     */
    @Query("""
        SELECT 
            p.id, p.nombre, p.genero, p.identificacion, p.direccion, p.telefono,
            p.created_at as persona_created_at, p.updated_at as persona_updated_at,
            c.contrasena, c.estado,
            c.created_at as cliente_created_at, c.updated_at as cliente_updated_at
        FROM clientes c
        INNER JOIN personas p ON c.id = p.id
        WHERE p.identificacion = :identificacion
    """)
    Mono<Cliente> findByIdentificacion(@Param("identificacion") String identificacion);
}