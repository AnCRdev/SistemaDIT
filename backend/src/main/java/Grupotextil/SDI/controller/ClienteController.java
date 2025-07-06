package Grupotextil.SDI.controller;

import Grupotextil.SDI.model.Cliente;
import Grupotextil.SDI.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable UUID id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Cliente cliente) {
        Map<String, Object> response = new HashMap<>();
        
        // Validaciones según RF-02 (necesario para registro de ventas)
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre del cliente es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (cliente.getDocumento() == null || cliente.getDocumento().trim().isEmpty()) {
            response.put("error", "El documento del cliente es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el documento no esté duplicado
        if (clienteRepository.findByDocumento(cliente.getDocumento()).isPresent()) {
            response.put("error", "Ya existe un cliente con este documento");
            return ResponseEntity.badRequest().body(response);
        }
        
        Cliente savedCliente = clienteRepository.save(cliente);
        response.put("mensaje", "Cliente registrado exitosamente");
        response.put("cliente", savedCliente);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Cliente cliente) {
        Map<String, Object> response = new HashMap<>();
        
        if (!clienteRepository.existsById(id)) {
            response.put("error", "Cliente no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Validaciones similares a create
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            response.put("error", "El nombre del cliente es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (cliente.getDocumento() == null || cliente.getDocumento().trim().isEmpty()) {
            response.put("error", "El documento del cliente es obligatorio");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar que el documento no esté duplicado (excluyendo el cliente actual)
        Optional<Cliente> existingCliente = clienteRepository.findByDocumento(cliente.getDocumento());
        if (existingCliente.isPresent() && !existingCliente.get().getId().equals(id)) {
            response.put("error", "Ya existe un cliente con este documento");
            return ResponseEntity.badRequest().body(response);
        }
        
        cliente.setId(id);
        Cliente updatedCliente = clienteRepository.save(cliente);
        response.put("mensaje", "Cliente actualizado exitosamente");
        response.put("cliente", updatedCliente);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Map<String, Object> response = new HashMap<>();
        
        if (!clienteRepository.existsById(id)) {
            response.put("error", "Cliente no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Verificar si el cliente tiene ventas asociadas (según RF-02)
        // TODO: Implementar verificación de ventas cuando se cree la entidad Venta
        
        clienteRepository.deleteById(id);
        response.put("mensaje", "Cliente eliminado exitosamente");
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar cliente por documento (útil para ventas)
    @GetMapping("/documento/{documento}")
    public ResponseEntity<Cliente> getByDocumento(@PathVariable String documento) {
        Optional<Cliente> cliente = clienteRepository.findByDocumento(documento);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
} 