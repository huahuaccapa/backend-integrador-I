// com.nico.multiservicios.repository.impl.ProductoRepositoryImpl
package com.nico.multiservicios.repository.impl;

import com.nico.multiservicios.dto.ReporteInventarioDTO;
import com.nico.multiservicios.dto.ReporteStockBajoDTO;
import com.nico.multiservicios.repository.custom.ProductoRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProductoRepositoryImpl implements ProductoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReporteInventarioDTO> findInventarioReporte(Date fechaInicio, Date fechaFin) {
        String queryStr = "SELECT new com.nico.multiservicios.dto.ReporteInventarioDTO(" +
                "CAST(p.id AS string), p.nombreProducto, p.precioVenta, p.stock, p.categoria, " +
                "(SELECT pr.nombre FROM Proveedor pr WHERE pr.id = " +
                "(SELECT MIN(pe.proveedorId) FROM Pedido pe JOIN pe.detallePedido dp WHERE dp.modelo = p.nombreProducto)), " +
                "DATE_FORMAT(p.fechaAdquisicion, '%d/%m/%Y')) " +
                "FROM Producto p " +
                "WHERE (:fechaInicio IS NULL OR p.fechaAdquisicion >= :fechaInicio) " +
                "AND (:fechaFin IS NULL OR p.fechaAdquisicion <= :fechaFin)";

        return entityManager.createQuery(queryStr, ReporteInventarioDTO.class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
    }

    @Override
    public List<ReporteStockBajoDTO> findProductosStockBajo() {
        String queryStr = "SELECT new com.nico.multiservicios.dto.ReporteStockBajoDTO(" +
                "CAST(p.id AS string), p.nombreProducto, p.precioVenta, p.stock, p.stockMinimo, " +
                "(SELECT pr.nombre FROM Proveedor pr WHERE pr.id = " +
                "(SELECT MIN(pe.proveedorId) FROM Pedido pe JOIN pe.detallePedido dp WHERE dp.modelo = p.nombreProducto))) " +
                "FROM Producto p WHERE p.stock <= p.stockMinimo";

        return entityManager.createQuery(queryStr, ReporteStockBajoDTO.class)
                .getResultList();
    }

    @Override
    public BigDecimal getTotalInventario() {
        String queryStr = "SELECT COALESCE(SUM(p.precioCompra * p.stock), 0) FROM Producto p";
        return entityManager.createQuery(queryStr, BigDecimal.class)
                .getSingleResult();
    }

    @Override
    public Long getCantidadProductos() {
        return entityManager.createQuery("SELECT COUNT(p) FROM Producto p", Long.class)
                .getSingleResult();
    }
}