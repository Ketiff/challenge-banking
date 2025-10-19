package com.bank.customer.domain.repositories;

import com.bank.customer.domain.entities.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    // Operaciones CRUD básicas reactivas
    Mono<Cliente> save(Cliente customer);

    Mono<Cliente> findById(Long id);

    Flux<Cliente> findAll();

    Mono<Cliente> update(Cliente customer);

    Mono<Void> deleteById(Long id);

    // Consultas de negocio - NO depende de Spring, R2DBC, JPA ni tecnologias externas
    Mono<Boolean> existsByIdentificacion(String identificacion);

    Mono<Cliente> findByIdentificacion(String identificacion);
}