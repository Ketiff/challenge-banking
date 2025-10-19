// src/main/java/com/bank/customer/infrastructure/config/R2dbcConfig.java
package com.bank.customer.infrastructure.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

/**
 * Configuración de R2DBC para acceso reactivo a MySQL.
 * Spring Boot auto-configura el ConnectionFactory desde application.yml
 */
@Configuration
@EnableR2dbcRepositories(basePackages = "com.bank.customer.infrastructure.persistence")
public class R2dbcConfig {

    /**
     * Transaction manager para operaciones reactivas.
     * El ConnectionFactory se inyecta automáticamente por Spring Boot
     */

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}