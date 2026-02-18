package com.minimarket.app.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minimarket.app.entidad.Rol;
import com.minimarket.app.repositorio.RolRepositorio;

@Service
public class RolServicioImpl implements RolServicio {
	
	@Autowired
	private RolRepositorio rolRepositorio;
	
	@Override
	public List<Rol> listarTodos() {
			return rolRepositorio.findAll();
	}

	@Override
	public Rol buscarPorId(Long id) {
		return rolRepositorio.findById(id).orElse(null);
	}

}
