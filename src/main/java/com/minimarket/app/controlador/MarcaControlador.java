package com.minimarket.app.controlador;

import com.minimarket.app.entidad.Marca;
import com.minimarket.app.servicio.MarcaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/marcas")
public class MarcaControlador {

    @Autowired
    private MarcaServicio marcaServicio;

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("marcas", marcaServicio.listarTodos());
        return "marca/listar";
    }

    // FORM NUEVO
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("marca", new Marca());
        return "marca/formulario";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Marca marca) {
        marcaServicio.guardar(marca);
        return "redirect:/marcas";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("marca", marcaServicio.buscarPorId(id));
        return "marca/formulario";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        marcaServicio.eliminar(id);
        return "redirect:/marcas";
    }
}