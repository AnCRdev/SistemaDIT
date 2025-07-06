package Grupotextil.SDI.repository;

import Grupotextil.SDI.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    
    // Buscar por código (para validar duplicados)
    Optional<Producto> findByCodigo(String codigo);
    
    // Buscar por tipo (Materia Prima o Producto Terminado)
    List<Producto> findByTipo(String tipo);
    
    // Buscar productos con stock bajo (stock <= stock mínimo)
    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo AND p.stockMinimo IS NOT NULL")
    List<Producto> findByStockLessThanEqualAndStockMinimoIsNotNull();
    
    // Buscar productos por proveedor
    List<Producto> findByProveedorId(UUID proveedorId);
    
    // Buscar productos por categoría
    List<Producto> findByCategoriaId(UUID categoriaId);
    
    // Buscar productos con stock positivo
    List<Producto> findByStockGreaterThan(Integer stock);
} 