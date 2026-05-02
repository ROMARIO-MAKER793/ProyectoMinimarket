package com.minimarket.app.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Guarda o actualiza un usuario
    @Override
    public Usuario guardar(Usuario usuario) {
        if (usuario.getContrasena() != null) {
            usuario.setContrasena(
                passwordEncoder.encode(usuario.getContrasena())
            );
        }
        return usuarioRepositorio.save(usuario);
    }

    // Lista todos los usuarios
    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    // Busca un usuario por ID
    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    // Elimina un usuario por ID
    @Override
    public void eliminar(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    // Busca un usuario por nombre de usuario
    @Override
    public Usuario buscarPorUsuario(String usuario) {
        return usuarioRepositorio.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
    }

    // Verifica contraseña
    @Override
    public boolean verificarContrasena(String textoPlano, String hash) {
        return passwordEncoder.matches(textoPlano, hash);
    }
    
    
}