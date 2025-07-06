package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TipoProductoRepository extends JpaRepository<TipoProducto, UUID> {
    
    // Buscar por nombre (para validar duplicados)
    Optional<TipoProducto> findByNombre(String nombre);
    
    // Buscar por descripción (búsqueda parcial)
    List<TipoProducto> findByDescripcionContainingIgnoreCase(String descripcion);
} 