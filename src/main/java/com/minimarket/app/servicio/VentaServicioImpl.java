package com.minimarket.app.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minimarket.app.entidad.Venta;
import com.minimarket.app.repositorio.VentaRepositorio;

@Service
public class VentaServicioImpl implements VentaServicio {

    @Autowired
    private VentaRepositorio ventaRepositorio;

    // Registra o actualiza una venta
    @Override
    public Venta guardar(Venta venta) {
        return ventaRepositorio.save(venta);
    }

    // Lista todas las ventas
    @Override
    public List<Venta> listarTodos() {
        return ventaRepositorio.findAll();
    }

    // Busca una venta por ID
    @Override
    public Venta buscarPorId(Long id) {
        return ventaRepositorio.findById(id).orElse(null);
    }

    // Elimina una venta por ID
    @Override
    public void eliminar(Long id) {
        ventaRepositorio.deleteById(id);
    }
}