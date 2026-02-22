package com.minimarket.app.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReporteVentaMesDTO {
	
	private String producto;
	private Long cantidadVendida;
	private double totalVendido;
	private LocalDateTime fechaUltimaVenta;

}
