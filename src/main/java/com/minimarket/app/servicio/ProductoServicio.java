package com.minimarket.app.servicio;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.minimarket.app.entidad.Producto;


public interface ProductoServicio {
	
	  // Listar todos los productos
    List<Producto> listarTodos();
    
    //Lista solo los Activos
    List<Producto> listarActivos(); 

    // Buscar producto por ID
    Producto buscarPorId(Long id);

    // Registrar o actualizar producto
    boolean guardar(Producto producto ,MultipartFile imagenFile)throws IOException;

    // Eliminar producto por ID
    void eliminar(Long id);
}
