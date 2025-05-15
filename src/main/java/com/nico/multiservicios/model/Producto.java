package com.nico.multiservicios.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Producto")


public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre_producto")
    private String nombreProducto;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "stock_disponible")
    private Integer stock; //cantidad de productos a ingresar

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_stock")
    private EstadoStock estadoStock;

    public Producto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public EstadoStock getEstadoStock() {
        return estadoStock;
    }

    public void setEstadoStock(EstadoStock estadoStock) {
        this.estadoStock = estadoStock;
    }
}
