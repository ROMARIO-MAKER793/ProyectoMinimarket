package com.minimarket.app.servicio;

import java.io.IOException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.minimarket.app.entidad.Producto;
import com.minimarket.app.repositorio.ProductoRepositorio;

@Service
public class ProductoServicioImpl implements ProductoServicio {
	

    @Autowired
    private ProductoRepositorio productoRepositorio;
    

    @Autowired
    private CategoriaServicio categoriaServicio;
    
    @Autowired
    private MarcaServicio marcaServicio;
    
    @Autowired
    private ImagenService imagenService;
    


    // Retorna todos los productos
    @Override
    public List<Producto> listarTodos() {
        return productoRepositorio.findAll();
    }
    
    public List<Producto> listarActivos(){
    	return productoRepositorio.findByActivoTrue();
    }

    // Busca un producto por su ID
    @Override
    public Producto buscarPorId(Long id) {
        return productoRepositorio.findById(id).orElse(null);
    }

   

    // Elimina un producto por su ID
    @Override
    public void eliminar(Long id) {
    	Producto producto = productoRepositorio.findById(id).orElseThrow();
    	producto.setActivo(false);
    	productoRepositorio.save(producto);
        
    }

	@Override
	public boolean guardar(Producto producto, MultipartFile imagenFile) throws IOException {
        boolean esNuevo = (producto.getId() == null);

        // Validar categoría
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una categoría");
        } else {
            producto.setCategoria(categoriaServicio.buscarPorId(producto.getCategoria().getId()));
        }

        // Validar marca
        if (producto.getMarca() == null || producto.getMarca().getId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una marca");
        } else {
            producto.setMarca(marcaServicio.buscarPorId(producto.getMarca().getId()));
        }

        // Manejo de imagen
        if (imagenFile != null && !imagenFile.isEmpty()) {
        	
        	String imagenUrl = imagenService.subirImagen(imagenFile);
        	producto.setImagenUrl(imagenUrl);
			/*
			 * antes de clodinary
			 * String nombreArchivo = imagenFile.getOriginalFilename(); Path ruta =
			 * Paths.get("src/main/resources/static/img/productos/" + nombreArchivo);
			 * Files.write(ruta, imagenFile.getBytes());
			 * producto.setImagenUrl("/img/productos/" + nombreArchivo);
			 */
        //    
        } else if (!esNuevo) {
            // Mantener imagen existente al editar
            Producto existente = buscarPorId(producto.getId());
            if (existente != null) {
                producto.setImagenUrl(existente.getImagenUrl());
            }
        }

        // Guardar en BD
        productoRepositorio.save(producto);
        return esNuevo;
    }

	@Override
	public boolean actualizarStock(Producto producto) {
		if(producto == null) return false;
		productoRepositorio.save(producto);
		
		return true;
	}

	@Override
	public List<Producto> obtenerProductoEnAlerta() {
		
		return productoRepositorio.productoEnAlerta();
	}

	
}
