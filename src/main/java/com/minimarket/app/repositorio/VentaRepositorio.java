package com.minimarket.app.repositorio;

import com.minimarket.app.entidad.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Permite el acceso a datos de las ventas
@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long> {
}