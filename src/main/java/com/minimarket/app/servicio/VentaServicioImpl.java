package com.minimarket.app.servicio;

import com.minimarket.app.entidad.Venta;
import com.minimarket.app.entidad.VentaDTO;
import com.minimarket.app.dto.ReporteVentaMesDTO;
import com.minimarket.app.entidad.DetalleVenta;
import com.minimarket.app.entidad.DetalleVentaDTO;
import com.minimarket.app.entidad.Producto;
import com.minimarket.app.entidad.Usuario;

import com.minimarket.app.repositorio.ProductoRepositorio;
import com.minimarket.app.repositorio.VentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class VentaServicioImpl implements VentaServicio {

    @Autowired
    private VentaRepositorio ventaRepositorio;


    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    
   
    @Autowired
    private ProductoRepositorio productoRepositorio;
    


 
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

   
    // Crear venta completa con detalles, total, stock y 
   
    @Override
    @Transactional
    public Map<String, Object> crearVentaDTO(VentaDTO ventaDTO) throws IOException {
        Map<String, Object> respuesta = new HashMap<>();

        Usuario usuario = usuarioServicio.buscarPorId(ventaDTO.getUsuarioId());

        // Crear venta principal
        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setFecha(LocalDateTime.now());
        venta.setNumeroBoleta(generarNumeroBoleta());

        double total = 0;
        List<DetalleVenta> detalles = new ArrayList<>();

        for (DetalleVentaDTO dto : ventaDTO.getDetalles()) {
            Producto producto = productoRepositorio.findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

         // Validar stock
            if (dto.getCantidad() > producto.getStockActual()) {
                throw new com.minimarket.app.excepciones.StockInsuficienteException(
                    "Solo quedan " + producto.getStockActual() + " unidades de " + producto.getNombre() + " en almacén."
                );
            }

            // Crear detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(dto.getCantidad());
            detalle.setPrecioUnitario(dto.getPrecioUnitario());
            detalle.setSubtotal(dto.getCantidad() * dto.getPrecioUnitario());

            detalles.add(detalle);

            // Descontar stock
            producto.setStockActual(producto.getStockActual() - dto.getCantidad());
            productoRepositorio.save(producto);

            // Acumular total
            total += detalle.getSubtotal();
        }

        // Guardar venta
        venta.setDetalles(detalles);
        venta.setTotal(total);
        ventaRepositorio.save(venta);

        respuesta.put("idVenta", venta.getId());
        respuesta.put("numeroBoleta", venta.getNumeroBoleta());//agrego para obtener el # de boleta pq no se muestra en el comprobante
        respuesta.put("mensaje", "¡Venta creada correctamente!");

        return respuesta;
    }

    
 // Generar número de boleta secuencial (VERSIÓN CORREGIDA)
    private String generarNumeroBoleta() {
        Long maxId = ventaRepositorio.obtenerMaximoId();
        // Si maxId es null (la tabla está vacía), el siguiente será 1. Si no, le sumamos 1 al máximo.
        long nextId = (maxId == null) ? 1 : maxId + 1; 
        return String.format("B-%05d", nextId); // Ej: B-00001, B-00002
    }

    //Metodo para reportes

	@Override
	public List<ReporteVentaMesDTO> getReporteVentasDelMes() {
		return ventaRepositorio.obtenerVentasDelMes();
	}


	@Override
	public double obtenerTotalVentasHoy() {
		
	    LocalDateTime inicio = LocalDateTime.now().toLocalDate().atStartOfDay();
	    LocalDateTime fin = inicio.plusDays(1).minusSeconds(1);

	    return ventaRepositorio.totalVentasEntreFechas(inicio, fin);
	}


	@Override
	public double obtenerTotalVentasMes() {
		
		return ventaRepositorio.totalVentasMesActual();
	}


	@Override
	public Long obtenerCantidadVentasMes() {
		
		return ventaRepositorio.cantidadVentasMesActual();
	}


	@Override
	public Long obtenerTotalProductosVendidosMes() {
		
		return ventaRepositorio.totalProductosVendidosMesActual();
	}


	@Override
	public List<Object[]> obtenerVentasPorDiaDelMes() {
		
		return ventaRepositorio.ventasPorDiaDelMes();
	}


	@Override
	public List<Object[]> obtenerTop5ProductosMes() {
		
		return ventaRepositorio.topProductosMes()
				.stream()
				.limit(5)
				.toList();
	}
	
	
	
	


}