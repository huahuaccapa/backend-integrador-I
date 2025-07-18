package ControllerTests;


import com.nico.multiservicios.controller.ProductoController;
import com.nico.multiservicios.model.Producto;
import com.nico.multiservicios.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoControllerUnitTest {

    private ProductoRepository productoRepository;
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        productoController = new ProductoController();
        productoController.setProductoRepository(productoRepository);

    }

    @Test
    void testObtenerProductosConStockBajo() {
        Producto p1 = new Producto();
        p1.setStock(1);
        p1.setStockMinimo(3);

        Producto p2 = new Producto();
        p2.setStock(5);
        p2.setStockMinimo(2);

        when(productoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Producto> resultado = productoController.obtenerProductosConStockBajo();

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(p1));
        assertFalse(resultado.contains(p2));
    }

    @Test
    void testCrearProducto() {
        // Arrange: Creamos un producto ficticio
        Producto nuevoProducto = new Producto();
        nuevoProducto.setCodigo("P001");
        nuevoProducto.setNombreProducto("Taladro");
        nuevoProducto.setStock(10);

        // Simulamos que al guardar, el repo devuelve el mismo producto
        when(productoRepository.save(nuevoProducto)).thenReturn(nuevoProducto);

        // Act: Ejecutamos el método que queremos probar
        Producto resultado = productoController.crearProducto(nuevoProducto);

        // Assert: Verificamos que se comportó como esperábamos
        assertNotNull(resultado);
        assertEquals("P001", resultado.getCodigo());
        assertEquals("Taladro", resultado.getNombreProducto());
        assertEquals(10, resultado.getStock());

        // Verificamos que el método save() fue llamado exactamente una vez
        verify(productoRepository, times(1)).save(nuevoProducto);
    }

}
