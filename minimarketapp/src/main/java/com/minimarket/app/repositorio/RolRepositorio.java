package com.minimarket.app.repositorio;

import com.minimarket.app.entidad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Permite el acceso a datos de los roles
@Repository
public interface RolRepositorio extends JpaRepository<Rol, Long> {
}