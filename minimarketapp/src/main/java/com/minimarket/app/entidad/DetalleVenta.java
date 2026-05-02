package com.minimarket.app.entidad;

import jakarta.persistence.*;
import lombok.*;

// Representa el detalle de productos vendidos en una venta
@Entity
@Table(name = "detalle_ventas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class DetalleVenta {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "venta_id")
	    private Venta venta;

	    @ManyToOne
	    @JoinColumn(name = "producto_id")
	    private Producto producto;

	    private int cantidad;

	    private double precioUnitario;

	    private double subtotal;
}
