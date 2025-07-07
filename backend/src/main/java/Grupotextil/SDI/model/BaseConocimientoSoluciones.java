package Grupotextil.SDI.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "base_conocimiento_soluciones")
public class BaseConocimientoSoluciones {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tipo_error", nullable = false)
    private String tipoError;

    @Column(nullable = false)
    private String causa;

    @Column(nullable = false)
    private String solucion;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTipoError() { return tipoError; }
    public void setTipoError(String tipoError) { this.tipoError = tipoError; }
    public String getCausa() { return causa; }
    public void setCausa(String causa) { this.causa = causa; }
    public String getSolucion() { return solucion; }
    public void setSolucion(String solucion) { this.solucion = solucion; }
} 