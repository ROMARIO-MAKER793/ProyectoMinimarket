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

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.time.format.DateTimeFormatter;


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
        
        // Enviamos tanto el ID como el Username del cajero logueado
        model.addAttribute("usuarioId", usuarioLogueado.getId());
        model.addAttribute("usuarioNombre", usuarioLogueado.getUsername()); 
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
    
    // ==========================================
    // EXPORTAR A EXCEL
    // ==========================================
    @GetMapping("/exportar/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=historial_ventas.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Venta> ventas = ventaServicio.listarTodos();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ventas");
        
     // Estilo para la cabecera
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont(); // <-- ¡Aquí está el truco!
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Fila de Cabecera
        Row headerRow = sheet.createRow(0);
        String[] columnas = {"ID", "Fecha", "N° Boleta", "Cajero", "Total (S/)"};
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        // Llenar datos
        int rowIdx = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Venta venta : ventas) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(venta.getId());
            row.createCell(1).setCellValue(venta.getFecha() != null ? venta.getFecha().format(formatter) : "N/A");
            row.createCell(2).setCellValue(venta.getNumeroBoleta());
            row.createCell(3).setCellValue(venta.getUsuario() != null ? venta.getUsuario().getUsername() : "N/A");
            row.createCell(4).setCellValue(venta.getTotal());
        }

        // Autoajustar columnas
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

   
}