package com.minimarket.app.repositorio;

import com.minimarket.app.entidad.DetalleVenta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Permite el acceso a datos del detalle de ventas
@Repository
public interface DetalleVentaRepositorio extends JpaRepository<DetalleVenta, Long> {
	@Query("""
	        SELECT d.producto.nombre, SUM(d.cantidad)
	        FROM DetalleVenta d
	        GROUP BY d.producto.nombre
	        ORDER BY SUM(d.cantidad) DESC
	    """)
	    List<Object[]> productosMasVendidos();
}