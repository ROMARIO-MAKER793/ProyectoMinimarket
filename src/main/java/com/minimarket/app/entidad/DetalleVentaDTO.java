package com.minimarket.app.entidad;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDTO {
	
    public Long idProducto;
    public String nombreProducto;
    public int cantidad;
    public double precioUnitario;
    public double subtotal;
}
