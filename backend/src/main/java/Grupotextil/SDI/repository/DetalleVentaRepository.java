package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, UUID> {
    
    // Buscar detalles por venta
    List<DetalleVenta> findByVentaId(UUID ventaId);
    
    // Buscar detalles por producto
    List<DetalleVenta> findByProductoId(UUID productoId);
    
    // Contar ventas de un producto
    @Query("SELECT COUNT(d) FROM DetalleVenta d WHERE d.producto.id = :productoId")
    Long countByProductoId(@Param("productoId") UUID productoId);
    
    // Sumar cantidad vendida de un producto
    @Query("SELECT COALESCE(SUM(d.cantidad), 0) FROM DetalleVenta d WHERE d.producto.id = :productoId")
    Integer sumCantidadByProductoId(@Param("productoId") UUID productoId);
    
    // Sumar total vendido de un producto
    @Query("SELECT COALESCE(SUM(d.subtotal), 0) FROM DetalleVenta d WHERE d.producto.id = :productoId")
    Double sumSubtotalByProductoId(@Param("productoId") UUID productoId);
} 