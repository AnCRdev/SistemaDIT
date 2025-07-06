package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.Proveedor;
import Grupotextil.SDI.repository.ProveedorRepository;
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
@RequestMapping("/api/proveedores")
public class ProveedorController {
    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public List<Proveedor> getAll() {
        return proveedorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getById(@PathVariable UUID id) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(id);
        return proveedor.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Proveedor proveedor) {
        Map<String, Object> response = new HashMap<>();
        
        // Validaciones según RF-01 (necesario para gestión de inventario)
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre del proveedor es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el RUC no esté duplicado (si se proporciona)
        if (proveedor.getRuc() != null && !proveedor.getRuc().trim().isEmpty()) {
            if (proveedorRepository.findByRuc(proveedor.getRuc()).isPresent()) {
                response.put("error", "Ya existe un proveedor con este RUC");
                return ResponseEntity.badRequest().body(response);
            }
        }
        
        Proveedor savedProveedor = proveedorRepository.save(proveedor);
        response.put("mensaje", "Proveedor registrado exitosamente");
        response.put("proveedor", savedProveedor);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Proveedor proveedor) {
        Map<String, Object> response = new HashMap<>();
        
        if (!proveedorRepository.existsById(id)) {
            response.put("error", "Proveedor no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Validaciones similares a create
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre del proveedor es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el RUC no esté duplicado (excluyendo el proveedor actual)
        if (proveedor.getRuc() != null && !proveedor.getRuc().trim().isEmpty()) {
            Optional<Proveedor> existingProveedor = proveedorRepository.findByRuc(proveedor.getRuc());
            if (existingProveedor.isPresent() && !existingProveedor.get().getId().equals(id)) {
                response.put("error", "Ya existe un proveedor con este RUC");
                return ResponseEntity.badRequest().body(response);
            }
        }
        
        proveedor.setId(id);
        Proveedor updatedProveedor = proveedorRepository.save(proveedor);
        response.put("mensaje", "Proveedor actualizado exitosamente");
        response.put("proveedor", updatedProveedor);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!proveedorRepository.existsById(id)) {
            response.put("error", "Proveedor no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Verificar si el proveedor tiene productos asociados (según RF-01)
        // TODO: Implementar verificación de productos cuando se cree la entidad Producto
        
        proveedorRepository.deleteById(id);
        response.put("mensaje", "Proveedor eliminado exitosamente");
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar proveedor por RUC
    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<Proveedor> getByRuc(@PathVariable String ruc) {
        Optional<Proveedor> proveedor = proveedorRepository.findByRuc(ruc);
        return proveedor.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para buscar proveedores por nombre
    @GetMapping("/nombre/{nombre}")
    public List<Proveedor> getByNombreContaining(@PathVariable String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }
} 