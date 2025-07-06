package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, UUID> {
    
    // Buscar por nombre (para validar duplicados)
    Optional<UnidadMedida> findByNombre(String nombre);
    
    // Buscar por símbolo
    List<UnidadMedida> findBySimbolo(String simbolo);
    
    // Buscar por nombre (búsqueda parcial)
    List<UnidadMedida> findByNombreContainingIgnoreCase(String nombre);
} 