package com.minimarket.app.entidad;

import jakarta.persistence.*;
import lombok.*;

// Define los roles de acceso del sistema
@Entity
@Table(name = "roles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class Rol {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
}
