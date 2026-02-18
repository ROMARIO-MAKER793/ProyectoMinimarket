package com.minimarket.app.entidad;


import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
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
    
    @Min(value = 20, message = "El stock debe ser al menos 20")
    @Column(name = "stock_actual")
    private int stockActual;

    @Column(name = "stock_minimo")
    private int stockMinimo = 20;
    
    @Column(nullable = false)
    private boolean activo = true;

    // NUEVO CAMPO IMAGEN
    private String imagenUrl;
    
    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    
    @NotNull(message = "La marca es obligatoria")
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;
}