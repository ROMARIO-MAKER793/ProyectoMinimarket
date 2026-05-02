package com.minimarket.app.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "productos_agranel")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductoAgranel extends Producto {
    
    private String unidadMedida; //ej:"Kg","Gramos"
}