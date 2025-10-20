package com.bank.customer.application.services;

import com.bank.customer.application.dto.CustomerDTO;
import com.bank.customer.application.dto.CreateCustomerRequest;
import com.bank.customer.application.dto.UpdateCustomerRequest;

import java.util.List;

/**
 * Service interface para operaciones de Customer.
 * Define los casos de uso de la aplicación.
 */
public interface CustomerService {

    /**
     * Crea un nuevo cliente
     */
    CustomerDTO createCustomer(CreateCustomerRequest request);

    /**
     * Busca un cliente por ID
     */
    CustomerDTO findCustomerById(Long id);

    /**
     * Busca un cliente por identificación (cédula)
     */
    CustomerDTO findCustomerByIdentification(String identification);

    /**
     * Lista todos los clientes
     */
    List<CustomerDTO> findAllCustomers();

    /**
     * Actualiza un cliente existente
     */
    CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request);

    /**
     * Elimina un cliente (soft delete - desactiva)
     */
    void deleteCustomer(Long id);

    /**
     * Elimina físicamente un cliente de la base de datos
     */
    void hardDeleteCustomer(Long id);
}