package com.minimarket.app.servicio;


import java.util.List;
import java.util.Map;

import com.minimarket.app.entidad.Venta;
import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.entidad.Producto;
import com.minimarket.app.entidad.DetalleVenta;

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
    
    //Crear una venta con detalles, calcula total, descuenta stock y asigna numero de boleta
    Venta crearVentaConDetails(List<DetalleVenta> detalles, Usuario usuario);
}
