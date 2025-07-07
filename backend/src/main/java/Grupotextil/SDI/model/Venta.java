package Grupotextil.SDI.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Positive(message = "El total debe ser mayor a 0")
    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    // Relación con detalles de venta
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DetalleVenta> detalles;

    // Constructores
    public Venta() {
        this.fecha = LocalDateTime.now();
    }

    public Venta(Cliente cliente, Usuario usuario) {
        this();
        this.cliente = cliente;
        this.usuario = usuario;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    // Método para calcular el total
    public void calcularTotal() {
        if (detalles != null && !detalles.isEmpty()) {
            this.total = detalles.stream()
                    .map(detalle -> detalle.getSubtotal())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.total = BigDecimal.ZERO;
        }
    }
} 