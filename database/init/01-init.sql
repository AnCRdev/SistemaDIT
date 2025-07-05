-- Script de inicialización de la base de datos
-- Este archivo se ejecutará automáticamente cuando se inicie el contenedor PostgreSQL

-- Crear la base de datos si no existe
-- (PostgreSQL ya la crea automáticamente con las variables de entorno)

-- Crear tablas de ejemplo (opcional)
-- Spring Boot con JPA creará las tablas automáticamente con ddl-auto=update

-- Ejemplo de tabla de usuarios (si quieres crear tablas manualmente)
/*
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo
INSERT INTO usuarios (nombre, email, password) VALUES 
    ('Admin', 'admin@sistema.com', 'password123'),
    ('Usuario', 'usuario@sistema.com', 'password123')
    ON CONFLICT (email) DO NOTHING;
*/

-- Comentario: Spring Boot JPA creará automáticamente las tablas basadas en las entidades 