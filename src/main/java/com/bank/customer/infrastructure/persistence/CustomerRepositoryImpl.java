// src/main/java/com/bank/customer/infrastructure/persistence/CustomerRepositoryImpl.java
package com.bank.customer.infrastructure.persistence;

import com.bank.customer.domain.entities.Customer;
import com.bank.customer.domain.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA del CustomerRepository
 * JPA maneja herencia automáticamente
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerRepository;

    @Override
    @Transactional
    public Customer save(Customer customer) {
        log.info("Saving customer with identification: {}", customer.getIdentification());

        Customer saved = customerRepository.save(customer);

        log.info("Customer saved successfully with ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        log.debug("Finding customer by ID: {}", id);

        Optional<Customer> customer = customerRepository.findById(id);

        customer.ifPresentOrElse(
                c -> log.debug("Customer found: {}", c.getName()),
                () -> log.debug("Customer not found with ID: {}", id)
        );

        return customer;
    }

    @Override
    public List<Customer> findAll() {
        log.debug("Finding all customers");

        List<Customer> customers = customerRepository.findAll();

        log.debug("Found {} customers", customers.size());
        return customers;
    }

    @Override
    @Transactional
    public Customer update(@NotNull Customer customer) {
        log.info("Updating customer with ID: {}", customer.getId());

        Customer updated = customerRepository.save(customer);

        log.info("Customer updated successfully");
        return updated;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Deleting customer with ID: {}", id);

        customerRepository.deleteById(id);

        log.info("Customer deleted successfully");
    }

    @Override
    public boolean existsByIdentificacion(String identificacion) {
        return false;
    }

    @Override
    public Optional<Customer> findByIdentificacion(String identificacion) {
        return Optional.empty();
    }

    @Override
    public boolean existsByIdentification(String identification) {
        log.debug("Checking if customer exists with identification: {}", identification);
        return customerRepository.existsByIdentification(identification);
    }

    @Override
    public Optional<Customer> findByIdentification(String identification) {
        log.debug("Finding customer by identification: {}", identification);

        return customerRepository.findByIdentification(identification);
    }
}