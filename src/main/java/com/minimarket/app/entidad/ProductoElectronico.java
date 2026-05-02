package com.minimarket.app.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "productos_electronicos")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoElectronico extends Producto {
	
	private int garantiaMeses;
	
	private String voltaje;
	
	private String potencia;
	
	private String fabricante;
	
}
