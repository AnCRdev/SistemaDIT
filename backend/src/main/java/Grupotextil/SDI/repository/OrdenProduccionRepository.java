package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.OrdenProduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrdenProduccionRepository extends JpaRepository<OrdenProduccion, UUID> {
    
    // Buscar por estado
    List<OrdenProduccion> findByEstado(OrdenProduccion.EstadoOrden estado);
    
    // Buscar por producto
    List<OrdenProduccion> findByProductoId(UUID productoId);
    
    // Buscar por rango de fechas
    @Query("SELECT o FROM OrdenProduccion o WHERE o.fechaInicio BETWEEN :fechaInicio AND :fechaFin")
    List<OrdenProduccion> findByFechaInicioBetween(@Param("fechaInicio") LocalDate fechaInicio, 
                                                   @Param("fechaFin") LocalDate fechaFin);
    
    // Buscar órdenes pendientes
    @Query("SELECT o FROM OrdenProduccion o WHERE o.estado = 'PENDIENTE' ORDER BY o.fechaInicio ASC")
    List<OrdenProduccion> findOrdenesPendientes();
    
    // Buscar órdenes en proceso
    @Query("SELECT o FROM OrdenProduccion o WHERE o.estado = 'EN_PROCESO' ORDER BY o.fechaInicio ASC")
    List<OrdenProduccion> findOrdenesEnProceso();
} 