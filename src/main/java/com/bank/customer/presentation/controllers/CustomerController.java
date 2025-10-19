package com.bank.customer.presentation.controllers;

import com.bank.customer.application.dto.CreateCustomerRequest;
import com.bank.customer.application.dto.CustomerDTO;
import com.bank.customer.application.dto.UpdateCustomerRequest;
import com.bank.customer.application.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST Controller para operaciones de Customer.
 * Expone endpoints HTTP para gestión de clientes.
 *
 * Base URL: /api/v1/customers
 */
@Slf4j // Puedes usar: log.info(), log.error(), log.debug()
@RestController // Combina @Controller + @ResponseBody - Todas las respuestas se convierten automáticamente a JSON - Indica que esta clase maneja requests HTTP
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor // Inyección de dependencias por constructor (best practice)
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Crea un nuevo cliente
     *
     * Endpoint: POST /api/v1/customers
     * Body: CreateCustomerRequest (JSON)
     * Response: 201 Created + CustomerDTO
     *
     * Ejemplo de request:
     * {
     *   "nombre": "Maria Lopez",
     *   "identificacion": "0987654321",
     *   "contrasena": "pass123"
     * }
     */

    /*
    // Define la ruta base para todos los endpoints de esta clase
    // Todos los métodos heredan esta ruta
    // Ejemplo: @GetMapping("/{id}") → /api/v1/customers/{id}
    * */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerDTO> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        log.info("REST request to create Customer: {}", request.getNombre());

        return customerService.createCustomer(request)
                .doOnSuccess(dto -> log.info("Customer created with ID: {}", dto.getId()))
                .doOnError(error -> log.error("Error creating customer: {}", error.getMessage()));
    }

    /**
     * Obtiene todos los clientes
     *
     * Endpoint: GET /api/v1/customers
     * Response: 200 OK + List<CustomerDTO>
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<CustomerDTO> getAllCustomers() {
        log.info("REST request to get all Customers");

        return customerService.findAllCustomers()
                .doOnComplete(() -> log.info("Retrieved all customers successfully"));
    }

    /**
     * Obtiene un cliente por ID
     *
     * Endpoint: GET /api/v1/customers/{id}
     * Response: 200 OK + CustomerDTO
     *           404 Not Found (si no existe)
     */
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerDTO> getCustomerById(@PathVariable Long id) {
        log.info("REST request to get Customer by ID: {}", id);

        return customerService.findCustomerById(id)
                .doOnSuccess(dto -> log.info("Customer found: {}", dto.getNombre()));
    }

    /**
     * Obtiene un cliente por número de identificación
     *
     * Endpoint: GET /api/v1/customers/identificacion/{identificacion}
     * Response: 200 OK + CustomerDTO
     *           404 Not Found (si no existe)
     */
    @GetMapping(
            value = "/identificacion/{identificacion}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerDTO> getCustomerByIdentificacion(@PathVariable String identificacion) {
        log.info("REST request to get Customer by identification: {}", identificacion);

        return customerService.findCustomerByIdentificacion(identificacion);
    }

    /**
     * Actualiza un cliente existente
     *
     * Endpoint: PUT /api/v1/customers/{id}
     * Body: UpdateCustomerRequest (JSON)
     * Response: 200 OK + CustomerDTO
     *           404 Not Found (si no existe)
     *
     * Ejemplo de request (actualización parcial):
     * {
     *   "telefono": "0999999999",
     *   "direccion": "Nueva dirección"
     * }
     */
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {

        log.info("REST request to update Customer with ID: {}", id);

        return customerService.updateCustomer(id, request)
                .doOnSuccess(dto -> log.info("Customer updated successfully: {}", dto.getId()));
    }

    /**
     * Desactiva un cliente (soft delete)
     *
     * Endpoint: DELETE /api/v1/customers/{id}
     * Response: 204 No Content
     *           404 Not Found (si no existe)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable Long id) {
        log.info("REST request to delete (deactivate) Customer with ID: {}", id);

        return customerService.deleteCustomer(id)
                .doOnSuccess(v -> log.info("Customer deactivated successfully"));
    }

    /**
     * Elimina físicamente un cliente (hard delete)
     *
     * Endpoint: DELETE /api/v1/customers/{id}/hard
     * Response: 204 No Content
     *           404 Not Found (si no existe)
     *
     * ADVERTENCIA: Esta operación es irreversible
     */
    @DeleteMapping("/{id}/hard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> hardDeleteCustomer(@PathVariable Long id) {
        log.warn("REST request to HARD DELETE Customer with ID: {}", id);

        return customerService.hardDeleteCustomer(id)
                .doOnSuccess(v -> log.info("Customer hard deleted successfully"));
    }
}