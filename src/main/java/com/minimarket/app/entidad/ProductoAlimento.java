package com.minimarket.app.entidad;

import java.util.Date;

import lombok.Data;

@Data
public class ProductoAlimento extends Producto {
	
	private Date fechaVencimiento;
	
	private String lote;
	
	private String registroSanitario;
	
	private String tipoConservacion; //refrigerado , congelado, ambiente
	
}
