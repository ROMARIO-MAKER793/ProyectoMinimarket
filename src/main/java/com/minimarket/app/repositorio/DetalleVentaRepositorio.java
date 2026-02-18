package com.minimarket.app.repositorio;

import com.minimarket.app.entidad.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Permite el acceso a datos del detalle de ventas
@Repository
public interface DetalleVentaRepositorio extends JpaRepository<DetalleVenta, Long> {
}