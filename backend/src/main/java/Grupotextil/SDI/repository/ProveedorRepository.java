package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProveedorRepository extends JpaRepository<Proveedor, UUID> {
    
    // Buscar por RUC (para validar duplicados)
    Optional<Proveedor> findByRuc(String ruc);
    
    // Buscar por nombre (búsqueda parcial)
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por correo
    Optional<Proveedor> findByCorreo(String correo);
} 