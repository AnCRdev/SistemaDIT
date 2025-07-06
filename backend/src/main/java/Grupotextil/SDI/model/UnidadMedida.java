package Grupotextil.SDI.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "unidades_medida")
public class UnidadMedida {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String simbolo;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }
} 