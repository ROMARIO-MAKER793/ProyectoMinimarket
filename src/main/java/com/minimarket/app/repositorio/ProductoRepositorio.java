package com.minimarket.app.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimarket.app.entidad.Producto;

import java.util.List;

// Permite el acceso a datos de los productos
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {

    // Obtiene los productos activos
    List<Producto> findByActivoTrue();

    // Obtiene los productos inactivos
    List<Producto> findByActivoFalse();
}