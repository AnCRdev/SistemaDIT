package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.CategoriaProducto;
import Grupotextil.SDI.repository.CategoriaProductoRepository;
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
@RequestMapping("/api/categorias-productos")
public class CategoriaProductoController {
    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @GetMapping
    public List<CategoriaProducto> getAll() {
        return categoriaProductoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProducto> getById(@PathVariable UUID id) {
        Optional<CategoriaProducto> categoria = categoriaProductoRepository.findById(id);
        return categoria.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoriaProducto categoriaProducto) {
        Map<String, Object> response = new HashMap<>();
        
        // Validaciones según RF-01 (necesario para gestión de inventario)
        if (categoriaProducto.getNombre() == null || categoriaProducto.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre de la categoría es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el nombre no esté duplicado
        if (categoriaProductoRepository.findByNombre(categoriaProducto.getNombre()).isPresent()) {
            response.put("error", "Ya existe una categoría con este nombre");
            return ResponseEntity.badRequest().body(response);
        }
        
        CategoriaProducto savedCategoria = categoriaProductoRepository.save(categoriaProducto);
        response.put("mensaje", "Categoría registrada exitosamente");
        response.put("categoria", savedCategoria);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody CategoriaProducto categoriaProducto) {
        Map<String, Object> response = new HashMap<>();
        
        if (!categoriaProductoRepository.existsById(id)) {
            response.put("error", "Categoría no encontrada");
            return ResponseEntity.notFound().build();
        }
        
        // Validaciones similares a create
        if (categoriaProducto.getNombre() == null || categoriaProducto.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre de la categoría es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el nombre no esté duplicado (excluyendo la categoría actual)
        Optional<CategoriaProducto> existingCategoria = categoriaProductoRepository.findByNombre(categoriaProducto.getNombre());
        if (existingCategoria.isPresent() && !existingCategoria.get().getId().equals(id)) {
            response.put("error", "Ya existe una categoría con este nombre");
            return ResponseEntity.badRequest().body(response);
        }
        
        categoriaProducto.setId(id);
        CategoriaProducto updatedCategoria = categoriaProductoRepository.save(categoriaProducto);
        response.put("mensaje", "Categoría actualizada exitosamente");
        response.put("categoria", updatedCategoria);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!categoriaProductoRepository.existsById(id)) {
            response.put("error", "Categoría no encontrada");
            return ResponseEntity.notFound().build();
        }
        
        // Verificar si la categoría tiene productos asociados (según RF-01)
        // TODO: Implementar verificación de productos cuando se cree la entidad Producto
        
        categoriaProductoRepository.deleteById(id);
        response.put("mensaje", "Categoría eliminada exitosamente");
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar categoría por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CategoriaProducto> getByNombre(@PathVariable String nombre) {
        Optional<CategoriaProducto> categoria = categoriaProductoRepository.findByNombre(nombre);
        return categoria.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
} 