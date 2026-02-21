package com.minimarket.app.repositorio;

import com.minimarket.app.entidad.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long> {

    // Total vendido en el mes actual
    @Query("""
        SELECT COALESCE (ROUND(SUM(v.total),2) ,0)
        FROM Venta v
        WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE)
          AND YEAR(v.fecha) = YEAR(CURRENT_DATE)
    """)
    Double totalVendidoMes();

    // Distribución por categoría
    @Query("""
        SELECT dv.producto.categoria.nombre, SUM(dv.cantidad)
        FROM Venta v
        JOIN v.detalles dv
        GROUP BY dv.producto.categoria.nombre
    """)
    List<Object[]> distribucionPorCategoria();

    // Productos más vendidos
    @Query("""
        SELECT dv.producto.nombre, SUM(dv.cantidad)
        FROM Venta v
        JOIN v.detalles dv
        GROUP BY dv.producto.nombre
        ORDER BY SUM(dv.cantidad) DESC
    """)
    List<Object[]> productosMasVendidos();
    
    @Query("""
    	    SELECT MONTH(v.fecha),ROUND(SUM(v.total),2)
    	    FROM Venta v
    	    GROUP BY MONTH(v.fecha)
    	    ORDER BY MONTH(v.fecha)
    	""")
    	List<Object[]> ventasPorMes();
    	
    	List<Venta> findTop5ByOrderByFechaDesc();
}