package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, UUID> {
    
    // Buscar por nombre (para validar duplicados)
    Optional<CategoriaProducto> findByNombre(String nombre);
    
    // Buscar por nombre (búsqueda parcial)
    List<CategoriaProducto> findByNombreContainingIgnoreCase(String nombre);
} 