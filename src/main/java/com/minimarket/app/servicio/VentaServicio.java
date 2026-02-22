package com.minimarket.app.servicio;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.minimarket.app.entidad.Venta;
import com.minimarket.app.entidad.VentaDTO;
import com.minimarket.app.dto.ReporteVentaMesDTO;
import com.minimarket.app.entidad.Producto;


public interface VentaServicio {
	
    // Registra una venta (guardar)
    Venta guardar(Venta venta);

    // Lista todas las ventas
    List<Venta> listarTodos();

    // Busca una venta por su ID
    Venta buscarPorId(Long id);

    // Elimina una venta por su ID
    void eliminar(Long id);
    
    //Validar si los productos tiene suficiente stock
    boolean validarStock(Map<Producto , Integer> items);
    
  //Crear con toda la logica
    Map<String, Object> crearVentaDTO(VentaDTO ventaDTO) throws IOException;
    
    //METODOS PARA REPORTE VENTAS
    
    List<ReporteVentaMesDTO> getReporteVentasDelMes();
    
    double obtenerTotalVentasHoy();
    
    double obtenerTotalVentasMes();
    
    Long obtenerCantidadVentasMes();
    
    Long obtenerTotalProductosVendidosMes();
    
    List<Object[]> obtenerVentasPorDiaDelMes(); 
    
    List<Object[]> obtenerTop5ProductosMes(); 
}
