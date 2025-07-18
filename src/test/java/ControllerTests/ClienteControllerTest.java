package ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nico.multiservicios.MultiserviciosApplication;
import com.nico.multiservicios.controller.ClienteController;
import com.nico.multiservicios.model.Cliente;
import com.nico.multiservicios.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@ContextConfiguration(classes = MultiserviciosApplication.class)
@AutoConfigureMockMvc(addFilters = false)

public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListarClientes() throws Exception {
        Mockito.when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testCrearClienteConNombreVacio() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setEmail("correo@correo.com");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El nombre es obligatorio"));
    }

    @Test
    public void testCrearClienteExitosamente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setEmail("juan@correo.com");

        Mockito.when(clienteRepository.existsByEmail("juan@correo.com")).thenReturn(false);
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@correo.com"));
    }
}
