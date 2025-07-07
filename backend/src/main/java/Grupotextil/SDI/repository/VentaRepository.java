package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VentaRepository extends JpaRepository<Venta, UUID> {
    
    // Buscar ventas por cliente
    List<Venta> findByClienteId(UUID clienteId);
    
    // Buscar ventas por usuario
    List<Venta> findByUsuarioId(UUID usuarioId);
    
    // Buscar ventas por rango de fechas
    @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fecha DESC")
    List<Venta> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                   @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar ventas del día actual
    @Query("SELECT v FROM Venta v WHERE CAST(v.fecha AS date) = CURRENT_DATE ORDER BY v.fecha DESC")
    List<Venta> findVentasHoy();
    
    // Contar ventas por rango de fechas
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    Long countByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                            @Param("fechaFin") LocalDateTime fechaFin);
    
    // Sumar total de ventas por rango de fechas
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    Double sumTotalByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                 @Param("fechaFin") LocalDateTime fechaFin);
} 