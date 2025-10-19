// src/main/java/com/bank/customer/infrastructure/persistence/PersonaR2dbcRepository.java
package com.bank.customer.infrastructure.persistence;

import com.bank.customer.domain.entities.Persona;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repository R2DBC para la tabla personas
 */
@Repository
public interface PersonaR2dbcRepository extends R2dbcRepository<Persona, Long> {

    Mono<Boolean> existsByIdentificacion(String identificacion);

    Mono<Persona> findByIdentificacion(String identificacion);
}