package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.ConflictoSubproceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConflictoSubprocesoRepository extends JpaRepository<ConflictoSubproceso, UUID> {
    List<ConflictoSubproceso> findByEtapaAsignadaId(UUID etapaAsignadaId);
    List<ConflictoSubproceso> findByEstado(String estado);
} 