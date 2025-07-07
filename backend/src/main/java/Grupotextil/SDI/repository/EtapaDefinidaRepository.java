package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.EtapaDefinida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EtapaDefinidaRepository extends JpaRepository<EtapaDefinida, UUID> {
    
    // Buscar por nombre
    Optional<EtapaDefinida> findByNombre(String nombre);
    
    // Verificar si existe por nombre
    boolean existsByNombre(String nombre);
} 