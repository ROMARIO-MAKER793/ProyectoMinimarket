package com.minimarket.app.servicio;

import java.util.List;
import com.minimarket.app.entidad.Categoria;

public interface CategoriaServicio {

    // Lista todas las categorías
    List<Categoria> listarTodas();

    // Busca una categoría por ID
    Categoria buscarPorId(Long id);

    // Registra o actualiza una categoría
    Categoria guardar(Categoria categoria);

    // Elimina una categoría por ID
    void eliminar(Long id);
}