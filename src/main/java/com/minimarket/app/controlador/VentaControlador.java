package com.minimarket.app.controlador;

import com.minimarket.app.entidad.Venta;
//import com.minimarket.app.entidad.Producto;
//import com.minimarket.app.entidad.DetalleVenta;
//import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.servicio.VentaServicio;
import com.minimarket.app.servicio.ProductoServicio;
//import com.minimarket.app.servicio.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("admin/ventas")
public class VentaControlador {

    @Autowired
    private VentaServicio ventaServicio;

    @Autowired
    private ProductoServicio productoServicio;

  //  @Autowired
   // private UsuarioServicio usuarioServicio;

    
    // LISTAR VENTAS / VENTANA DE VENTA
   
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoServicio.listarActivos()); // productos para vender
        model.addAttribute("venta", new Venta());
        model.addAttribute("content", "admin/ventas"); // indica el fragment a cargar
        return "layaout/admin_layaout"; 
    }

  
    // GUARDAR VENTA
   
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta, RedirectAttributes redirectAttributes) {

       // aquí validaremos stock, calcularemos total y generaremos numero_boleta
        venta.setFecha(LocalDateTime.now());

        ventaServicio.guardar(venta);

        redirectAttributes.addFlashAttribute("success", "Venta registrada correctamente");

        return "redirect:/admin/ventas";
    }

    
    // HISTORIAL DE VENTAS 
    
    @GetMapping("/historial")
    public String historial(Model model) {
        List<Venta> ventas = ventaServicio.listarTodos();
        model.addAttribute("ventas", ventas);
        model.addAttribute("content", "admin/historial_ventas");
        return "layaout/admin_layaout";
    }
}