package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.Usuario;
import Grupotextil.SDI.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable UUID id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        
        // Validaciones obligatorias
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre del usuario es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            response.put("error", "El correo del usuario es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            response.put("error", "La contraseña del usuario es obligatoria");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            response.put("error", "El rol del usuario es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Validar formato de correo básico
        if (!usuario.getCorreo().contains("@")) {
            response.put("error", "El formato del correo no es válido");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Validar rol válido
        String[] rolesValidos = {"Administrador", "Gestor de Inventario", "Gerente", "Vendedor"};
        boolean rolValido = false;
        for (String rol : rolesValidos) {
            if (rol.equals(usuario.getRol())) {
                rolValido = true;
                break;
            }
        }
        if (!rolValido) {
            response.put("error", "El rol debe ser: Administrador, Gestor de Inventario, Gerente o Vendedor");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el correo no esté duplicado
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            response.put("error", "Ya existe un usuario con este correo");
            return ResponseEntity.badRequest().body(response);
        }
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        response.put("mensaje", "Usuario registrado exitosamente");
        response.put("usuario", savedUsuario);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();
        
        if (!usuarioRepository.existsById(id)) {
            response.put("error", "Usuario no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Validaciones similares a create
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre del usuario es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            response.put("error", "El correo del usuario es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            response.put("error", "La contraseña del usuario es obligatoria");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            response.put("error", "El rol del usuario es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Validar formato de correo básico
        if (!usuario.getCorreo().contains("@")) {
            response.put("error", "El formato del correo no es válido");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Validar rol válido
        String[] rolesValidos = {"Administrador", "Gestor de Inventario", "Gerente", "Vendedor"};
        boolean rolValido = false;
        for (String rol : rolesValidos) {
            if (rol.equals(usuario.getRol())) {
                rolValido = true;
                break;
            }
        }
        if (!rolValido) {
            response.put("error", "El rol debe ser: Administrador, Gestor de Inventario, Gerente o Vendedor");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el correo no esté duplicado (excluyendo el usuario actual)
        Optional<Usuario> existingUsuario = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (existingUsuario.isPresent() && !existingUsuario.get().getId().equals(id)) {
            response.put("error", "Ya existe un usuario con este correo");
            return ResponseEntity.badRequest().body(response);
        }
        
        usuario.setId(id);
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        response.put("mensaje", "Usuario actualizado exitosamente");
        response.put("usuario", updatedUsuario);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!usuarioRepository.existsById(id)) {
            response.put("error", "Usuario no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        usuarioRepository.deleteById(id);
        response.put("mensaje", "Usuario eliminado exitosamente");
        
        return ResponseEntity.ok(response);
    }
    
    // Endpoint para buscar usuario por correo
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> getByCorreo(@PathVariable String correo) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    // Endpoint para buscar usuarios por rol
    @GetMapping("/rol/{rol}")
    public List<Usuario> getByRol(@PathVariable String rol) {
        return usuarioRepository.findByRol(rol);
    }
} 