package com.bank.customer.application.services;

import com.bank.customer.application.dto.CustomerDTO;
import com.bank.customer.application.dto.CreateCustomerRequest;
import com.bank.customer.application.dto.UpdateCustomerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface para operaciones de Customer.
 * Define los casos de uso de la aplicación.
 */
public interface CustomerService {

    /**
     * Crea un nuevo cliente
     * @param request datos del nuevo cliente
     * @return cliente creado
     */
    Mono<CustomerDTO> createCustomer(CreateCustomerRequest request);

    /**
     * Busca un cliente por ID
     * @param id identificador del cliente
     * @return cliente encontrado o error si no existe
     */
    Mono<CustomerDTO> findCustomerById(Long id);

    /**
     * Busca un cliente por identificación (cédula)
     * @param identificacion número de identificación
     * @return cliente encontrado o error si no existe
     */
    Mono<CustomerDTO> findCustomerByIdentificacion(String identificacion);

    /**
     * Lista todos los clientes
     * @return flux de clientes
     */
    Flux<CustomerDTO> findAllCustomers();

    /**
     * Actualiza un cliente existente
     * @param id identificador del cliente
     * @param request datos actualizados
     * @return cliente actualizado
     */
    Mono<CustomerDTO> updateCustomer(Long id, UpdateCustomerRequest request);

    /**
     * Elimina un cliente (soft delete - desactiva)
     * @param id identificador del cliente
     * @return void
     */
    Mono<Void> deleteCustomer(Long id);

    /**
     * Elimina físicamente un cliente de la base de datos
     * @param id identificador del cliente
     * @return void
     */
    Mono<Void> hardDeleteCustomer(Long id);
}