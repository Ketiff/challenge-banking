package com.bank.customer.presentation.controllers;

import com.bank.customer.application.dto.CreateCustomerRequest;
import com.bank.customer.application.dto.CustomerDTO;
import com.bank.customer.application.dto.UpdateCustomerRequest;
import com.bank.customer.application.services.CustomerService;
import com.bank.customer.presentation.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * Base URL: /api/v1/customers
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "Endpoints para gestión de clientes bancarios")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Crear nuevo cliente",
            description = "Crea un nuevo cliente en el sistema bancario con información personal y credenciales"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cliente creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cliente ya existe con la misma identificación",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerDTO> createCustomer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo cliente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateCustomerRequest.class))
            )
            @Valid @RequestBody CreateCustomerRequest request) {

        log.info("REST request to create Customer: {}", request.getName());

        return Mono.fromCallable(() -> {
            CustomerDTO created = customerService.createCustomer(request);
            log.info("Customer created with ID: {}", created.getId());
            return created;
        });
    }

    @Operation(
            summary = "Listar todos los clientes",
            description = "Obtiene la lista completa de clientes registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de clientes obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<CustomerDTO> getAllCustomers() {
        log.info("REST request to get all Customers");

        return Mono.fromCallable(() -> {
            var customers = customerService.findAllCustomers();
            log.info("Retrieved {} customers successfully", customers.size());
            return customers;
        }).flatMapMany(Flux::fromIterable);
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Obtiene la información detallada de un cliente específico mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerDTO> getCustomerById(
            @Parameter(description = "ID único del cliente", required = true, example = "1")
            @PathVariable Long id) {

        log.info("REST request to get Customer by ID: {}", id);

        return Mono.fromCallable(() -> {
            CustomerDTO customer = customerService.findCustomerById(id);
            log.info("Customer found: {}", customer.getName());
            return customer;
        });
    }

    @Operation(
            summary = "Buscar cliente por identificación",
            description = "Obtiene un cliente mediante su número de identificación (cédula/pasaporte)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping(
            value = "/identification/{identification}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerDTO> getCustomerByIdentification(
            @Parameter(description = "Número de identificación del cliente", required = true, example = "1234567890")
            @PathVariable String identification) {

        log.info("REST request to get Customer by identification: {}", identification);

        return Mono.fromCallable(() ->
                customerService.findCustomerByIdentification(identification)
        );
    }

    @Operation(
            summary = "Actualizar cliente",
            description = "Actualiza la información de un cliente existente. Soporta actualización parcial."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cliente actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<CustomerDTO> updateCustomer(
            @Parameter(description = "ID del cliente a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos a actualizar (solo campos presentes serán actualizados)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateCustomerRequest.class))
            )
            @Valid @RequestBody UpdateCustomerRequest request) {

        log.info("REST request to update Customer with ID: {}", id);

        return Mono.fromCallable(() -> {
            CustomerDTO updated = customerService.updateCustomer(id, request);
            log.info("Customer updated successfully: {}", updated.getId());
            return updated;
        });
    }

    @Operation(
            summary = "Desactivar cliente (soft delete)",
            description = "Desactiva un cliente sin eliminarlo físicamente de la base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Cliente desactivado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(
            @Parameter(description = "ID del cliente a desactivar", required = true, example = "1")
            @PathVariable Long id) {

        log.info("REST request to delete (deactivate) Customer with ID: {}", id);

        return Mono.fromRunnable(() -> {
            customerService.deleteCustomer(id);
            log.info("Customer deactivated successfully");
        });
    }

    @Operation(
            summary = "Eliminar cliente permanentemente (hard delete)",
            description = "Elimina físicamente un cliente de la base de datos. Esta operación es irreversible."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Cliente eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}/hard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> hardDeleteCustomer(
            @Parameter(description = "ID del cliente a eliminar permanentemente", required = true, example = "1")
            @PathVariable Long id) {

        log.warn("REST request to HARD DELETE Customer with ID: {}", id);

        return Mono.fromRunnable(() -> {
            customerService.hardDeleteCustomer(id);
            log.info("Customer hard deleted successfully");
        });
    }
}