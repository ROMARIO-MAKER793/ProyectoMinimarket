package com.minimarket.app.entidad;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// Representa una venta realizada en el sistema
@Entity
@Table(name = "ventas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Venta {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private double total;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;
    
    @Column(name = "numero_boleta", unique = true)
    private String numeroBoleta;
}
