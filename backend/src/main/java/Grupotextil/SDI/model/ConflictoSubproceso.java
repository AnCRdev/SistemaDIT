package Grupotextil.SDI.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "conflictos_subprocesos")
public class ConflictoSubproceso {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etapa_asignada_id", nullable = false)
    private EtapaAsignada etapaAsignada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_reporta_id", nullable = false)
    private Usuario usuarioReporta;

    @Column(name = "tipo_error", nullable = false)
    private String tipoError; // Falta de material, Falla de máquina, Error de calidad

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private String estado; // En Conflicto, Resuelto, Escalado

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @ElementCollection
    @CollectionTable(name = "conflicto_causas", joinColumns = @JoinColumn(name = "conflicto_id"))
    @Column(name = "causa")
    private List<String> posiblesCausas;

    @ElementCollection
    @CollectionTable(name = "conflicto_soluciones", joinColumns = @JoinColumn(name = "conflicto_id"))
    @Column(name = "solucion")
    private List<String> solucionesSugeridas;

    @Column(name = "solucion_seleccionada")
    private String solucionSeleccionada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_resolucion_id")
    private Usuario responsableResolucion;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public EtapaAsignada getEtapaAsignada() { return etapaAsignada; }
    public void setEtapaAsignada(EtapaAsignada etapaAsignada) { this.etapaAsignada = etapaAsignada; }
    public Usuario getUsuarioReporta() { return usuarioReporta; }
    public void setUsuarioReporta(Usuario usuarioReporta) { this.usuarioReporta = usuarioReporta; }
    public String getTipoError() { return tipoError; }
    public void setTipoError(String tipoError) { this.tipoError = tipoError; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(LocalDateTime fechaReporte) { this.fechaReporte = fechaReporte; }
    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public List<String> getPosiblesCausas() { return posiblesCausas; }
    public void setPosiblesCausas(List<String> posiblesCausas) { this.posiblesCausas = posiblesCausas; }
    public List<String> getSolucionesSugeridas() { return solucionesSugeridas; }
    public void setSolucionesSugeridas(List<String> solucionesSugeridas) { this.solucionesSugeridas = solucionesSugeridas; }
    public String getSolucionSeleccionada() { return solucionSeleccionada; }
    public void setSolucionSeleccionada(String solucionSeleccionada) { this.solucionSeleccionada = solucionSeleccionada; }
    public Usuario getResponsableResolucion() { return responsableResolucion; }
    public void setResponsableResolucion(Usuario responsableResolucion) { this.responsableResolucion = responsableResolucion; }
} 