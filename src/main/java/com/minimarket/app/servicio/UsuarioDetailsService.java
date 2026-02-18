package com.minimarket.app.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.repositorio.UsuarioRepositorio;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //  Busca el usuario en la base de datos por su username
        Usuario usuario = usuarioRepositorio.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        //  Retorna un objeto User de Spring Security con los datos necesarios
        return usuario;
    }
}