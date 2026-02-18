package com.minimarket.app.controlador;

import com.minimarket.app.entidad.DetalleVenta;
import com.minimarket.app.entidad.Producto;
import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.entidad.Venta;
import com.minimarket.app.entidad.VentaDTO;
//import com.minimarket.app.entidad.Producto;
//import com.minimarket.app.entidad.DetalleVenta;
//import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.servicio.VentaServicio;
import com.minimarket.app.servicio.ProductoServicio;
//import com.minimarket.app.servicio.UsuarioServicio;
import com.minimarket.app.servicio.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/ventas")
public class VentaControlador {

    @Autowired
    private VentaServicio ventaServicio;

    @Autowired
    private ProductoServicio productoServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;
   

 

    
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
    
 // NUEVO: GUARDAR VENTA DESDE FRONT-END CON DTO (JSON)
    @ResponseBody
    @PostMapping("/crear")
    public Venta crearVenta(@RequestBody VentaDTO ventaDTO) {
        // 1️⃣ Obtener el usuario completo desde el ID
        Usuario usuario = usuarioServicio.buscarPorId(ventaDTO.getUsuarioId());

        // 2️⃣ Mapear los DetalleVentaDTO a DetalleVenta real
        List<DetalleVenta> detallesEntidad = ventaDTO.getDetalles().stream().map(dto -> {
            DetalleVenta dv = new DetalleVenta();
            Producto producto = productoServicio.buscarPorId(dto.getIdProducto()); // trae el producto real
            dv.setProducto(producto);
            dv.setCantidad(dto.getCantidad());
            dv.setPrecioUnitario(producto.getPrecio()); // Precio unitario desde Producto
            return dv;
        }).collect(Collectors.toList());

        //Llamar al servicio que valida stock, calcula total, asigna boleta y guarda
        return ventaServicio.crearVentaConDetails(detallesEntidad, usuario);
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