package com.minimarket.app.repositorio;

import com.minimarket.app.entidad.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Permite el acceso a datos de las marcas
@Repository
public interface MarcaRepositorio extends JpaRepository<Marca, Long> {
}