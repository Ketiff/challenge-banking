// src/main/java/com/bank/customer/infrastructure/persistence/CustomerRepositoryImpl.java
package com.bank.customer.infrastructure.persistence;

import com.bank.customer.domain.entities.Cliente;
import com.bank.customer.domain.entities.Persona;
import com.bank.customer.domain.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final PersonaR2dbcRepository personaRepository;
    private final ClienteR2dbcRepository clienteRepository;
    private final DatabaseClient databaseClient;  // Para queries manuales

    @Override
    @Transactional
    public Mono<Cliente> save(Cliente customer) {
        log.info("Saving customer with identification: {}", customer.getIdentificacion());

        // Paso 1: Guardar Persona
        Persona persona = createPersonaFromCliente(customer);

        return personaRepository.save(persona)
                .flatMap(savedPersona -> {
                    // Paso 2: INSERT manual en clientes usando el ID generado
                    Long personaId = savedPersona.getId();
                    LocalDateTime now = LocalDateTime.now();

                    String insertSql = """
                        INSERT INTO clientes (id, contrasena, estado, created_at, updated_at)
                        VALUES (:id, :contrasena, :estado, :created_at, :updated_at)
                    """;

                    return databaseClient.sql(insertSql)
                            .bind("id", personaId)
                            .bind("contrasena", customer.getContrasena())
                            .bind("estado", customer.getEstado() != null ? customer.getEstado() : true)
                            .bind("created_at", now)
                            .bind("updated_at", now)
                            .fetch()
                            .rowsUpdated()
                            .flatMap(rows -> {
                                // Paso 3: Buscar el cliente completo recién creado
                                return findById(personaId);
                            });
                })
                .doOnSuccess(saved -> log.info("Customer saved successfully with ID: {}", saved.getId()))
                .doOnError(error -> log.error("Error saving customer: {}", error.getMessage(), error));
    }

    @Override
    public Mono<Cliente> findById(Long id) {
        log.debug("Finding customer by ID: {}", id);

        // Paso 1: Buscar Cliente
        return clienteRepository.findById(id)
                .flatMap(cliente -> {
                    // Paso 2: Buscar Persona y combinar datos
                    return personaRepository.findById(id)
                            .map(persona -> {
                                // Copiar campos de Persona a Cliente
                                cliente.setNombre(persona.getNombre());
                                cliente.setGenero(persona.getGenero());
                                cliente.setIdentificacion(persona.getIdentificacion());
                                cliente.setDireccion(persona.getDireccion());
                                cliente.setTelefono(persona.getTelefono());
                                cliente.setCreatedAt(persona.getCreatedAt());
                                cliente.setUpdatedAt(persona.getUpdatedAt());
                                return cliente;
                            });
                })
                .doOnSuccess(customer -> {
                    if (customer != null) {
                        log.debug("Customer found: {}", customer.getNombre());
                    } else {
                        log.debug("Customer not found with ID: {}", id);
                    }
                });
    }

    @Override
    public Flux<Cliente> findAll() {
        log.debug("Finding all customers");

        return clienteRepository.findAll()
                .flatMap(cliente -> {
                    // Para cada cliente, buscar su persona
                    return personaRepository.findById(cliente.getId())
                            .map(persona -> {
                                cliente.setNombre(persona.getNombre());
                                cliente.setGenero(persona.getGenero());
                                cliente.setIdentificacion(persona.getIdentificacion());
                                cliente.setDireccion(persona.getDireccion());
                                cliente.setTelefono(persona.getTelefono());
                                cliente.setCreatedAt(persona.getCreatedAt());
                                cliente.setUpdatedAt(persona.getUpdatedAt());
                                return cliente;
                            });
                })
                .doOnComplete(() -> log.debug("Finished retrieving all customers"));
    }

    @Override
    @Transactional
    public Mono<Cliente> update(Cliente customer) {
        log.info("Updating customer with ID: {}", customer.getId());

        // Actualizar Persona
        Persona persona = createPersonaFromCliente(customer);

        return personaRepository.save(persona)
                .flatMap(updatedPersona -> {
                    // UPDATE manual en clientes
                    LocalDateTime now = LocalDateTime.now();

                    String updateSql = """
                        UPDATE clientes 
                        SET contrasena = :contrasena, 
                            estado = :estado, 
                            updated_at = :updated_at
                        WHERE id = :id
                    """;

                    return databaseClient.sql(updateSql)
                            .bind("id", customer.getId())
                            .bind("contrasena", customer.getContrasena())
                            .bind("estado", customer.getEstado())
                            .bind("updated_at", now)
                            .fetch()
                            .rowsUpdated()
                            .flatMap(rows -> findById(customer.getId()));
                })
                .doOnSuccess(updated -> log.info("Customer updated successfully"))
                .doOnError(error -> log.error("Error updating customer: {}", error.getMessage()));
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(Long id) {
        log.info("Deleting customer with ID: {}", id);

        // Eliminar cliente primero (FK constraint), luego persona
        return clienteRepository.deleteById(id)
                .then(personaRepository.deleteById(id))
                .doOnSuccess(v -> log.info("Customer deleted successfully"))
                .doOnError(error -> log.error("Error deleting customer: {}", error.getMessage()));
    }

    @Override
    public Mono<Boolean> existsByIdentificacion(String identificacion) {
        log.debug("Checking if customer exists with identification: {}", identificacion);
        return personaRepository.existsByIdentificacion(identificacion);
    }

    @Override
    public Mono<Cliente> findByIdentificacion(String identificacion) {
        log.debug("Finding customer by identification: {}", identificacion);

        return personaRepository.findByIdentificacion(identificacion)
                .flatMap(persona -> {
                    // Buscar cliente con ese ID
                    return clienteRepository.findById(persona.getId())
                            .map(cliente -> {
                                // Combinar datos
                                cliente.setNombre(persona.getNombre());
                                cliente.setGenero(persona.getGenero());
                                cliente.setIdentificacion(persona.getIdentificacion());
                                cliente.setDireccion(persona.getDireccion());
                                cliente.setTelefono(persona.getTelefono());
                                cliente.setCreatedAt(persona.getCreatedAt());
                                cliente.setUpdatedAt(persona.getUpdatedAt());
                                return cliente;
                            });
                });
    }

    /**
     * Helper: Crea Persona a partir de Cliente
     */
    private Persona createPersonaFromCliente(Cliente cliente) {
        Persona persona = new Persona();
        persona.setId(cliente.getId());
        persona.setNombre(cliente.getNombre());
        persona.setGenero(cliente.getGenero());
        persona.setIdentificacion(cliente.getIdentificacion());
        persona.setDireccion(cliente.getDireccion());
        persona.setTelefono(cliente.getTelefono());
        persona.setCreatedAt(cliente.getCreatedAt() != null ? cliente.getCreatedAt() : LocalDateTime.now());
        persona.setUpdatedAt(LocalDateTime.now());
        return persona;
    }
}