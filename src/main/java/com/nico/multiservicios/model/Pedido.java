package com.nico.multiservicios.model;

import java.util.Date;
import java.util.List;

public class Pedido {

        private Long id;
        private Long proveedorId;
        private List<DetalleProducto> detalleProductos;
        private String metodoPago;
        private Date fechaEntrega;
        private Integer total;

}
