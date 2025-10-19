-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS customer_db;
USE customer_db;

-- Tabla personas (entidad base)
CREATE TABLE IF NOT EXISTS personas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20),
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla clientes (hereda de personas)
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT PRIMARY KEY,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id) REFERENCES personas(id) ON DELETE CASCADE
);

-- Índices para optimización (sin IF NOT EXISTS)
-- El UNIQUE en identificacion ya crea un índice automáticamente
-- Solo agregamos índice para clientes.estado si es necesario
-- CREATE INDEX IF NOT EXISTS idx_persona_identificacion ON personas(identificacion);
-- CREATE INDEX IF NOT EXISTS idx_cliente_estado ON clientes(estado);