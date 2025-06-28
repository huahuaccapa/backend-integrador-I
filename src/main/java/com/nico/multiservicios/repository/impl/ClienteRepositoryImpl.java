// com.nico.multiservicios.repository.impl.ClienteRepositoryImpl
package com.nico.multiservicios.repository.impl;

import com.nico.multiservicios.dto.ReporteClienteDTO;
import com.nico.multiservicios.repository.custom.ClienteRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public class ClienteRepositoryImpl implements ClienteRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReporteClienteDTO> findClientesReporte() {
        String queryStr = "SELECT new com.nico.multiservicios.dto.ReporteClienteDTO(" +
                "c.identificacion, c.nombre, " +
                "(SELECT v.metodoPago FROM Venta v WHERE v.cliente = c ORDER BY v.fechaVenta DESC LIMIT 1), " +
                "(SELECT DATE_FORMAT(MAX(v.fechaVenta), '%d/%m/%Y') FROM Venta v WHERE v.cliente = c), " +
                "(SELECT COUNT(v) FROM Venta v WHERE v.cliente = c)) " +
                "FROM Cliente c";

        return entityManager.createQuery(queryStr, ReporteClienteDTO.class)
                .getResultList();
    }

    @Override
    public Long countClientesRegistrados() {
        return entityManager.createQuery("SELECT COUNT(c) FROM Cliente c", Long.class)
                .getSingleResult();
    }
}