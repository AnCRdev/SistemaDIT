package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.Producto;
import Grupotextil.SDI.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoRepository productoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto productoTest;
    private UUID productoId;

    @BeforeEach
    void setUp() {
        productoId = UUID.randomUUID();
        productoTest = new Producto();
        productoTest.setId(productoId);
        productoTest.setCodigo("PROD-001");
        productoTest.setNombre("Producto Test");
        productoTest.setStock(100);
        productoTest.setTipo("Materia Prima");
    }

    @Test
    void getAllProductos_ShouldReturnList() throws Exception {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(productoTest));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("PROD-001"))
                .andExpect(jsonPath("$[0].nombre").value("Producto Test"));
    }

    @Test
    void getProductoById_WhenExists_ShouldReturnProducto() throws Exception {
        when(productoRepository.findById(productoId)).thenReturn(Optional.of(productoTest));

        mockMvc.perform(get("/api/productos/" + productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("PROD-001"))
                .andExpect(jsonPath("$.nombre").value("Producto Test"));
    }

    @Test
    void getProductoById_WhenNotExists_ShouldReturn404() throws Exception {
        when(productoRepository.findById(productoId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/" + productoId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProducto_WithValidData_ShouldReturnCreated() throws Exception {
        when(productoRepository.findByCodigo("PROD-001")).thenReturn(Optional.empty());
        when(productoRepository.save(any(Producto.class))).thenReturn(productoTest);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoTest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Artículo guardado exitosamente"));
    }

    @Test
    void createProducto_WithEmptyCodigo_ShouldReturnBadRequest() throws Exception {
        productoTest.setCodigo("");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoTest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El código es obligatorio"));
    }

    @Test
    void createProducto_WithDuplicateCodigo_ShouldReturnBadRequest() throws Exception {
        when(productoRepository.findByCodigo("PROD-001")).thenReturn(Optional.of(productoTest));

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoTest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El código de producto ya existe"));
    }

    @Test
    void updateProducto_WithValidData_ShouldReturnOk() throws Exception {
        when(productoRepository.existsById(productoId)).thenReturn(true);
        when(productoRepository.findByCodigo("PROD-001")).thenReturn(Optional.empty());
        when(productoRepository.save(any(Producto.class))).thenReturn(productoTest);

        mockMvc.perform(put("/api/productos/" + productoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoTest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Artículo actualizado exitosamente"));
    }

    @Test
    void deleteProducto_WhenExists_ShouldReturnOk() throws Exception {
        when(productoRepository.existsById(productoId)).thenReturn(true);

        mockMvc.perform(delete("/api/productos/" + productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Producto eliminado exitosamente"));
    }

    @Test
    void deleteProducto_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(productoRepository.existsById(productoId)).thenReturn(false);

        mockMvc.perform(delete("/api/productos/" + productoId))
                .andExpect(status().isNotFound());
    }
} 