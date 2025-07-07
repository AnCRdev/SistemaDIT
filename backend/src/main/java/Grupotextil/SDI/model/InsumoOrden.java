package Grupotextil.SDI.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "insumos_orden")
public class InsumoOrden {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "La orden de producción es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    @JsonBackReference
    private OrdenProduccion orden;

    @NotNull(message = "El producto (materia prima) es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "La cantidad utilizada es obligatoria")
    @Positive(message = "La cantidad utilizada debe ser mayor a 0")
    @Column(name = "cantidad_utilizada", nullable = false)
    private BigDecimal cantidadUtilizada;

    // Constructores
    public InsumoOrden() {}

    public InsumoOrden(OrdenProduccion orden, Producto producto, BigDecimal cantidadUtilizada) {
        this.orden = orden;
        this.producto = producto;
        this.cantidadUtilizada = cantidadUtilizada;
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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(BigDecimal cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
    }
} 