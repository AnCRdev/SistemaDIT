package Grupotextil.SDI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "etapas_definidas")
public class EtapaDefinida {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "El nombre de la etapa es obligatorio")
    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Constructores
    public EtapaDefinida() {}

    public EtapaDefinida(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
} 