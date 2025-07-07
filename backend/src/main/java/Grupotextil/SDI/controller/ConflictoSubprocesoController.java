package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.*;
import Grupotextil.SDI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/api/conflictos")
public class ConflictoSubprocesoController {
    @Autowired
    private ConflictoSubprocesoRepository conflictoRepo;
    @Autowired
    private EtapaAsignadaRepository etapaAsignadaRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private BaseConocimientoSolucionesRepository baseConocimientoRepo;

    // POST: Reportar conflicto
    @PostMapping
    public ResponseEntity<?> reportarConflicto(@RequestBody Map<String, Object> request) {
        try {
            UUID etapaAsignadaId = UUID.fromString((String) request.get("etapaAsignadaId"));
            UUID usuarioReportaId = UUID.fromString((String) request.get("usuarioReportaId"));
            String tipoError = (String) request.get("tipoError");
            String descripcion = (String) request.getOrDefault("descripcion", "");

            EtapaAsignada etapaAsignada = etapaAsignadaRepo.findById(etapaAsignadaId)
                .orElseThrow(() -> new RuntimeException("Etapa asignada no encontrada"));
            Usuario usuarioReporta = usuarioRepo.findById(usuarioReportaId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Cambiar estado de etapa a 'En Conflicto'
            etapaAsignada.setEstado(EtapaAsignada.EstadoEtapa.EN_CONFLICTO);
            etapaAsignadaRepo.save(etapaAsignada);

            // Buscar causas y soluciones sugeridas
            List<BaseConocimientoSoluciones> baseConocimiento = baseConocimientoRepo.findByTipoError(tipoError);
            List<String> causas = baseConocimiento.stream().map(BaseConocimientoSoluciones::getCausa).collect(Collectors.toList());
            List<String> soluciones = baseConocimiento.stream().map(BaseConocimientoSoluciones::getSolucion).collect(Collectors.toList());

            ConflictoSubproceso conflicto = new ConflictoSubproceso();
            conflicto.setEtapaAsignada(etapaAsignada);
            conflicto.setUsuarioReporta(usuarioReporta);
            conflicto.setTipoError(tipoError);
            conflicto.setDescripcion(descripcion);
            conflicto.setEstado("En Conflicto");
            conflicto.setFechaReporte(LocalDateTime.now());
            conflicto.setPosiblesCausas(causas);
            conflicto.setSolucionesSugeridas(soluciones);

            ConflictoSubproceso saved = conflictoRepo.save(conflicto);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Conflicto reportado exitosamente");
            response.put("conflicto", saved);
            response.put("causasSugeridas", causas);
            response.put("solucionesSugeridas", soluciones);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // GET: Listar conflictos de una etapa
    @GetMapping("/etapa/{etapaAsignadaId}")
    public ResponseEntity<?> listarPorEtapa(@PathVariable UUID etapaAsignadaId) {
        List<ConflictoSubproceso> conflictos = conflictoRepo.findByEtapaAsignadaId(etapaAsignadaId);
        return ResponseEntity.ok(conflictos);
    }

    // GET: Obtener detalle de un conflicto
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return conflictoRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // PUT: Resolver conflicto
    @PutMapping("/{id}/resolver")
    public ResponseEntity<?> resolverConflicto(@PathVariable UUID id, @RequestBody Map<String, Object> request) {
        return conflictoRepo.findById(id).map(conflicto -> {
            try {
                String solucionSeleccionada = (String) request.get("solucionSeleccionada");
                String diagnostico = (String) request.getOrDefault("diagnostico", "");
                UUID responsableId = request.containsKey("responsableResolucionId") ? UUID.fromString((String) request.get("responsableResolucionId")) : null;
                Usuario responsable = responsableId != null ? usuarioRepo.findById(responsableId).orElse(null) : null;

                conflicto.setEstado("Resuelto");
                conflicto.setFechaResolucion(LocalDateTime.now());
                conflicto.setSolucionSeleccionada(solucionSeleccionada);
                conflicto.setDiagnostico(diagnostico);
                conflicto.setResponsableResolucion(responsable);
                conflictoRepo.save(conflicto);

                // Cambiar estado de etapa a EN_PROCESO o FINALIZADO según lógica de negocio
                EtapaAsignada etapa = conflicto.getEtapaAsignada();
                if (etapa.getEstado() == EtapaAsignada.EstadoEtapa.EN_CONFLICTO) {
                    etapa.setEstado(EtapaAsignada.EstadoEtapa.EN_PROCESO);
                    etapaAsignadaRepo.save(etapa);
                }

                return ResponseEntity.ok(Map.of("mensaje", "Conflicto resuelto exitosamente", "conflicto", conflicto));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            }
        }).orElse(ResponseEntity.notFound().build());
    }
} 