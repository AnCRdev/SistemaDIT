package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.UnidadMedida;
import Grupotextil.SDI.repository.UnidadMedidaRepository;
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
@RequestMapping("/api/unidades-medida")
public class UnidadMedidaController {
    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @GetMapping
    public List<UnidadMedida> getAll() {
        return unidadMedidaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadMedida> getById(@PathVariable UUID id) {
        Optional<UnidadMedida> unidad = unidadMedidaRepository.findById(id);
        return unidad.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UnidadMedida unidadMedida) {
        Map<String, Object> response = new HashMap<>();
        
        // Validaciones según RF-01 (necesario para gestión de inventario)
        if (unidadMedida.getNombre() == null || unidadMedida.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre de la unidad de medida es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el nombre no esté duplicado
        if (unidadMedidaRepository.findByNombre(unidadMedida.getNombre()).isPresent()) {
            response.put("error", "Ya existe una unidad de medida con este nombre");
            return ResponseEntity.badRequest().body(response);
        }
        
        UnidadMedida savedUnidad = unidadMedidaRepository.save(unidadMedida);
        response.put("mensaje", "Unidad de medida registrada exitosamente");
        response.put("unidad", savedUnidad);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UnidadMedida unidadMedida) {
        Map<String, Object> response = new HashMap<>();
        
        if (!unidadMedidaRepository.existsById(id)) {
            response.put("error", "Unidad de medida no encontrada");
            return ResponseEntity.notFound().build();
        }
        
        // Validaciones similares a create
        if (unidadMedida.getNombre() == null || unidadMedida.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre de la unidad de medida es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el nombre no esté duplicado (excluyendo la unidad actual)
        Optional<UnidadMedida> existingUnidad = unidadMedidaRepository.findByNombre(unidadMedida.getNombre());
        if (existingUnidad.isPresent() && !existingUnidad.get().getId().equals(id)) {
            response.put("error", "Ya existe una unidad de medida con este nombre");
            return ResponseEntity.badRequest().body(response);
        }
        
        unidadMedida.setId(id);
        UnidadMedida updatedUnidad = unidadMedidaRepository.save(unidadMedida);
        response.put("mensaje", "Unidad de medida actualizada exitosamente");
        response.put("unidad", updatedUnidad);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!unidadMedidaRepository.existsById(id)) {
            response.put("error", "Unidad de medida no encontrada");
            return ResponseEntity.notFound().build();
        }
        
        // Verificar si la unidad tiene productos asociados (según RF-01)
        // TODO: Implementar verificación de productos cuando se cree la entidad Producto
        
        unidadMedidaRepository.deleteById(id);
        response.put("mensaje", "Unidad de medida eliminada exitosamente");
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar unidad por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<UnidadMedida> getByNombre(@PathVariable String nombre) {
        Optional<UnidadMedida> unidad = unidadMedidaRepository.findByNombre(nombre);
        return unidad.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para buscar unidades por símbolo
    @GetMapping("/simbolo/{simbolo}")
    public List<UnidadMedida> getBySimbolo(@PathVariable String simbolo) {
        return unidadMedidaRepository.findBySimbolo(simbolo);
    }
} 