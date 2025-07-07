package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.EtapaAsignada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EtapaAsignadaRepository extends JpaRepository<EtapaAsignada, UUID> {
    
    // Buscar etapas por orden de producción
    List<EtapaAsignada> findByOrdenId(UUID ordenId);
    
    // Buscar etapas por estado
    List<EtapaAsignada> findByEstado(EtapaAsignada.EstadoEtapa estado);
    
    // Buscar etapas pendientes de una orden
    @Query("SELECT ea FROM EtapaAsignada ea WHERE ea.orden.id = :ordenId AND ea.estado = 'PENDIENTE' ORDER BY ea.etapa.nombre")
    List<EtapaAsignada> findEtapasPendientesByOrden(@Param("ordenId") UUID ordenId);
    
    // Buscar etapas en proceso de una orden
    @Query("SELECT ea FROM EtapaAsignada ea WHERE ea.orden.id = :ordenId AND ea.estado = 'EN_PROCESO' ORDER BY ea.etapa.nombre")
    List<EtapaAsignada> findEtapasEnProcesoByOrden(@Param("ordenId") UUID ordenId);
    
    // Buscar etapas finalizadas de una orden
    @Query("SELECT ea FROM EtapaAsignada ea WHERE ea.orden.id = :ordenId AND ea.estado = 'FINALIZADO' ORDER BY ea.etapa.nombre")
    List<EtapaAsignada> findEtapasFinalizadasByOrden(@Param("ordenId") UUID ordenId);
} 