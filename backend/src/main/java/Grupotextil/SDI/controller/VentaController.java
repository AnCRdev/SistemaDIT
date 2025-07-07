package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.*;
import Grupotextil.SDI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // GET - Obtener todas las ventas
    @GetMapping
    public List<Venta> getAll() {
        return ventaRepository.findAll();
    }

    // GET - Obtener venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getById(@PathVariable UUID id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // POST - Crear una nueva venta
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extraer datos del request
            String clienteIdStr = (String) request.get("clienteId");
            String usuarioIdStr = (String) request.get("usuarioId");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detallesData = (List<Map<String, Object>>) request.get("detalles");

            if (clienteIdStr == null || detallesData == null || detallesData.isEmpty()) {
                response.put("error", "clienteId y detalles son obligatorios");
                return ResponseEntity.badRequest().body(response);
            }

            // Validar y obtener cliente
            UUID clienteId = UUID.fromString(clienteIdStr);
            Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
            if (!clienteOpt.isPresent()) {
                response.put("error", "Cliente no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Validar y obtener usuario (opcional)
            Usuario usuario = null;
            if (usuarioIdStr != null) {
                UUID usuarioId = UUID.fromString(usuarioIdStr);
                Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
                if (!usuarioOpt.isPresent()) {
                    response.put("error", "Usuario no encontrado");
                    return ResponseEntity.badRequest().body(response);
                }
                usuario = usuarioOpt.get();
            }

            // Crear la venta
            Venta venta = new Venta();
            venta.setCliente(clienteOpt.get());
            venta.setUsuario(usuario);
            venta.setFecha(LocalDateTime.now());

            // Procesar detalles
            BigDecimal totalVenta = BigDecimal.ZERO;
            List<DetalleVenta> detalles = new ArrayList<>();

            for (Map<String, Object> detalleData : detallesData) {
                String productoIdStr = (String) detalleData.get("productoId");
                Integer cantidad = (Integer) detalleData.get("cantidad");
                Double precioUnitario = (Double) detalleData.get("precioUnitario");

                if (productoIdStr == null || cantidad == null || precioUnitario == null) {
                    response.put("error", "productoId, cantidad y precioUnitario son obligatorios en cada detalle");
                    return ResponseEntity.badRequest().body(response);
                }

                // Validar y obtener producto
                UUID productoId = UUID.fromString(productoIdStr);
                Optional<Producto> productoOpt = productoRepository.findById(productoId);
                if (!productoOpt.isPresent()) {
                    response.put("error", "Producto no encontrado: " + productoIdStr);
                    return ResponseEntity.badRequest().body(response);
                }

                Producto producto = productoOpt.get();

                // Validar stock
                if (producto.getStock() < cantidad) {
                    response.put("error", "Stock insuficiente para el producto: " + producto.getNombre() + 
                               ". Stock disponible: " + producto.getStock() + ", Cantidad solicitada: " + cantidad);
                    return ResponseEntity.badRequest().body(response);
                }

                // Crear detalle
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setProducto(producto);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(BigDecimal.valueOf(precioUnitario));
                detalle.calcularSubtotal();

                detalles.add(detalle);
                totalVenta = totalVenta.add(detalle.getSubtotal());
            }

            venta.setDetalles(detalles);
            venta.setTotal(totalVenta);

            // Guardar la venta
            Venta savedVenta = ventaRepository.save(venta);

            // Actualizar stock de productos
            for (DetalleVenta detalle : savedVenta.getDetalles()) {
                Producto producto = detalle.getProducto();
                producto.setStock(producto.getStock() - detalle.getCantidad());
                productoRepository.save(producto);
            }

            response.put("mensaje", "Venta registrada exitosamente");
            response.put("venta", savedVenta);
            response.put("total", totalVenta);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            response.put("error", "Formato de UUID inválido");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // PUT - Actualizar una venta
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody Venta venta) {
        Map<String, Object> response = new HashMap<>();

        if (!ventaRepository.existsById(id)) {
            response.put("error", "Venta no encontrada");
            return ResponseEntity.notFound().build();
        }

        venta.setId(id);
        Venta updatedVenta = ventaRepository.save(venta);
        
        response.put("mensaje", "Venta actualizada exitosamente");
        response.put("venta", updatedVenta);
        
        return ResponseEntity.ok(response);
    }

    // DELETE - Eliminar una venta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();

        if (!ventaRepository.existsById(id)) {
            response.put("error", "Venta no encontrada");
            return ResponseEntity.notFound().build();
        }

        ventaRepository.deleteById(id);
        response.put("mensaje", "Venta eliminada exitosamente");
        
        return ResponseEntity.ok(response);
    }

    // GET - Buscar ventas por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venta>> getByCliente(@PathVariable UUID clienteId) {
        List<Venta> ventas = ventaRepository.findByClienteId(clienteId);
        return ResponseEntity.ok(ventas);
    }

    // GET - Buscar ventas por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Venta>> getByUsuario(@PathVariable UUID usuarioId) {
        List<Venta> ventas = ventaRepository.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(ventas);
    }

    // GET - Buscar ventas por rango de fechas
    @GetMapping("/fechas")
    public ResponseEntity<List<Venta>> getByFechaBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
        return ResponseEntity.ok(ventas);
    }

    // GET - Ventas del día actual
    @GetMapping("/hoy")
    public ResponseEntity<List<Venta>> getVentasHoy() {
        List<Venta> ventas = ventaRepository.findVentasHoy();
        return ResponseEntity.ok(ventas);
    }

    // GET - Reporte de ventas por rango de fechas
    @GetMapping("/reporte")
    public ResponseEntity<Map<String, Object>> getReporteVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        Map<String, Object> reporte = new HashMap<>();
        
        List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
        Long totalVentas = ventaRepository.countByFechaBetween(fechaInicio, fechaFin);
        Double totalIngresos = ventaRepository.sumTotalByFechaBetween(fechaInicio, fechaFin);
        
        reporte.put("fechaInicio", fechaInicio);
        reporte.put("fechaFin", fechaFin);
        reporte.put("totalVentas", totalVentas);
        reporte.put("totalIngresos", totalIngresos != null ? totalIngresos : 0.0);
        reporte.put("ventas", ventas);
        
        return ResponseEntity.ok(reporte);
    }

    // GET - Detalles de una venta
    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleVenta>> getDetallesByVenta(@PathVariable UUID id) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(id);
        return ResponseEntity.ok(detalles);
    }
} 