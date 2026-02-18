package com.minimarket.app.servicio;

import java.util.List;

import com.minimarket.app.entidad.Usuario;

public interface UsuarioServicio {
	
	// Registra un usuario
    Usuario guardar(Usuario usuario);
    
    // Lista todos los Usuarios
    List<Usuario> listarTodos();
    
    // Busca usuario por su id
    Usuario buscarPorId(Long id);
    
    //Eliminia un usuaario por su ID
    void eliminar(Long id);
    
    // Busca un usuario por su correo
    Usuario buscarPorUsuario(String usuario);

    //Verifica si la contraseña coincide con el hash
    boolean verificarContrasena(String textoPlano, String hash);
    	
    
    
}