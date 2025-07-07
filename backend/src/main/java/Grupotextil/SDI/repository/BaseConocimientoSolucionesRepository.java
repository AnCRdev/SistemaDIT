package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.BaseConocimientoSoluciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BaseConocimientoSolucionesRepository extends JpaRepository<BaseConocimientoSoluciones, UUID> {
    List<BaseConocimientoSoluciones> findByTipoError(String tipoError);
} 