package com.minimarket.app.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minimarket.app.entidad.DetalleVenta;
import com.minimarket.app.repositorio.DetalleVentaRepositorio;

@Service
public class DetalleVentaServicioImpl implements DetalleVentaServicio {

    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;

    // Guarda un detalle de venta
    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        return detalleVentaRepositorio.save(detalleVenta);
    }
}