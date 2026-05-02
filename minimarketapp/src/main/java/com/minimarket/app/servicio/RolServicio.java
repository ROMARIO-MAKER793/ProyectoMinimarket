package com.minimarket.app.servicio;

import java.util.List;

import com.minimarket.app.entidad.Rol;

public interface RolServicio {
	
	List<Rol> listarTodos();
	
	Rol buscarPorId(Long Id);

}
