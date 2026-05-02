package com.minimarket.app.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "productos_higienicos")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoHigienico extends Producto{
	

	private String tipoPiel;
	
	private boolean esHipoAlergenico;
	
	private String contenido;
	
	private String fragancia;
}
