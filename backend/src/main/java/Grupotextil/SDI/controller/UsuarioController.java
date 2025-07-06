package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.Usuario;
import Grupotextil.SDI.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Usuario> getById(@PathVariable UUID id) {
        return usuarioRepository.findById(id);
    }

    @PostMapping
    public Usuario create(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario update(@PathVariable UUID id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        usuarioRepository.deleteById(id);
    }
} 