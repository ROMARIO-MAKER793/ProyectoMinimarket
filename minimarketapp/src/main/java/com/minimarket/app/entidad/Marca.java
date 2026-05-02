package com.minimarket.app.entidad;


import jakarta.persistence.*;
import lombok.*;

// Representa la marca de un producto
@Entity
@Table(name = "marcas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Marca {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String nombre;

}
