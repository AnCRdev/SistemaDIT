package Grupotextil.SDI.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ordenes_produccion")
public class OrdenProduccion {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsable_id")
    private Usuario responsable;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(nullable = false)
    @Convert(converter = EstadoOrdenConverter.class)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    // Relación con etapas asignadas
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<EtapaAsignada> etapasAsignadas;

    // Relación con insumos utilizados
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<InsumoOrden> insumos;

    // Constructores
    public OrdenProduccion() {}

    public OrdenProduccion(Producto producto, Integer cantidad, Usuario responsable) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.responsable = responsable;
        this.estado = EstadoOrden.PENDIENTE;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public List<EtapaAsignada> getEtapasAsignadas() {
        return etapasAsignadas;
    }

    public void setEtapasAsignadas(List<EtapaAsignada> etapasAsignadas) {
        this.etapasAsignadas = etapasAsignadas;
    }

    public List<InsumoOrden> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<InsumoOrden> insumos) {
        this.insumos = insumos;
    }

    // Enum para estados
    public enum EstadoOrden {
        PENDIENTE("Pendiente"),
        EN_PROCESO("En Proceso"),
        FINALIZADO("Finalizado");

        private final String valor;

        EstadoOrden(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }
} 