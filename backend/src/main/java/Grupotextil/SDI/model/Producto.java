package Grupotextil.SDI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "El código es obligatorio")
    @Column(nullable = false, unique = true)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Integer stock;

    @Min(value = 0, message = "El stock mínimo debe ser mayor o igual a 0")
    private Integer stockMinimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private CategoriaProducto categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id")
    private UnidadMedida unidad;

    @NotBlank(message = "El tipo es obligatorio")
    @Column(nullable = false)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_producto_id")
    private TipoProducto tipoProducto;

    @Positive(message = "El precio debe ser mayor a 0")
    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    public CategoriaProducto getCategoria() { return categoria; }
    public void setCategoria(CategoriaProducto categoria) { this.categoria = categoria; }
    public UnidadMedida getUnidad() { return unidad; }
    public void setUnidad(UnidadMedida unidad) { this.unidad = unidad; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public TipoProducto getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(TipoProducto tipoProducto) { this.tipoProducto = tipoProducto; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
} 