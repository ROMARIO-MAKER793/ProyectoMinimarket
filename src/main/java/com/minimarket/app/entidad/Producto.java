package com.minimarket.app.entidad;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@NoArgsConstructor @AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) //estrategia de tablas separadas unidas por ID
@Data // Lombok 
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private Double precio;
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock_actual")
    private int stockActual;

    @Column(name = "stock_minimo")
    private int stockMinimo = 20;
    
    @Column(nullable = false)
    private boolean activo = true;

    private String imagenUrl;
    
    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY) // Recomendado agregar LAZY para mejorar rendimiento
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    
    @NotNull(message = "La marca es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;
}