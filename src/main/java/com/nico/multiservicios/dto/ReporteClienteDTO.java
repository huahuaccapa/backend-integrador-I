// com.nico.multiservicios.dto.ReporteClienteDTO
package com.nico.multiservicios.dto;

import com.nico.multiservicios.model.MetodoPago;

public class ReporteClienteDTO {
    private String ruc;
    private String nombre;
    private MetodoPago metodoPago;
    private String fechaUltimaCompra;
    private Integer comprasTotales;

    public ReporteClienteDTO(String ruc, String nombre, MetodoPago metodoPago, String fechaUltimaCompra, Integer comprasTotales) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.metodoPago = metodoPago;
        this.fechaUltimaCompra = fechaUltimaCompra;
        this.comprasTotales = comprasTotales;
    }

    // Getters y Setters
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public String getFechaUltimaCompra() { return fechaUltimaCompra; }
    public void setFechaUltimaCompra(String fechaUltimaCompra) { this.fechaUltimaCompra = fechaUltimaCompra; }
    public Integer getComprasTotales() { return comprasTotales; }
    public void setComprasTotales(Integer comprasTotales) { this.comprasTotales = comprasTotales; }
}