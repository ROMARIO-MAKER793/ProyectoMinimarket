package com.minimarket.app.entidad;

import jakarta.persistence.*;
import lombok.*;

// Representa la categoría de un producto
@Entity
@Table(name = "categorias")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class Categoria {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String nombre;

}
