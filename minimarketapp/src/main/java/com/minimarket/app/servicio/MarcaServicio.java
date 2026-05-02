package com.minimarket.app.servicio;

import com.minimarket.app.entidad.Marca;
import java.util.List;

/**
 * Define las operaciones de negocio para la entidad Marca.
 */
public interface MarcaServicio {

    // Obtiene todas las marcas
    List<Marca> listarTodos();

    // Guarda o actualiza una marca
    Marca guardar(Marca marca);

    // Busca una marca por su ID
    Marca buscarPorId(Long id);

    // Elimina una marca por su ID
    void eliminar(Long id);
}