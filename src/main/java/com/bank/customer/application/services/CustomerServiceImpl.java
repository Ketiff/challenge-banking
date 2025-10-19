// src/main/java/com/bank/customer/application/services/CustomerServiceImpl.java
package com.bank.customer.application.services;

import com.bank.customer.application.dto.CustomerDTO;
import com.bank.customer.application.dto.CreateCustomerRequest;
import com.bank.customer.application.dto.UpdateCustomerRequest;
import com.bank.customer.domain.entities.Cliente;
import com.bank.customer.domain.exceptions.CustomerExceptions.*;
import com.bank.customer.domain.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación del servicio de Customer.
 * Contiene la lógica de negocio y orquestación.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Mono<CustomerDTO> createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer with identification: {}", request.getIdentificacion());

        // Validación 1: Cliente no debe existir previamente
        return customerRepository.existsByIdentificacion(request.getIdentificacion())
                .flatMap(exists -> {
                    if (exists) {
                        log.warn("Customer already exists with identification: {}", request.getIdentificacion());
                        return Mono.error(new CustomerAlreadyExistsException(
                                "Customer with identification " + request.getIdentificacion() + " already exists"
                        ));
                    }

                    // Crear entidad Cliente a partir del request
                    Cliente cliente = mapToEntity(request);

                    // Guardar en repositorio
                    return customerRepository.save(cliente);
                })
                .map(this::mapToDTO)
                .doOnSuccess(dto -> log.info("Customer created successfully with ID: {}", dto.getId()))
                .doOnError(error -> log.error("Error creating customer: {}", error.getMessage()));
    }

    @Override
    public Mono<CustomerDTO> findCustomerById(Long id) {
        log.debug("Finding customer by ID: {}", id);

        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(
                        "Customer not found with ID: " + id
                )))
                .map(this::mapToDTO)
                .doOnSuccess(dto -> log.debug("Customer found: {}", dto.getNombre()));
    }

    @Override
    public Mono<CustomerDTO> findCustomerByIdentificacion(String identificacion) {
        log.debug("Finding customer by identification: {}", identificacion);

        return customerRepository.findByIdentificacion(identificacion)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(
                        "Customer not found with identification: " + identificacion
                )))
                .map(this::mapToDTO);
    }

    @Override
    public Flux<CustomerDTO> findAllCustomers() {
        log.debug("Finding all customers");

        return customerRepository.findAll()
                .map(this::mapToDTO)
                .doOnComplete(() -> log.debug("Finished retrieving all customers"));
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer with ID: {}", id);

        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(
                        "Customer not found with ID: " + id
                )))
                .flatMap(existingCliente -> {
                    // Actualizar solo los campos que vienen en el request
                    updateEntityFromRequest(existingCliente, request);

                    return customerRepository.update(existingCliente);
                })
                .map(this::mapToDTO)
                .doOnSuccess(dto -> log.info("Customer updated successfully"))
                .doOnError(error -> log.error("Error updating customer: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteCustomer(Long id) {
        log.info("Deactivating customer with ID: {}", id);

        // Soft delete: solo desactivamos el cliente
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(
                        "Customer not found with ID: " + id
                )))
                .flatMap(cliente -> {
                    cliente.deactivate(); // Método del dominio
                    return customerRepository.update(cliente);
                })
                .then()
                .doOnSuccess(v -> log.info("Customer deactivated successfully"))
                .doOnError(error -> log.error("Error deactivating customer: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> hardDeleteCustomer(Long id) {
        log.warn("Hard deleting customer with ID: {}", id);

        // Hard delete: eliminación física
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(
                        "Customer not found with ID: " + id
                )))
                .flatMap(cliente -> customerRepository.deleteById(id))
                .doOnSuccess(v -> log.info("Customer hard deleted successfully"))
                .doOnError(error -> log.error("Error hard deleting customer: {}", error.getMessage()));
    }

    // ========== MÉTODOS PRIVADOS DE MAPEO ==========

    /**
     * Mapea CreateCustomerRequest a entidad Cliente
     */
    private Cliente mapToEntity(CreateCustomerRequest request) {
        Cliente cliente = new Cliente();

        // Campos de Persona (heredados)
        cliente.setNombre(request.getNombre());
        cliente.setGenero(request.getGenero());
        cliente.setIdentificacion(request.getIdentificacion());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setCreatedAt(LocalDateTime.now());
        cliente.setUpdatedAt(LocalDateTime.now());

        // Campos propios de Cliente
        cliente.setContrasena(request.getContrasena()); // TODO: Encriptar en producción
        cliente.setEstado(true); // Por defecto activo
        cliente.setClientCreatedAt(LocalDateTime.now());
        cliente.setClientUpdatedAt(LocalDateTime.now());

        return cliente;
    }

    /**
     * Mapea entidad Cliente a DTO
     */
    private CustomerDTO mapToDTO(Cliente cliente) {
        return CustomerDTO.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .genero(cliente.getGenero())
                .identificacion(cliente.getIdentificacion())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .estado(cliente.getEstado())
                .createdAt(cliente.getCreatedAt())
                .updatedAt(cliente.getUpdatedAt())
                .build();
    }

    /**
     * Actualiza entidad Cliente desde UpdateCustomerRequest
     * Solo actualiza campos no nulos del request
     */
    private void updateEntityFromRequest(Cliente cliente, UpdateCustomerRequest request) {
        if (request.getNombre() != null) {
            cliente.setNombre(request.getNombre());
        }
        if (request.getGenero() != null) {
            cliente.setGenero(request.getGenero());
        }
        if (request.getDireccion() != null) {
            cliente.setDireccion(request.getDireccion());
        }
        if (request.getTelefono() != null) {
            cliente.setTelefono(request.getTelefono());
        }
        if (request.getContrasena() != null) {
            cliente.setContrasena(request.getContrasena()); // TODO: Encriptar
        }
        if (request.getEstado() != null) {
            cliente.setEstado(request.getEstado());
        }

        // Actualizar timestamps
        cliente.setUpdatedAt(LocalDateTime.now());
        cliente.setClientUpdatedAt(LocalDateTime.now());
    }
}