package Grupotextil.SDI.integration;

import Grupotextil.SDI.model.Producto;
import Grupotextil.SDI.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class ProductoIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        productoRepository.deleteAll();
    }

    @Test
    void createAndRetrieveProducto_ShouldWork() throws Exception {
        // Crear producto
        Producto producto = new Producto();
        producto.setCodigo("TEST-001");
        producto.setNombre("Producto Test");
        producto.setStock(100);
        producto.setTipo("Materia Prima");

        // POST request
        String response = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Artículo guardado exitosamente"))
                .andReturn().getResponse().getContentAsString();

        // Verificar que se guardó en la base de datos
        assert productoRepository.findByCodigo("TEST-001").isPresent();
    }

    @Test
    void createProductoWithInvalidData_ShouldReturnBadRequest() throws Exception {
        Producto producto = new Producto();
        // Sin código - debería fallar
        producto.setNombre("Producto Test");
        producto.setStock(100);
        producto.setTipo("Materia Prima");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El código es obligatorio"));
    }
} 