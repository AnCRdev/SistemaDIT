package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Buscar por correo (para validar duplicados)
    Optional<Usuario> findByCorreo(String correo);
    
    // Buscar usuarios por rol
    List<Usuario> findByRol(String rol);
} 