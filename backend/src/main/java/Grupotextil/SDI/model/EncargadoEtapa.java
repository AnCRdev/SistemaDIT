package Grupotextil.SDI.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "encargados_etapas")
public class EncargadoEtapa {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "La etapa asignada es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etapa_asignada_id", nullable = false)
    @JsonBackReference
    private EtapaAsignada etapaAsignada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion = LocalDate.now();

    // Constructores
    public EncargadoEtapa() {}

    public EncargadoEtapa(EtapaAsignada etapaAsignada, Usuario usuario) {
        this.etapaAsignada = etapaAsignada;
        this.usuario = usuario;
        this.fechaAsignacion = LocalDate.now();
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EtapaAsignada getEtapaAsignada() {
        return etapaAsignada;
    }

    public void setEtapaAsignada(EtapaAsignada etapaAsignada) {
        this.etapaAsignada = etapaAsignada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
} 