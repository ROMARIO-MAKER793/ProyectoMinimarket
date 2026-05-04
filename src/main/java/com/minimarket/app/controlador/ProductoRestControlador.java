package com.minimarket.app.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.minimarket.app.entidad.Producto;
import com.minimarket.app.servicio.ProductoServicio;

@CrossOrigin(origins = "http://localhost:59170")
@RestController
@RequestMapping("/api/productos")
public class ProductoRestControlador {
	
	@Autowired
	private ProductoServicio productoServicio;
	
	
	//LISTAR TODOS LOS PRODUCTOS
	@GetMapping
	public List<Producto> listar(){
		
		return productoServicio.listarTodos();
		
	}

	//LISTAR PRODUCTOS ACTIVOS
	@GetMapping("/activos")
	public List<Producto> listarActivos(){
		return productoServicio.listarActivos();
	}
	
	// BUSCAR POR ID
    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoServicio.buscarPorId(id);
    }

    // CREAR PRODUCTO (sin imagen ya que usamos cloudinary)
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        try {
            productoServicio.guardar(producto, null); // 
            return producto;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear producto: " + e.getMessage());
        }
    }

    // ACTUALIZAR PRODUCTO
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            producto.setId(id);
            productoServicio.guardar(producto, null); // sin imagen
            return producto;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage());
        }
    }

    // ELIMINAR PRODUCTO
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        productoServicio.eliminar(id);
        return "Producto eliminado correctamente";
    }

}
