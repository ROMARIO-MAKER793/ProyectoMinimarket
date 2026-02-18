package com.minimarket.app.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minimarket.app.entidad.Categoria;
import com.minimarket.app.repositorio.CategoriaRepositorio;

@Service
public class CategoriaServicioImpl implements CategoriaServicio {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    // Retorna todas las categorías
    @Override
    public List<Categoria> listarTodas() {
        return categoriaRepositorio.findAll();
    }

    // Busca una categoría por su ID
    @Override
    public Categoria buscarPorId(Long id) {
        return categoriaRepositorio.findById(id).orElse(null);
    }

    // Guarda o actualiza una categoría
    @Override
    public Categoria guardar(Categoria categoria) {
        return categoriaRepositorio.save(categoria);
    }

    // Elimina una categoría por su ID
    @Override
    public void eliminar(Long id) {
        categoriaRepositorio.deleteById(id);
    }
}