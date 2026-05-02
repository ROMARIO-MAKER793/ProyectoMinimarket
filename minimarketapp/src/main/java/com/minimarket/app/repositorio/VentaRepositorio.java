package com.minimarket.app.repositorio;

import com.minimarket.app.dto.ReporteVentaMesDTO;
import com.minimarket.app.entidad.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long> {
	
	//CONSULTAS PARA DASHBOARD INICIO
	
	// OBTENER ÚLTIMO ID PARA GENERAR BOLETA
    @Query("SELECT MAX(v.id) FROM Venta v")
    Long obtenerMaximoId();
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
    	//FIN CONSULTAS PARA DASHBOARD
    	
    	//REPORTES
    	//Consulta para Reporte Ventas inicio
    	
    	@Query("""
    		    SELECT new com.minimarket.app.dto.ReporteVentaMesDTO(
    		        dv.producto.nombre,       
    		        SUM(dv.cantidad),
    		        SUM(dv.subtotal),
    		        MAX(v.fecha)
    		    )
    		    FROM Venta v
    		    JOIN v.detalles dv          
    		    WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE)
    		      AND YEAR(v.fecha) = YEAR(CURRENT_DATE)
    		    GROUP BY dv.producto.nombre
    		    ORDER BY SUM(dv.subtotal) DESC
    		""")
    		List<ReporteVentaMesDTO> obtenerVentasDelMes();
    	
    	//consulta para reportes venta al dia
    	@Query("""
    		    SELECT COALESCE(ROUND(SUM(v.total),2),0)
    		    FROM Venta v
    		    WHERE v.fecha BETWEEN :inicio AND :fin
    		""")
    		Double totalVentasEntreFechas(
    		        @Param("inicio") LocalDateTime inicio,
    		        @Param("fin") LocalDateTime fin
    		);
    	
    	// Total dinero vendido en el mes actual
    	@Query("""
    	    SELECT COALESCE(ROUND(SUM(v.total),2),0)
    	    FROM Venta v
    	    WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE)
    	      AND YEAR(v.fecha) = YEAR(CURRENT_DATE)
    	""")
    	Double totalVentasMesActual();


    	// Cantidad de ventas realizadas en el mes
    	@Query("""
    	    SELECT COUNT(v)
    	    FROM Venta v
    	    WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE)
    	      AND YEAR(v.fecha) = YEAR(CURRENT_DATE)
    	""")
    	Long cantidadVentasMesActual();


    	// Total productos vendidos (sumando cantidades de detalles)
    	@Query("""
    	    SELECT COALESCE(SUM(dv.cantidad),0)
    	    FROM Venta v
    	    JOIN v.detalles dv
    	    WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE)
    	      AND YEAR(v.fecha) = YEAR(CURRENT_DATE)
    	""")
    	Long totalProductosVendidosMesActual();
    	
    	// Calculando que dia se vendio más
    	
    	@Query("""
    		    SELECT DAY(v.fecha), COALESCE(ROUND(SUM(v.total),2),0)
    		    FROM Venta v
    		    WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE)
    		      AND YEAR(v.fecha) = YEAR(CURRENT_DATE)
    		    GROUP BY DAY(v.fecha)
    		    ORDER BY DAY(v.fecha)
    		""")
    		List<Object[]> ventasPorDiaDelMes();
    	
    	// Reportes para ver los top productos mas vendidos	
    		@Query("""
    			    SELECT dv.producto.nombre, SUM(dv.cantidad)
    			    FROM Venta v
    			    JOIN v.detalles dv
    			    WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE)
    			      AND YEAR(v.fecha) = YEAR(CURRENT_DATE)
    			    GROUP BY dv.producto.nombre
    			    ORDER BY SUM(dv.cantidad) DESC
    			""")
    			List<Object[]> topProductosMes();
    			
    	
}