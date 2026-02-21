package com.minimarket.app.controlador;

import com.minimarket.app.dto.CategoriaDTO;
import com.minimarket.app.dto.ProductoVendidoDTO;
import com.minimarket.app.dto.VentaMesDTO;
import com.minimarket.app.repositorio.CategoriaRepositorio;
import com.minimarket.app.repositorio.DetalleVentaRepositorio;
import com.minimarket.app.repositorio.ProductoRepositorio;
import com.minimarket.app.repositorio.UsuarioRepositorio;
import com.minimarket.app.repositorio.VentaRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private VentaRepositorio ventaRepositorio;
    
    @Autowired
    private CategoriaRepositorio categoriaRepositorio;
    
    @Autowired
    private DetalleVentaRepositorio detalleVentaRepositorio;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
    	

        // Totales que se mostraran en el dashboard
        model.addAttribute("totalProductos", productoRepositorio.countByActivoTrue());
        model.addAttribute("totalUsuarios", usuarioRepositorio.count());
        model.addAttribute("totalCategorias", categoriaRepositorio.count());
        model.addAttribute("ventasMes", ventaRepositorio.totalVendidoMes());
        model.addAttribute("actividadesRecientes", ventaRepositorio.findTop5ByOrderByFechaDesc());

      
        // PRODUCTOS MÁS VENDIDOS

        List<Object[]> productos = detalleVentaRepositorio.productosMasVendidos();
        List<ProductoVendidoDTO> productosDTO = new ArrayList<>();

        for (Object[] obj : productos) {
            productosDTO.add(
                new ProductoVendidoDTO(
                    (String) obj[0],
                    ((Number) obj[1]).longValue()
                )
            );
        }

        model.addAttribute("productosMasVendidos", productosDTO);
     
        // DISTRIBUCIÓN POR CATEGORÍA
       
        List<Object[]> categorias = productoRepositorio.distribucionCategorias();
        List<CategoriaDTO> categoriasDTO = new ArrayList<>();

        for (Object[] obj : categorias) {
            categoriasDTO.add(
                new CategoriaDTO(
                    (String) obj[0],
                    ((Number) obj[1]).longValue()
                )
            );
        }

        model.addAttribute("distribucionCategorias", categoriasDTO);

    
        // VENTAS POR MES
       
        List<Object[]> ventas = ventaRepositorio.ventasPorMes();
        
        System.out.println("VENTAS POR MES:");
        for (Object[] v : ventas) {
            System.out.println("Mes: " + v[0] + " Total: " + v[1]);
        }
        List<VentaMesDTO> ventasDTO = new ArrayList<>();

        for (Object[] obj : ventas) {
            ventasDTO.add(
                new VentaMesDTO(
                    ((Number) obj[0]).intValue(),
                    Math.round(((Number) obj[1]).doubleValue() * 100.0) / 100.0
                )
            );
        }

        model.addAttribute("ventasPorMes", ventasDTO);
        
        model.addAttribute("content", "admin/dashboard");

        return "layaout/admin_layaout";
    }
}