package com.bank.customer.infrastructure.persistence;

import com.bank.customer.domain.entities.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Aggregate para mapear resultado de JOIN entre personas y clientes.
 * Esta clase es solo para infraestructura (queries R2DBC).
 * NO es parte del dominio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAggregate {

    // Campos de Persona
    private Long id;
    private String nombre;
    private String genero;
    private String identificacion;
    private String direccion;
    private String telefono;
    private LocalDateTime personaCreatedAt;
    private LocalDateTime personaUpdatedAt;

    // Campos de Cliente
    private String contrasena;
    private Boolean estado;
    private LocalDateTime clienteCreatedAt;
    private LocalDateTime clienteUpdatedAt;

    /**
     * Convierte el aggregate a la entidad Cliente del dominio
     */

    public Cliente toCliente() {
        Cliente cliente = new Cliente();

        // Campos de Persona (heredados)
        cliente.setId(this.id);
        cliente.setNombre(this.nombre);
        cliente.setGenero(this.genero);
        cliente.setIdentificacion(this.identificacion);
        cliente.setDireccion(this.direccion);
        cliente.setTelefono(this.telefono);
        cliente.setCreatedAt(this.personaCreatedAt);
        cliente.setUpdatedAt(this.personaUpdatedAt);

        // Campos propios de Cliente
        cliente.setContrasena(this.contrasena);
        cliente.setEstado(this.estado);
        cliente.setClientCreatedAt(this.clienteCreatedAt);
        cliente.setClientUpdatedAt(this.clienteUpdatedAt);

        return cliente;
    }
}