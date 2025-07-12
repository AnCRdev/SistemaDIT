package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.*;
import Grupotextil.SDI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;
    
    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;
    
    @Autowired
    private TipoProductoRepository tipoProductoRepository;

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
    public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Extraer datos básicos
            String codigo = (String) request.get("codigo");
            String nombre = (String) request.get("nombre");
            String descripcion = (String) request.get("descripcion");
            Integer stock = (Integer) request.get("stock");
            Integer stockMinimo = (Integer) request.get("stockMinimo");
            String tipo = (String) request.get("tipo");
            BigDecimal precio = null;
            if (request.get("precio") != null) {
                precio = new BigDecimal(request.get("precio").toString());
            }
            
            // Validaciones básicas
            if (codigo == null || codigo.trim().isEmpty()) {
                response.put("error", "El código es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (nombre == null || nombre.trim().isEmpty()) {
                response.put("error", "El nombre es obligatorio");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (stock == null || stock < 0) {
                response.put("error", "El stock debe ser mayor o igual a 0");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Verificar que el código no esté duplicado
            if (productoRepository.findByCodigo(codigo).isPresent()) {
                response.put("error", "El código de producto ya existe");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Validar tipo de producto
            if (tipo == null || 
                (!tipo.equals("Materia Prima") && !tipo.equals("Producto Terminado"))) {
                response.put("error", "El tipo debe ser 'Materia Prima' o 'Producto Terminado'");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Crear el producto
            Producto producto = new Producto();
            producto.setCodigo(codigo);
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setStock(stock);
            producto.setStockMinimo(stockMinimo);
            producto.setTipo(tipo);
            producto.setPrecio(precio);
            
            // Procesar relaciones por ID
            if (request.get("proveedorId") != null) {
                UUID proveedorId = UUID.fromString((String) request.get("proveedorId"));
                Optional<Proveedor> proveedor = proveedorRepository.findById(proveedorId);
                if (proveedor.isPresent()) {
                    producto.setProveedor(proveedor.get());
                }
            }
            
            if (request.get("categoriaId") != null) {
                UUID categoriaId = UUID.fromString((String) request.get("categoriaId"));
                Optional<CategoriaProducto> categoria = categoriaProductoRepository.findById(categoriaId);
                if (categoria.isPresent()) {
                    producto.setCategoria(categoria.get());
                }
            }
            
            if (request.get("unidadId") != null) {
                UUID unidadId = UUID.fromString((String) request.get("unidadId"));
                Optional<UnidadMedida> unidad = unidadMedidaRepository.findById(unidadId);
                if (unidad.isPresent()) {
                    producto.setUnidad(unidad.get());
                }
            }
            
            if (request.get("tipoProductoId") != null) {
                UUID tipoProductoId = UUID.fromString((String) request.get("tipoProductoId"));
                Optional<TipoProducto> tipoProducto = tipoProductoRepository.findById(tipoProductoId);
                if (tipoProducto.isPresent()) {
                    producto.setTipoProducto(tipoProducto.get());
                }
            }
            
            Producto savedProducto = productoRepository.save(producto);
            response.put("mensaje", "Artículo guardado exitosamente");
            response.put("producto", savedProducto);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("error", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
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