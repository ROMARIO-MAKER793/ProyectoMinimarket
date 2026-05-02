package com.minimarket.app.servicio;

import com.minimarket.app.entidad.Marca;
import com.minimarket.app.repositorio.MarcaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementa la lógica de negocio para Marca.
 */
@Service
public class MarcaServicioImpl implements MarcaServicio {

    @Autowired
    private MarcaRepositorio marcaRepositorio;

    // Retorna todas las marcas registradas
    @Override
    public List<Marca> listarTodos() {
        return marcaRepositorio.findAll();
    }

    // Guarda o actualiza una marca
    @Override
    public Marca guardar(Marca marca) {
        return marcaRepositorio.save(marca);
    }

    // Busca una marca por ID
    @Override
    public Marca buscarPorId(Long id) {
        return marcaRepositorio.findById(id).orElse(null);
    }

    // Elimina una marca por ID
    @Override
    public void eliminar(Long id) {
        marcaRepositorio.deleteById(id);
    }
}