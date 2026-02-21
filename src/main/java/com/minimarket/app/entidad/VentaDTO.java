package com.minimarket.app.entidad;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

//DTO PARA TRAER SOLO LOS DATOS QUE NECESITAMOS


@Data
@NoArgsConstructor
@AllArgsConstructor

public class VentaDTO {
	
    public Long id;                     // ID de la venta
    public String numeroBoleta;         // Número de boleta generado
    public LocalDateTime fecha;         // Fecha de la venta
    public Double total;                // Total de la venta
    public Long usuarioId;              // ID del usuario que realizó la venta
    public List<DetalleVentaDTO> detalles; // Lista de detalles de la venta
    
    
    
}
