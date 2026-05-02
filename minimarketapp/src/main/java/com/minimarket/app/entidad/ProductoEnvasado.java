package com.minimarket.app.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Table(name = "productos_envasados")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoEnvasado extends Producto {
    
    private String codigoBarras;
    private LocalDate fechaVencimiento;
}