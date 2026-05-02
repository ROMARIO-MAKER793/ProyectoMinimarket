package com.minimarket.app.controlador;

import com.minimarket.app.entidad.Producto;
import com.minimarket.app.servicio.ProductoServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // <-- Cambiado
@RequestMapping("/api/productos") // <-- Cambiado
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoRestControlador {

    @Autowired
    private ProductoServicio productoServicio;

    // LISTAR PRODUCTOS
    @GetMapping 
    public ResponseEntity<List<Producto>> listar() {
        // En REST ya no enviamos las listas de categorías o marcas aquí. 
        // Angular hará peticiones GET separadas a CategoriaControlador y MarcaControlador si las necesita.
        return ResponseEntity.ok(productoServicio.listarActivos());
    }

    // GUARDAR (CREAR / EDITAR)
    // Usamos POST. En Angular, enviaremos esto como un FormData porque incluye un archivo (imagen)
    @PostMapping("/guardar")
    public ResponseEntity<Map<String, Object>> guardar(
            @Valid @ModelAttribute Producto producto, // Mantenemos ModelAttribute porque recibe FormData, no JSON crudo
            @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean esNuevo = productoServicio.guardar(producto, imagenFile);
            response.put("mensaje", esNuevo ? "Producto creado correctamente" : "Producto editado correctamente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            response.put("error", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            response.put("error", "Ocurrió un error al guardar el producto");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ELIMINAR
    // En REST la convención es usar el verbo DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productoServicio.eliminar(id);
            response.put("mensaje", "Producto eliminado correctamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", "Error al eliminar el producto");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}