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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/etapas")
@Tag(name = "Etapas de Producción", description = "Gestión de etapas de producción")
@CrossOrigin(originPatterns = "*")
public class EtapaController {

    @Autowired
    private EtapaDefinidaRepository etapaDefinidaRepository;

    @Autowired
    private EtapaAsignadaRepository etapaAsignadaRepository;

    @Autowired
    private OrdenProduccionRepository ordenProduccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // GET - Obtener todas las etapas definidas
    @GetMapping("/definidas")
    @Operation(summary = "Obtener todas las etapas definidas")
    public ResponseEntity<List<EtapaDefinida>> getAllEtapasDefinidas() {
        List<EtapaDefinida> etapas = etapaDefinidaRepository.findAll();
        return ResponseEntity.ok(etapas);
    }

    // POST - Crear nueva etapa definida
    @PostMapping("/definidas")
    @Operation(summary = "Crear una nueva etapa definida")
    public ResponseEntity<?> createEtapaDefinida(@Valid @RequestBody Map<String, Object> request) {
        try {
            String nombre = (String) request.get("nombre");
            String descripcion = (String) request.get("descripcion");

            if (etapaDefinidaRepository.existsByNombre(nombre)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Ya existe una etapa con ese nombre"));
            }

            EtapaDefinida etapa = new EtapaDefinida(nombre, descripcion);
            EtapaDefinida saved = etapaDefinidaRepository.save(etapa);

            Map<String, Object> response = new HashMap<>();
            response.put("etapa", saved);
            response.put("mensaje", "Etapa definida creada exitosamente");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear la etapa: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // GET - Obtener etapas asignadas de una orden
    @GetMapping("/orden/{ordenId}")
    @Operation(summary = "Obtener etapas asignadas de una orden de producción")
    public ResponseEntity<List<EtapaAsignada>> getEtapasByOrden(@PathVariable UUID ordenId) {
        List<EtapaAsignada> etapas = etapaAsignadaRepository.findByOrdenId(ordenId);
        return ResponseEntity.ok(etapas);
    }

    // POST - Asignar etapa a una orden
    @PostMapping("/orden/{ordenId}")
    @Operation(summary = "Asignar una etapa a una orden de producción")
    public ResponseEntity<?> asignarEtapa(@PathVariable UUID ordenId, @Valid @RequestBody Map<String, Object> request) {
        try {
            OrdenProduccion orden = ordenProduccionRepository.findById(ordenId)
                    .orElseThrow(() -> new RuntimeException("Orden de producción no encontrada"));

            UUID etapaId = UUID.fromString((String) request.get("etapaId"));
            EtapaDefinida etapa = etapaDefinidaRepository.findById(etapaId)
                    .orElseThrow(() -> new RuntimeException("Etapa no encontrada"));

            EtapaAsignada etapaAsignada = new EtapaAsignada(orden, etapa);

            // Fechas opcionales
            if (request.containsKey("fechaInicio")) {
                etapaAsignada.setFechaInicio(LocalDate.parse((String) request.get("fechaInicio")));
            }
            if (request.containsKey("fechaFin")) {
                etapaAsignada.setFechaFin(LocalDate.parse((String) request.get("fechaFin")));
            }

            EtapaAsignada saved = etapaAsignadaRepository.save(etapaAsignada);

            Map<String, Object> response = new HashMap<>();
            response.put("etapaAsignada", saved);
            response.put("mensaje", "Etapa asignada exitosamente");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al asignar la etapa: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // PUT - Actualizar estado de una etapa asignada
    @PutMapping("/asignada/{etapaAsignadaId}")
    @Operation(summary = "Actualizar estado de una etapa asignada")
    public ResponseEntity<?> updateEstadoEtapa(@PathVariable UUID etapaAsignadaId, @Valid @RequestBody Map<String, Object> request) {
        return etapaAsignadaRepository.findById(etapaAsignadaId)
                .map(etapaAsignada -> {
                    try {
                        String nuevoEstado = (String) request.get("estado");
                        EtapaAsignada.EstadoEtapa estado = EtapaAsignada.EstadoEtapa.valueOf(nuevoEstado.toUpperCase());

                        // Validar transiciones de estado
                        if (!validarTransicionEstado(etapaAsignada.getEstado(), estado)) {
                            return ResponseEntity.badRequest()
                                    .body(Map.of("error", "Transición de estado no válida"));
                        }

                        etapaAsignada.setEstado(estado);

                        // Actualizar fechas según el estado
                        if (estado == EtapaAsignada.EstadoEtapa.EN_PROCESO && etapaAsignada.getFechaInicio() == null) {
                            etapaAsignada.setFechaInicio(LocalDate.now());
                        } else if (estado == EtapaAsignada.EstadoEtapa.FINALIZADO && etapaAsignada.getFechaFin() == null) {
                            etapaAsignada.setFechaFin(LocalDate.now());
                        }

                        EtapaAsignada updated = etapaAsignadaRepository.save(etapaAsignada);

                        Map<String, Object> response = new HashMap<>();
                        response.put("etapaAsignada", updated);
                        response.put("mensaje", "Estado de etapa actualizado exitosamente");

                        return ResponseEntity.ok(response);

                    } catch (Exception e) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Error al actualizar el estado: " + e.getMessage());
                        return ResponseEntity.badRequest().body(error);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Obtener progreso de una orden
    @GetMapping("/progreso/{ordenId}")
    @Operation(summary = "Obtener progreso de etapas de una orden")
    public ResponseEntity<?> getProgresoOrden(@PathVariable UUID ordenId) {
        try {
            OrdenProduccion orden = ordenProduccionRepository.findById(ordenId)
                    .orElseThrow(() -> new RuntimeException("Orden de producción no encontrada"));

            List<EtapaAsignada> etapasPendientes = etapaAsignadaRepository.findEtapasPendientesByOrden(ordenId);
            List<EtapaAsignada> etapasEnProceso = etapaAsignadaRepository.findEtapasEnProcesoByOrden(ordenId);
            List<EtapaAsignada> etapasFinalizadas = etapaAsignadaRepository.findEtapasFinalizadasByOrden(ordenId);

            int totalEtapas = etapasPendientes.size() + etapasEnProceso.size() + etapasFinalizadas.size();
            double progreso = totalEtapas > 0 ? (double) etapasFinalizadas.size() / totalEtapas * 100 : 0;

            Map<String, Object> response = new HashMap<>();
            response.put("orden", orden);
            response.put("etapasPendientes", etapasPendientes);
            response.put("etapasEnProceso", etapasEnProceso);
            response.put("etapasFinalizadas", etapasFinalizadas);
            response.put("progreso", Math.round(progreso * 100.0) / 100.0);
            response.put("totalEtapas", totalEtapas);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener el progreso: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Método privado para validar transiciones de estado
    private boolean validarTransicionEstado(EtapaAsignada.EstadoEtapa estadoActual, EtapaAsignada.EstadoEtapa nuevoEstado) {
        // Reglas de transición:
        // PENDIENTE -> EN_PROCESO o FINALIZADO
        // EN_PROCESO -> FINALIZADO
        // FINALIZADO -> no puede cambiar
        switch (estadoActual) {
            case PENDIENTE:
                return nuevoEstado == EtapaAsignada.EstadoEtapa.EN_PROCESO || 
                       nuevoEstado == EtapaAsignada.EstadoEtapa.FINALIZADO;
            case EN_PROCESO:
                return nuevoEstado == EtapaAsignada.EstadoEtapa.FINALIZADO;
            case FINALIZADO:
                return false; // No se puede cambiar una etapa finalizada
            default:
                return false;
        }
    }
} 