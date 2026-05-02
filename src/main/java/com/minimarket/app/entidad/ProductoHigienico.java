package com.minimarket.app.entidad;

import lombok.Data;

@Data
public class ProductoHigienico extends Producto{
	

	private String tipoPiel;
	
	private boolean esHipoAlergenico;
	
	private String contenido;
	
	private String fragancia;
}
