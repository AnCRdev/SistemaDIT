package Grupotextil.SDI.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Duration;
import java.util.UUID;

@Entity
@Table(name = "etapas_asignadas")
public class EtapaAsignada {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "La orden de producción es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    @JsonBackReference
    private OrdenProduccion orden;

    @NotNull(message = "La etapa es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "etapa_id", nullable = false)
    private EtapaDefinida etapa;

    @Column(nullable = false)
    @Convert(converter = EstadoEtapaConverter.class)
    private EstadoEtapa estado = EstadoEtapa.PENDIENTE;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "tiempo_estimado", nullable = true)
    @Convert(converter = DurationConverter.class)
    private Duration tiempoEstimado;

    @Column(name = "tiempo_real", nullable = true)
    @Convert(converter = DurationConverter.class)
    private Duration tiempoReal;

    // Constructores
    public EtapaAsignada() {}

    public EtapaAsignada(OrdenProduccion orden, EtapaDefinida etapa) {
        this.orden = orden;
        this.etapa = etapa;
        this.estado = EstadoEtapa.PENDIENTE;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrdenProduccion getOrden() {
        return orden;
    }

    public void setOrden(OrdenProduccion orden) {
        this.orden = orden;
    }

    public EtapaDefinida getEtapa() {
        return etapa;
    }

    public void setEtapa(EtapaDefinida etapa) {
        this.etapa = etapa;
    }

    public EstadoEtapa getEstado() {
        return estado;
    }

    public void setEstado(EstadoEtapa estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Duration getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Duration tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public Duration getTiempoReal() {
        return tiempoReal;
    }

    public void setTiempoReal(Duration tiempoReal) {
        this.tiempoReal = tiempoReal;
    }

    // Enum para estados de etapa
    public enum EstadoEtapa {
        PENDIENTE("Pendiente"),
        EN_PROCESO("En Proceso"),
        FINALIZADO("Finalizado"),
        EN_CONFLICTO("En Conflicto");

        private final String valor;

        EstadoEtapa(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }
} 