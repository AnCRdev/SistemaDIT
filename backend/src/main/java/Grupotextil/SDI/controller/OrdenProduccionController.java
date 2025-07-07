package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.*;
import Grupotextil.SDI.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordenes-produccion")
@Tag(name = "Órdenes de Producción", description = "Gestión de órdenes de producción")
@CrossOrigin(originPatterns = "*")
public class OrdenProduccionController {

    @Autowired
    private OrdenProduccionRepository ordenProduccionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EtapaDefinidaRepository etapaDefinidaRepository;

    @Autowired
    private EtapaAsignadaRepository etapaAsignadaRepository;

    @Autowired
    private InsumoOrdenRepository insumoOrdenRepository;

    // GET - Obtener todas las órdenes
    @GetMapping
    @Operation(summary = "Obtener todas las órdenes de producción")
    public ResponseEntity<List<OrdenProduccion>> getAll() {
        List<OrdenProduccion> ordenes = ordenProduccionRepository.findAll();
        return ResponseEntity.ok(ordenes);
    }

    // GET - Obtener orden por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una orden de producción por ID")
    public ResponseEntity<OrdenProduccion> getById(@PathVariable UUID id) {
        return ordenProduccionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Crear nueva orden
    @PostMapping
    @Operation(summary = "Crear una nueva orden de producción")
    public ResponseEntity<?> create(@Valid @RequestBody Map<String, Object> request) {
        try {
            // Validar que el producto existe
            UUID productoId = UUID.fromString((String) request.get("productoId"));
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Validar que es un producto terminado
            if (!"Producto Terminado".equals(producto.getTipo())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Solo se pueden crear órdenes para productos terminados"));
            }

            // Validar que el responsable existe
            UUID responsableId = UUID.fromString((String) request.get("responsableId"));
            Usuario responsable = usuarioRepository.findById(responsableId)
                    .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));

            // Validar stock disponible de materias primas (simplificado)
            Integer cantidad = (Integer) request.get("cantidad");
            if (!validarStockDisponible(producto, cantidad)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Stock insuficiente de materias primas para la cantidad solicitada"));
            }

            // Crear la orden
            OrdenProduccion orden = new OrdenProduccion();
            orden.setProducto(producto);
            orden.setCantidad(cantidad);
            orden.setResponsable(responsable);
            
            // Fechas opcionales
            if (request.containsKey("fechaInicio")) {
                orden.setFechaInicio(LocalDate.parse((String) request.get("fechaInicio")));
            }
            if (request.containsKey("fechaFin")) {
                orden.setFechaFin(LocalDate.parse((String) request.get("fechaFin")));
            }
            if (request.containsKey("observaciones")) {
                orden.setObservaciones((String) request.get("observaciones"));
            }

            OrdenProduccion saved = ordenProduccionRepository.save(orden);
            
            // Crear etapas automáticamente si se especifican
            if (request.containsKey("etapas")) {
                crearEtapasAutomaticas(saved, (List<Map<String, Object>>) request.get("etapas"));
            }
            
            // Crear insumos si se especifican
            if (request.containsKey("insumos")) {
                crearInsumosAutomaticos(saved, (List<Map<String, Object>>) request.get("insumos"));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("orden", saved);
            response.put("mensaje", "Orden de producción creada exitosamente");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear la orden: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // PUT - Actualizar orden
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una orden de producción")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody Map<String, Object> request) {
        return ordenProduccionRepository.findById(id)
                .map(orden -> {
                    try {
                        // Actualizar campos básicos
                        if (request.containsKey("cantidad")) {
                            orden.setCantidad((Integer) request.get("cantidad"));
                        }
                        if (request.containsKey("responsableId")) {
                            UUID responsableId = UUID.fromString((String) request.get("responsableId"));
                            Usuario responsable = usuarioRepository.findById(responsableId)
                                    .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));
                            orden.setResponsable(responsable);
                        }
                        if (request.containsKey("fechaInicio")) {
                            orden.setFechaInicio(LocalDate.parse((String) request.get("fechaInicio")));
                        }
                        if (request.containsKey("fechaFin")) {
                            orden.setFechaFin(LocalDate.parse((String) request.get("fechaFin")));
                        }
                        if (request.containsKey("observaciones")) {
                            orden.setObservaciones((String) request.get("observaciones"));
                        }
                        if (request.containsKey("estado")) {
                            orden.setEstado(OrdenProduccion.EstadoOrden.valueOf((String) request.get("estado")));
                        }

                        OrdenProduccion updated = ordenProduccionRepository.save(orden);
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("orden", updated);
                        response.put("mensaje", "Orden de producción actualizada exitosamente");
                        
                        return ResponseEntity.ok(response);
                        
                    } catch (Exception e) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Error al actualizar la orden: " + e.getMessage());
                        return ResponseEntity.badRequest().body(error);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Eliminar orden
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una orden de producción")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ordenProduccionRepository.findById(id)
                .map(orden -> {
                    ordenProduccionRepository.delete(orden);
                    Map<String, String> response = new HashMap<>();
                    response.put("mensaje", "Orden de producción eliminada exitosamente");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Obtener órdenes por estado
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener órdenes por estado")
    public ResponseEntity<List<OrdenProduccion>> getByEstado(@PathVariable String estado) {
        try {
            OrdenProduccion.EstadoOrden estadoEnum = OrdenProduccion.EstadoOrden.valueOf(estado.toUpperCase());
            List<OrdenProduccion> ordenes = ordenProduccionRepository.findByEstado(estadoEnum);
            return ResponseEntity.ok(ordenes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET - Obtener órdenes pendientes
    @GetMapping("/pendientes")
    @Operation(summary = "Obtener órdenes pendientes")
    public ResponseEntity<List<OrdenProduccion>> getPendientes() {
        List<OrdenProduccion> ordenes = ordenProduccionRepository.findOrdenesPendientes();
        return ResponseEntity.ok(ordenes);
    }

    // GET - Obtener órdenes en proceso
    @GetMapping("/en-proceso")
    @Operation(summary = "Obtener órdenes en proceso")
    public ResponseEntity<List<OrdenProduccion>> getEnProceso() {
        List<OrdenProduccion> ordenes = ordenProduccionRepository.findOrdenesEnProceso();
        return ResponseEntity.ok(ordenes);
    }

    // POST - Iniciar producción (cambiar estado a EN_PROCESO)
    @PostMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar producción de una orden")
    public ResponseEntity<?> iniciarProduccion(@PathVariable UUID id) {
        return ordenProduccionRepository.findById(id)
                .map(orden -> {
                    try {
                        if (orden.getEstado() != OrdenProduccion.EstadoOrden.PENDIENTE) {
                            return ResponseEntity.badRequest()
                                    .body(Map.of("error", "Solo se pueden iniciar órdenes pendientes"));
                        }

                        // Descontar stock de materias primas
                        if (!descontarStockMateriasPrimas(orden)) {
                            return ResponseEntity.badRequest()
                                    .body(Map.of("error", "Stock insuficiente de materias primas"));
                        }

                        orden.setEstado(OrdenProduccion.EstadoOrden.EN_PROCESO);
                        orden.setFechaInicio(LocalDate.now());
                        
                        OrdenProduccion updated = ordenProduccionRepository.save(orden);
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("orden", updated);
                        response.put("mensaje", "Producción iniciada exitosamente");
                        
                        return ResponseEntity.ok(response);
                        
                    } catch (Exception e) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Error al iniciar producción: " + e.getMessage());
                        return ResponseEntity.badRequest().body(error);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Finalizar producción (cambiar estado a FINALIZADO)
    @PostMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar producción de una orden")
    public ResponseEntity<?> finalizarProduccion(@PathVariable UUID id) {
        return ordenProduccionRepository.findById(id)
                .map(orden -> {
                    try {
                        if (orden.getEstado() != OrdenProduccion.EstadoOrden.EN_PROCESO) {
                            return ResponseEntity.badRequest()
                                    .body(Map.of("error", "Solo se pueden finalizar órdenes en proceso"));
                        }

                        // Incrementar stock del producto terminado
                        incrementarStockProductoTerminado(orden);

                        orden.setEstado(OrdenProduccion.EstadoOrden.FINALIZADO);
                        orden.setFechaFin(LocalDate.now());
                        
                        OrdenProduccion updated = ordenProduccionRepository.save(orden);
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("orden", updated);
                        response.put("mensaje", "Producción finalizada exitosamente");
                        
                        return ResponseEntity.ok(response);
                        
                    } catch (Exception e) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Error al finalizar producción: " + e.getMessage());
                        return ResponseEntity.badRequest().body(error);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Métodos privados de ayuda
    private boolean validarStockDisponible(Producto producto, Integer cantidad) {
        // Implementación simplificada - en un caso real se validaría contra las materias primas necesarias
        return true;
    }

    private boolean descontarStockMateriasPrimas(OrdenProduccion orden) {
        // Implementación simplificada - en un caso real se descontaría el stock de materias primas
        return true;
    }

    private void incrementarStockProductoTerminado(OrdenProduccion orden) {
        Producto producto = orden.getProducto();
        producto.setStock(producto.getStock() + orden.getCantidad());
        productoRepository.save(producto);
    }

    private void crearEtapasAutomaticas(OrdenProduccion orden, List<Map<String, Object>> etapas) {
        for (Map<String, Object> etapaData : etapas) {
            String nombreEtapa = (String) etapaData.get("nombre");
            EtapaDefinida etapaDefinida = etapaDefinidaRepository.findByNombre(nombreEtapa)
                    .orElseGet(() -> {
                        EtapaDefinida nueva = new EtapaDefinida(nombreEtapa, "Etapa automática");
                        return etapaDefinidaRepository.save(nueva);
                    });

            EtapaAsignada etapaAsignada = new EtapaAsignada(orden, etapaDefinida);
            etapaAsignadaRepository.save(etapaAsignada);
        }
    }

    private void crearInsumosAutomaticos(OrdenProduccion orden, List<Map<String, Object>> insumos) {
        for (Map<String, Object> insumoData : insumos) {
            UUID productoId = UUID.fromString((String) insumoData.get("productoId"));
            BigDecimal cantidad = new BigDecimal(insumoData.get("cantidad").toString());
            
            Producto materiaPrima = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Materia prima no encontrada"));

            InsumoOrden insumo = new InsumoOrden(orden, materiaPrima, cantidad);
            insumoOrdenRepository.save(insumo);
        }
    }
} 