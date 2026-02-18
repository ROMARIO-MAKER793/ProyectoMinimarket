package com.minimarket.app.servicio;


import java.util.List;
import com.minimarket.app.entidad.Venta;

public interface VentaServicio {
	
    // Registra una venta (guardar)
    Venta guardar(Venta venta);

    // Lista todas las ventas
    List<Venta> listarTodos();

    // Busca una venta por su ID
    Venta buscarPorId(Long id);

    // Elimina una venta por su ID
    void eliminar(Long id);
}
