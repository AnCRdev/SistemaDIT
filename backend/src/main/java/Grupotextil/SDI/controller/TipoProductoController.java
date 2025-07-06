package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.TipoProducto;
import Grupotextil.SDI.repository.TipoProductoRepository;
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
@RequestMapping("/api/tipos-producto")
public class TipoProductoController {
    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @GetMapping
    public List<TipoProducto> getAll() {
        return tipoProductoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoProducto> getById(@PathVariable UUID id) {
        Optional<TipoProducto> tipo = tipoProductoRepository.findById(id);
        return tipo.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TipoProducto tipoProducto) {
        Map<String, Object> response = new HashMap<>();
        
        // Validaciones según RF-04 (necesario para órdenes de producción)
        if (tipoProducto.getNombre() == null || tipoProducto.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre del tipo de producto es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el nombre no esté duplicado
        if (tipoProductoRepository.findByNombre(tipoProducto.getNombre()).isPresent()) {
            response.put("error", "Ya existe un tipo de producto con este nombre");
            return ResponseEntity.badRequest().body(response);
        }
        
        TipoProducto savedTipo = tipoProductoRepository.save(tipoProducto);
        response.put("mensaje", "Tipo de producto registrado exitosamente");
        response.put("tipo", savedTipo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody TipoProducto tipoProducto) {
        Map<String, Object> response = new HashMap<>();
        
        if (!tipoProductoRepository.existsById(id)) {
            response.put("error", "Tipo de producto no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Validaciones similares a create
        if (tipoProducto.getNombre() == null || tipoProducto.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre del tipo de producto es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el nombre no esté duplicado (excluyendo el tipo actual)
        Optional<TipoProducto> existingTipo = tipoProductoRepository.findByNombre(tipoProducto.getNombre());
        if (existingTipo.isPresent() && !existingTipo.get().getId().equals(id)) {
            response.put("error", "Ya existe un tipo de producto con este nombre");
            return ResponseEntity.badRequest().body(response);
        }
        
        tipoProducto.setId(id);
        TipoProducto updatedTipo = tipoProductoRepository.save(tipoProducto);
        response.put("mensaje", "Tipo de producto actualizado exitosamente");
        response.put("tipo", updatedTipo);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!tipoProductoRepository.existsById(id)) {
            response.put("error", "Tipo de producto no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Verificar si el tipo tiene productos asociados (según RF-04)
        // TODO: Implementar verificación de productos cuando se cree la entidad Producto
        
        tipoProductoRepository.deleteById(id);
        response.put("mensaje", "Tipo de producto eliminado exitosamente");
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar tipo por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<TipoProducto> getByNombre(@PathVariable String nombre) {
        Optional<TipoProducto> tipo = tipoProductoRepository.findByNombre(nombre);
        return tipo.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para buscar tipos por descripción
    @GetMapping("/descripcion/{descripcion}")
    public List<TipoProducto> getByDescripcionContaining(@PathVariable String descripcion) {
        return tipoProductoRepository.findByDescripcionContainingIgnoreCase(descripcion);
    }
} 