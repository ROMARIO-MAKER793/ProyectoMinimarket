package com.minimarket.app.entidad;

import lombok.Data;

@Data
public class ProductoElectronico extends Producto {
	
	private int garantiaMeses;
	
	private String voltaje;
	
	private String potencia;
	
	private String fabricante;
	
}
