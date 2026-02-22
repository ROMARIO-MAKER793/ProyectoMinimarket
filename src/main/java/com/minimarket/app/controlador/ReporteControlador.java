package com.minimarket.app.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.minimarket.app.dto.ReporteVentaMesDTO;
import com.minimarket.app.entidad.Categoria;
import com.minimarket.app.entidad.Producto;
import com.minimarket.app.servicio.CategoriaServicio;
import com.minimarket.app.servicio.ProductoServicio;
import com.minimarket.app.servicio.VentaServicio;

@Controller
@RequestMapping("/admin")
public class ReporteControlador {

   
	@Autowired
	private VentaServicio ventaServicio;
	

    @Autowired
    private ProductoServicio productoServicio;
    
    @Autowired
    private CategoriaServicio categoriaServicio;
    
    

	
	@GetMapping("/reportes")
	public String reporteVentaMes(Model model) {
		// Obtener las ventas del mes
        List<ReporteVentaMesDTO> ventasDelMes = ventaServicio.getReporteVentasDelMes();
        
        List<Object[]> topProductos = ventaServicio.obtenerTop5ProductosMes();
        
        List<Producto> productosAlerta = productoServicio.obtenerProductoEnAlerta();
        
        
        List<Object[]> ventasPorDia = ventaServicio.obtenerVentasPorDiaDelMes();

        // Obtener productos y categorías (si los quieres mostrar en filtros o info extra)
        List<Producto> productos = productoServicio.listarTodos();
        List<Categoria> categorias = categoriaServicio.listarTodas();
        
        Double ventasHoy = ventaServicio.obtenerTotalVentasHoy();
        
        Double ventasMes = ventaServicio.obtenerTotalVentasMes();
        
        Long cantidadVentas = ventaServicio.obtenerCantidadVentasMes();
        
        Long productosVendidos = ventaServicio.obtenerTotalProductosVendidosMes();
        

        // Añadir al modelo
        model.addAttribute("ventasHoy", ventasHoy);
        model.addAttribute("ventasMes", ventasMes);
        model.addAttribute("cantidadVentas", cantidadVentas);
        model.addAttribute("productosVendidos", productosVendidos);
        model.addAttribute("topProductos", topProductos);
        model.addAttribute("productosAlerta", productosAlerta);
        model.addAttribute("ventasPorDia", ventasPorDia);
        model.addAttribute("ventas", ventasDelMes);
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);

        // Indica al fragmento Thymeleaf que se cargará como contenido
        model.addAttribute("content", "admin/reportes");

        // Retornar el layout general
        return "layaout/admin_layaout";
	}
	

}
