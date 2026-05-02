package com.minimarket.app.entidad;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Table(name = "productos_alimentos")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoAlimento extends Producto {
	
	private Date fechaVencimiento;
	
	private String lote;
	
	private String registroSanitario;
	
	private String tipoConservacion; //refrigerado , congelado, ambiente
	
}
