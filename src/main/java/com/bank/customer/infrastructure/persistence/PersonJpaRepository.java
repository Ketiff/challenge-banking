// src/main/java/com/bank/customer/infrastructure/persistence/PersonJpaRepository.java
package com.bank.customer.infrastructure.persistence;

import com.bank.customer.domain.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository para Person
 * Spring Data genera implementación automáticamente
 */
@Repository
public interface PersonJpaRepository extends JpaRepository<Person, Long> {

    /**
     * Busca persona por identificación
     * Query generado automáticamente: SELECT * FROM personas WHERE identificacion = ?
     */
    Optional<Person> findByIdentification(String identification);

    /**
     * Verifica si existe persona con esa identificación
     * Query generado automáticamente: SELECT COUNT(*) FROM personas WHERE identificacion = ?
     */
    boolean existsByIdentification(String identification);
}