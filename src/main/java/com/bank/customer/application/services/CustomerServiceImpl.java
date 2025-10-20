package com.bank.customer.application.services;

import com.bank.customer.application.dto.CustomerDTO;
import com.bank.customer.application.dto.CreateCustomerRequest;
import com.bank.customer.application.dto.UpdateCustomerRequest;
import com.bank.customer.domain.entities.Customer;
import com.bank.customer.domain.exceptions.CustomerExceptions.*;
import com.bank.customer.domain.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Customer con JPA.
 * Código imperativo simple en vez de reactivo.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        log.info("Creating new customer with identification: {}", request.getIdentification());

        // Validación: Cliente no debe existir previamente
        if (customerRepository.existsByIdentification(request.getIdentification())) {
            log.warn("Customer already exists with identification: {}", request.getIdentification());
            throw new CustomerAlreadyExistsException(
                    "Customer with identification " + request.getIdentification() + " already exists"
            );
        }

        // Crear entidad Customer a partir del request
        Customer customer = mapToEntity(request);

        // Guardar en repositorio
        Customer saved = customerRepository.save(customer);

        log.info("Customer created successfully with ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    @Override
    public CustomerDTO findCustomerById(Long id) {
        log.debug("Finding customer by ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + id
                ));

        log.debug("Customer found: {}", customer.getName());
        return mapToDTO(customer);
    }

    @Override
    public CustomerDTO findCustomerByIdentification(String identification) {
        return null;
    }


    @Override
    public List<CustomerDTO> findAllCustomers() {
        log.debug("Finding all customers");

        List<Customer> customers = customerRepository.findAll();

        log.debug("Found {} customers", customers.size());
        return customers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer with ID: {}", id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + id
                ));

        // Actualizar solo los campos que vienen en el request
        updateEntityFromRequest(existingCustomer, request);

        Customer updated = customerRepository.update(existingCustomer);

        log.info("Customer updated successfully");
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deactivating customer with ID: {}", id);

        // Soft delete: solo desactivamos el cliente
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found with ID: " + id
                ));

        customer.deactivate(); // Método del dominio
        customerRepository.update(customer);

        log.info("Customer deactivated successfully");
    }

    @Override
    @Transactional
    public void hardDeleteCustomer(Long id) {
        log.warn("Hard deleting customer with ID: {}", id);

        // Hard delete: eliminación física
        if (!customerRepository.findById(id).isPresent()) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }

        customerRepository.deleteById(id);

        log.info("Customer hard deleted successfully");
    }

    /**
     * Mapea CreateCustomerRequest a entidad Customer
     */
    private Customer mapToEntity(CreateCustomerRequest request) {
        Customer customer = new Customer();

        // Campos de Person (heredados)
        customer.setName(request.getName());
        customer.setGender(request.getGender());
        customer.setIdentification(request.getIdentification());
        customer.setAddress(request.getAddress());
        customer.setPhone(request.getPhone());

        // Campos propios de Customer
        customer.setPassword(request.getPassword()); // TODO: Encriptar en producción
        customer.setStatus(true); // Por defecto activo

        return customer;
    }

    /**
     * Mapea entidad Customer a DTO
     */
    private CustomerDTO mapToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .gender(customer.getGender())
                .identification(customer.getIdentification())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    private void updateEntityFromRequest(Customer customer, UpdateCustomerRequest request) {
        if (request.getName() != null) {
            customer.setName(request.getName());
        }
        if (request.getGender() != null) {
            customer.setGender(request.getGender());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }
        if (request.getPassword() != null) {
            customer.setPassword(request.getPassword()); // TODO: Encriptar
        }
        if (request.getStatus() != null) {
            customer.setStatus(request.getStatus());
        }

        // updatedAt se actualiza automáticamente con @PreUpdate
    }
}