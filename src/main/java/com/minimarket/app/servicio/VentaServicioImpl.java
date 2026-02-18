package com.minimarket.app.servicio;

import com.minimarket.app.entidad.Venta;
import com.minimarket.app.entidad.DetalleVenta;
import com.minimarket.app.entidad.Producto;
import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.repositorio.VentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class VentaServicioImpl implements VentaServicio {

    @Autowired
    private VentaRepositorio ventaRepositorio;

    @Autowired
    private ProductoServicio productoServicio;

 
    // Guardar venta simple
    
    @Override
    public Venta guardar(Venta venta) {
        return ventaRepositorio.save(venta);
    }

 
    // Listar todas las ventas
 
    @Override
    public List<Venta> listarTodos() {
        return ventaRepositorio.findAll();
    }

   
    // Buscar venta por ID
  
    @Override
    public Venta buscarPorId(Long id) {
        return ventaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada con ID: " + id));
    }

 
    // Eliminar venta
  
    @Override
    public void eliminar(Long id) {
        ventaRepositorio.deleteById(id);
    }


    // Validar stock de productos antes de vender
   
    @Override
    public boolean validarStock(Map<Producto, Integer> items) {
        for (Map.Entry<Producto, Integer> entry : items.entrySet()) {
            if (entry.getKey().getStockActual() < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

   
    // Crear venta completa con detalles, total, stock y número de boleta
   
    @Override
    public Venta crearVentaConDetails(List<DetalleVenta> detalles, Usuario usuario) {

        // 1️⃣ Validar stock suficiente
        if (!validarStock(detalles.stream()
                .collect(Collectors.toMap(DetalleVenta::getProducto, DetalleVenta::getCantidad)))) {
            throw new IllegalArgumentException("No hay suficiente stock para uno o más productos");
        }

        // Crear objeto Venta y asignar usuario y fecha
        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFecha(LocalDateTime.now());

        double total = 0;

        // Recorrer detalles: calcular total y descontar stock
        for (DetalleVenta dv : detalles) {
            Producto p = dv.getProducto();
            int cantidadVendida = dv.getCantidad();

            // Calcular subtotal
            total += p.getPrecio() * cantidadVendida;

            // Descontar stock
            p.setStockActual(p.getStockActual() - cantidadVendida);

            // Guardar producto actualizado
            try {
                productoServicio.guardar(p, null); // null = no hay archivo de imagen
            } catch (IOException e) {
                throw new RuntimeException("Error guardando producto: " + p.getNombre(), e);
            }

            // Vincular detalle a la venta
            dv.setVenta(venta);
        }

        // 4️⃣ Asignar lista de detalles y total
        venta.setDetalles(detalles);
        venta.setTotal(total);

        // 5️⃣ Generar número de boleta secuencial
        venta.setNumeroBoleta(generarNumeroBoleta());

        // 6️⃣ Guardar venta final
        return guardar(venta);
    }

    
    // Generar número de boleta secuencial
    
    private String generarNumeroBoleta() {
        long count = ventaRepositorio.count() + 1;
        return String.format("B-%05d", count); // Ej: B-00001, B-00002
    }
}