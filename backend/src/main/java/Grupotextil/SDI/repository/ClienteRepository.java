package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    
    // Buscar por documento (para validar duplicados y búsquedas)
    Optional<Cliente> findByDocumento(String documento);
    
    // Buscar por nombre (búsqueda parcial)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por correo
    Optional<Cliente> findByCorreo(String correo);
} 