package com.minimarket.app.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.minimarket.app.servicio.ProductoServicio;

@Controller
@RequestMapping("/user")
public class UserProductoControlador {
	

    @Autowired
    private ProductoServicio productoServicio;

    @GetMapping("/productos")
    public String listarProductosUser(Model model) {
        model.addAttribute("productos", productoServicio.listarActivos());
        model.addAttribute("content", "user/productos");
        return "layaout/admin_layaout"; // el mismo layout
    }

}
