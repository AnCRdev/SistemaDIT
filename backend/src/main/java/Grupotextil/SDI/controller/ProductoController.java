package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.Producto;
import Grupotextil.SDI.repository.ProductoRepository;
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
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable UUID id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
        
        // Validaciones según RF-01
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            response.put("error", "El código es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (producto.getStock() == null || producto.getStock() < 0) {
            response.put("error", "El stock debe ser mayor o igual a 0");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el código no esté duplicado
        if (productoRepository.findByCodigo(producto.getCodigo()).isPresent()) {
            response.put("error", "El código de producto ya existe");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Validar tipo de producto
        if (producto.getTipo() == null || 
            (!producto.getTipo().equals("Materia Prima") && !producto.getTipo().equals("Producto Terminado"))) {
            response.put("error", "El tipo debe ser 'Materia Prima' o 'Producto Terminado'");
            return ResponseEntity.badRequest().body(response);
        }
        
        Producto savedProducto = productoRepository.save(producto);
        response.put("mensaje", "Artículo guardado exitosamente");
        response.put("producto", savedProducto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();
        
        if (!productoRepository.existsById(id)) {
            response.put("error", "Producto no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Validaciones similares a create
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            response.put("error", "El código es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (producto.getStock() == null || producto.getStock() < 0) {
            response.put("error", "El stock debe ser mayor o igual a 0");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el código no esté duplicado (excluyendo el producto actual)
        Optional<Producto> existingProducto = productoRepository.findByCodigo(producto.getCodigo());
        if (existingProducto.isPresent() && !existingProducto.get().getId().equals(id)) {
            response.put("error", "El código de producto ya existe");
            return ResponseEntity.badRequest().body(response);
        }
        
        producto.setId(id);
        Producto updatedProducto = productoRepository.save(producto);
        response.put("mensaje", "Artículo actualizado exitosamente");
        response.put("producto", updatedProducto);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!productoRepository.existsById(id)) {
            response.put("error", "Producto no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        productoRepository.deleteById(id);
        response.put("mensaje", "Producto eliminado exitosamente");
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para productos con stock bajo (según RF-01)
    @GetMapping("/stock-bajo")
    public List<Producto> getProductosStockBajo() {
        return productoRepository.findByStockLessThanEqualAndStockMinimoIsNotNull();
    }

    // Endpoint para productos por tipo
    @GetMapping("/tipo/{tipo}")
    public List<Producto> getProductosPorTipo(@PathVariable String tipo) {
        return productoRepository.findByTipo(tipo);
    }
} 