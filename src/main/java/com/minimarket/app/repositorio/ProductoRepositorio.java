package com.minimarket.app.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.minimarket.app.entidad.Producto;

import java.util.List;

// Permite el acceso a datos de los productos
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
	
	
	long countByActivoTrue();
    // Obtiene los productos activos
    List<Producto> findByActivoTrue();

    // Obtiene los productos inactivos
    List<Producto> findByActivoFalse();
    
    @Query("""
            SELECT p.categoria.nombre, COUNT(p)
            FROM Producto p
            GROUP BY p.categoria.nombre
        """)
        List<Object[]> distribucionCategorias();
}