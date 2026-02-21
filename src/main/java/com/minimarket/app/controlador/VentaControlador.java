package com.minimarket.app.controlador;


import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.entidad.Venta;
import com.minimarket.app.entidad.VentaDTO;

import com.minimarket.app.servicio.VentaServicio;
import com.minimarket.app.servicio.ProductoServicio;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("admin/ventas")
public class VentaControlador {

    @Autowired
    private VentaServicio ventaServicio;

    @Autowired
    private ProductoServicio productoServicio;
    
    
    // LISTAR VENTAS / VENTANA DE VENTA
   
    @GetMapping
    public String listar(Model model, @AuthenticationPrincipal Usuario usuarioLogueado) {
        model.addAttribute("productos", productoServicio.listarActivos()); // productos para vender
        model.addAttribute("venta", new Venta());
        model.addAttribute("usuarioId", usuarioLogueado.getId());
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
    
 // GUARDAR VENTA DESDE FRONT-END CON DTO (JSON) CON VALIDACIÓN DE STOCK
    @PostMapping("/crear")
    @ResponseBody
    public Map<String, Object> crearVenta(@RequestBody VentaDTO ventaDTO) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            // Llamada al servicio que maneja toda la lógica
            Map<String, Object> resultado = ventaServicio.crearVentaDTO(ventaDTO);
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Ocurrió un error al guardar la venta: " + e.getMessage());
            return respuesta;
        }
    }

    
    // HISTORIAL DE VENTAS FALTA CREAR BOTON Y MODAL Y REPORTE
    
    @GetMapping("/historial")
    public String historial(Model model) {
        List<Venta> ventas = ventaServicio.listarTodos();
        model.addAttribute("ventas", ventas);
        model.addAttribute("content", "admin/historial_ventas");
        return "layaout/admin_layaout";
    }
}