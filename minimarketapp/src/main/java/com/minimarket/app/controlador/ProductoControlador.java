package com.minimarket.app.controlador;

import com.minimarket.app.entidad.Producto;
import com.minimarket.app.servicio.ProductoServicio;

import jakarta.validation.Valid;

import com.minimarket.app.servicio.CategoriaServicio;
import com.minimarket.app.servicio.MarcaServicio;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("admin/productos")
public class ProductoControlador {

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private CategoriaServicio categoriaServicio;
    
    @Autowired
    private MarcaServicio marcaServicio;


    // LISTAR PRODUCTOS
	
	  @GetMapping 
	  public String listar(Model model) {
	  
	  model.addAttribute("productos", productoServicio.listarActivos());
	  model.addAttribute("producto", new Producto());
	  model.addAttribute("categorias", categoriaServicio.listarTodas());
	  model.addAttribute("marcas", marcaServicio.listarTodos());
	  
	  model.addAttribute("content", "admin/productos");
	  
	  return "layaout/admin_layaout"; 
	  
	  }
	  
 
    // GUARDAR (CREAR / EDITAR)
   
	  @PostMapping("/guardar")
	    public String guardar(
	            @Valid @ModelAttribute Producto producto,
	            BindingResult result,
	            @RequestParam MultipartFile imagenFile,
	            RedirectAttributes redirectAttributes
	    ) {
	        try {
	            // Llamamos al servicio, toda la lógica de validación y guardado va allí
	            boolean esNuevo = productoServicio.guardar(producto, imagenFile);
	            if (esNuevo) {
	                redirectAttributes.addFlashAttribute("success", "Producto creado correctamente");
	            } else {
	                redirectAttributes.addFlashAttribute("success", "Producto editado correctamente");
	            }
	        } catch (IllegalArgumentException ex) {
	            // Captura errores de validación (categoría/marca obligatoria)
	            redirectAttributes.addFlashAttribute("error", ex.getMessage());
	        } catch (Exception ex) {
	            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar el producto");
	            ex.printStackTrace();
	        }
	        return "redirect:/admin/productos";

       
    }

   
    // ELIMINAR
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        productoServicio.eliminar(id);
        

        redirectAttributes.addFlashAttribute("success",
                "Producto eliminado correctamente");

        return "redirect:/admin/productos";
        
    }
}