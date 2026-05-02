package com.minimarket.app.controlador;

import com.minimarket.app.entidad.Categoria;
import com.minimarket.app.servicio.CategoriaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaControlador {

    @Autowired
    private CategoriaServicio categoriaServicio;

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaServicio.listarTodas());
        return "categoria/listar";
    }

    // FORM NUEVO
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/formulario";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        categoriaServicio.guardar(categoria);
        return "redirect:/categorias";
    }

    // FORM EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categoriaServicio.buscarPorId(id));
        return "categoria/formulario";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        categoriaServicio.eliminar(id);
        return "redirect:/categorias";
    }
}