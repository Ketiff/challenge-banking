package com.bank.customer.application.services;

import com.bank.customer.application.dto.CreateCustomerRequest;
import com.bank.customer.application.dto.CustomerDTO;
import com.bank.customer.application.dto.UpdateCustomerRequest;
import com.bank.customer.domain.entities.Customer;
import com.bank.customer.domain.exceptions.CustomerExceptions.CustomerAlreadyExistsException;
import com.bank.customer.domain.exceptions.CustomerExceptions.CustomerNotFoundException;
import com.bank.customer.domain.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CustomerServiceImpl
 *
 * Tecnologías:
 * - JUnit 5 (testing framework)
 * - Mockito (mocking dependencies)
 * - AssertJ (assertions - opcional, usamos JUnit assertions)
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service - Unit Tests")
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer sampleCustomer;
    private CreateCustomerRequest createRequest;
    private UpdateCustomerRequest updateRequest;

    /**
     * Setup ejecutado antes de cada test
     * Prepara datos de prueba reutilizables
     */

    @BeforeEach
    void setUp() {
        // Crear customer de ejemplo
        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);
        sampleCustomer.setName("John Doe");
        sampleCustomer.setGender("Male");
        sampleCustomer.setIdentification("1234567890");
        sampleCustomer.setAddress("123 Main St");
        sampleCustomer.setPhone("0999999999");
        sampleCustomer.setPassword("hashedPassword123");
        sampleCustomer.setStatus(true);
        sampleCustomer.setCreatedAt(LocalDateTime.now());
        sampleCustomer.setUpdatedAt(LocalDateTime.now());

        // Crear request de ejemplo
        createRequest = CreateCustomerRequest.builder()
                .name("John Doe")
                .gender("Male")
                .identification("1234567890")
                .address("123 Main St")
                .phone("0999999999")
                .password("password123")
                .build();

        // Crear update request de ejemplo
        updateRequest = UpdateCustomerRequest.builder()
                .phone("0988888888")
                .address("456 New Address")
                .build();
    }

    // ========== TEST 1: CREATE CUSTOMER - SUCCESS ==========

    @Test
    @DisplayName("Should create customer successfully with valid data")
    void createCustomer_WithValidData_ShouldReturnCustomerDTO() {
        // Given (Preparación)
        when(customerRepository.existsByIdentification(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(sampleCustomer);

        // When (Ejecución)
        CustomerDTO result = customerService.createCustomer(createRequest);

        // Then (Verificación)
        assertNotNull(result, "Result should not be null");
        assertEquals(1L, result.getId(), "ID should match");
        assertEquals("John Doe", result.getName(), "Name should match");
        assertEquals("1234567890", result.getIdentification(), "Identification should match");
        assertTrue(result.getStatus(), "Status should be true");
        assertNull(result.getClass().getDeclaredFields().length > 0
                ? null : "password", "Password should NOT be in DTO");

        // Verificar que se llamaron los métodos correctos
        verify(customerRepository, times(1)).existsByIdentification("1234567890");
        verify(customerRepository, times(1)).save(any(Customer.class));
        verifyNoMoreInteractions(customerRepository);
    }

    // ========== TEST 2: CREATE CUSTOMER - ALREADY EXISTS ==========

    @Test
    @DisplayName("Should throw exception when customer with same identification already exists")
    void createCustomer_WithDuplicateIdentification_ShouldThrowException() {
        // Given
        when(customerRepository.existsByIdentification(anyString())).thenReturn(true);

        // When & Then
        CustomerAlreadyExistsException exception = assertThrows(
                CustomerAlreadyExistsException.class,
                () -> customerService.createCustomer(createRequest),
                "Should throw CustomerAlreadyExistsException"
        );

        // Verificar mensaje de error
        assertTrue(exception.getMessage().contains("1234567890"),
                "Exception message should contain identification");
        assertTrue(exception.getMessage().contains("already exists"),
                "Exception message should mention 'already exists'");

        // Verificar que save() NUNCA se llamó
        verify(customerRepository, times(1)).existsByIdentification("1234567890");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    // ========== TEST 3: FIND CUSTOMER BY ID - NOT FOUND ==========

    @Test
    @DisplayName("Should throw exception when customer not found by ID")
    void findCustomerById_WithNonExistentId_ShouldThrowException() {
        // Given
        Long nonExistentId = 999L;
        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.findCustomerById(nonExistentId),
                "Should throw CustomerNotFoundException"
        );

        // Verificar mensaje
        assertTrue(exception.getMessage().contains("999"),
                "Exception message should contain the ID");
        assertTrue(exception.getMessage().contains("not found"),
                "Exception message should mention 'not found'");

        verify(customerRepository, times(1)).findById(nonExistentId);
    }

    // ========== TEST 4: FIND CUSTOMER BY ID - SUCCESS ==========

    @Test
    @DisplayName("Should return customer DTO when found by ID")
    void findCustomerById_WithExistingId_ShouldReturnCustomerDTO() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));

        // When
        CustomerDTO result = customerService.findCustomerById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("1234567890", result.getIdentification());

        verify(customerRepository, times(1)).findById(1L);
    }

    // ========== TEST 5: FIND ALL CUSTOMERS ==========

    @Test
    @DisplayName("Should return list of customer DTOs")
    void findAllCustomers_ShouldReturnListOfCustomerDTOs() {
        // Given
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");
        customer2.setIdentification("0987654321");
        customer2.setStatus(true);

        List<Customer> customers = Arrays.asList(sampleCustomer, customer2);
        when(customerRepository.findAll()).thenReturn(customers);

        // When
        List<CustomerDTO> result = customerService.findAllCustomers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size(), "Should return 2 customers");
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());

        verify(customerRepository, times(1)).findAll();
    }

    // ========== TEST 6: UPDATE CUSTOMER - PARTIAL UPDATE ==========

    @Test
    @DisplayName("Should update only provided fields (partial update)")
    void updateCustomer_WithPartialData_ShouldUpdateOnlyProvidedFields() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(customerRepository.update(any(Customer.class))).thenReturn(sampleCustomer);

        // When
        CustomerDTO result = customerService.updateCustomer(1L, updateRequest);

        // Then
        assertNotNull(result);

        // Verificar que el método update fue llamado
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).update(argThat(customer ->
                customer.getPhone().equals("0988888888") &&
                        customer.getAddress().equals("456 New Address")
        ));
    }

    // ========== TEST 7: UPDATE CUSTOMER - NOT FOUND ==========

    @Test
    @DisplayName("Should throw exception when updating non-existent customer")
    void updateCustomer_WithNonExistentId_ShouldThrowException() {
        // Given
        Long nonExistentId = 999L;
        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        CustomerNotFoundException exception = assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.updateCustomer(nonExistentId, updateRequest),
                "Should throw CustomerNotFoundException"
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(customerRepository, times(1)).findById(nonExistentId);
        verify(customerRepository, never()).update(any(Customer.class));
    }

    // ========== TEST 8: DELETE CUSTOMER (SOFT DELETE) ==========

    @Test
    @DisplayName("Should deactivate customer (soft delete)")
    void deleteCustomer_ShouldDeactivateCustomer() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(customerRepository.update(any(Customer.class))).thenReturn(sampleCustomer);

        // When
        assertDoesNotThrow(() -> customerService.deleteCustomer(1L));

        // Then
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).update(argThat(customer ->
                customer.getStatus() == false
        ));
    }
}