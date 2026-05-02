package com.minimarket.app;

import com.minimarket.app.entidad.Producto;
import com.minimarket.app.entidad.Usuario;
import com.minimarket.app.entidad.VentaDTO;
import com.minimarket.app.entidad.DetalleVentaDTO;
import com.minimarket.app.repositorio.ProductoRepositorio;
import com.minimarket.app.repositorio.VentaRepositorio;
import com.minimarket.app.servicio.UsuarioServicio;
import com.minimarket.app.servicio.VentaServicioImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MiniMarketAppApplicationTests {

    // Simulamos los repositorios para no tocar la base de datos real (BD de prueba)
    @Mock
    private VentaRepositorio ventaRepositorio;

    @Mock
    private ProductoRepositorio productoRepositorio;

    @Mock
    private UsuarioServicio usuarioServicio;

    // Inyectamos los mocks en tu servicio real
    @InjectMocks
    private VentaServicioImpl ventaServicio;

    @Test
    void testCrearVentaDescuentaStockCorrectamente() throws Exception {
        // 1. Preparar el escenario (Given)
        Usuario mockUser = new Usuario();
        mockUser.setId(1L);
        mockUser.setUsuario("cajero_test");

        Producto mockProducto = new Producto();
        mockProducto.setId(10L);
        mockProducto.setNombre("Aceite Primor");
        mockProducto.setPrecio(8.5);
        mockProducto.setStockActual(50); // Stock inicial simulado

        when(usuarioServicio.buscarPorId(1L)).thenReturn(mockUser);
        when(productoRepositorio.findById(10L)).thenReturn(Optional.of(mockProducto));
        
        // Simulamos la compra de 2 unidades que enviaría el Frontend (ventas.js)
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setUsuarioId(1L);
        ventaDTO.setTotal(17.0);

        DetalleVentaDTO detalle = new DetalleVentaDTO();
        detalle.setIdProducto(10L);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(8.5);
        ventaDTO.setDetalles(Arrays.asList(detalle));

        // 2. Ejecutar la acción (When)
        Map<String, Object> respuesta = ventaServicio.crearVentaDTO(ventaDTO);

        // 3. Verificar los resultados (Then)
        assertNotNull(respuesta, "La respuesta no debe ser nula");
        assertEquals("¡Venta creada correctamente!", respuesta.get("mensaje"));
        
        // VERIFICACIÓN CRÍTICA: El stock bajó de 50 a 48 matemáticamente
        assertEquals(48, mockProducto.getStockActual(), "El stock no se descontó correctamente");
        
        // Verificar que el servicio llamó al método 'save' de los repositorios
        verify(ventaRepositorio, times(1)).save(any());
        verify(productoRepositorio, times(1)).save(mockProducto);
    }
}