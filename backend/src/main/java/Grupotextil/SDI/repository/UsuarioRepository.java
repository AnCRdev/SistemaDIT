package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Métodos personalizados si los necesitas
} 