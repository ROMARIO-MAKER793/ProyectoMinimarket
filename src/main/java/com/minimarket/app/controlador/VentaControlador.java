package com.minimarket.app.controlador;

import com.minimarket.app.entidad.DetalleVenta;
import com.minimarket.app.entidad.DetalleVentaDTO;
import com.minimarket.app.entidad.Producto;
import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.entidad.Venta;
import com.minimarket.app.entidad.VentaDTO;
import com.minimarket.app.repositorio.DetalleVentaRepositorio;
import com.minimarket.app.repositorio.ProductoRepositorio;
import com.minimarket.app.repositorio.VentaRepositorio;
//import com.minimarket.app.entidad.Producto;
//import com.minimarket.app.entidad.DetalleVenta;
//import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.servicio.VentaServicio;
import com.minimarket.app.servicio.ProductoServicio;
//import com.minimarket.app.servicio.UsuarioServicio;
import com.minimarket.app.servicio.UsuarioServicio;

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
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private VentaRepositorio ventaRepositorio;
   
    @Autowired
    private ProductoRepositorio productoRepositorio;
    
    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;

 

    
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
    
 // NUEVO: GUARDAR VENTA DESDE FRONT-END CON DTO (JSON)
    @PostMapping("/crear")
    @ResponseBody
    public Map<String, Object> crearVenta(@RequestBody VentaDTO ventaDTO) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            // 1️⃣ Crear la venta principal
            Venta venta = new Venta();
            venta.setUsuario(usuarioServicio.buscarPorId(ventaDTO.getUsuarioId())); // Asegúrate de tener el service
            venta.setFecha(LocalDateTime.now());
            venta.setTotal(ventaDTO.getDetalles().stream()
                            .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                            .sum());

            // Guardar venta primero para tener ID
            ventaRepositorio.save(venta);

            // 2️⃣ Crear los detalles de venta
            for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
                Producto producto = productoRepositorio.findById(detalleDTO.getIdProducto())
                                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                // Crear detalle
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setSubtotal(detalleDTO.getCantidad() * detalleDTO.getPrecioUnitario());

                detalleVentaRepositorio.save(detalle);

                // Descontar stock
                producto.setStockActual(producto.getStockActual() - detalleDTO.getCantidad());
                productoRepositorio.save(producto);
            }

            // 3️⃣ Respuesta simple al frontend
            respuesta.put("idVenta", venta.getId());
            respuesta.put("mensaje", "¡Venta creada correctamente!");
            return respuesta;

        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Ocurrió un error al guardar la venta: " + e.getMessage());
            return respuesta;
        }
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