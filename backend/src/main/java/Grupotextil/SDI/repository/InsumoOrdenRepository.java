package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.InsumoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsumoOrdenRepository extends JpaRepository<InsumoOrden, UUID> {
    
    // Buscar insumos por orden de producción
    List<InsumoOrden> findByOrdenId(UUID ordenId);
    
    // Buscar insumos por producto (materia prima)
    List<InsumoOrden> findByProductoId(UUID productoId);
} 