package com.minimarket.app.dto;

import lombok.*;

//DTO Para Dashboard

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CategoriaDTO {
	
	private String nombre;
	
	private Long cantidad;

}
