package com.minimarket.app.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minimarket.app.entidad.Usuario;


// Permite el acceso a datos de los usuarios
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su nombre de usuario
  Optional <Usuario> findByUsuario(String usuario);
  
  
}

