package com.minimarket.app.servicio;

import com.minimarket.app.entidad.DetalleVenta;

public interface DetalleVentaServicio {
	
    // Registra un detalle de venta
    DetalleVenta guardar(DetalleVenta detalleVenta);
}
