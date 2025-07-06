package Grupotextil.SDI.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "categorias_productos")
public class CategoriaProducto {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
} 