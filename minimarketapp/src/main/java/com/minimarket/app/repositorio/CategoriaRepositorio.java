package com.minimarket.app.repositorio;

import com.minimarket.app.entidad.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Permite el acceso a datos de las categorías
@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
}