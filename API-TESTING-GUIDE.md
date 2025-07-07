# 🧪 Guía Completa de Pruebas de API - Sistema de Información

## 📋 Índice
- [Productos](#productos)
- [Usuarios](#usuarios)
- [Proveedores](#proveedores)
- [Clientes](#clientes)
- [Categorías](#categorías)
- [Tipos de Producto](#tipos-de-producto)
- [Unidades de Medida](#unidades-de-medida)
- [Hello Controller](#hello-controller)

---

## 🏷️ PRODUCTOS

### 1. GET - Obtener todos los productos
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET
```

### 2. POST - Crear un producto
```powershell
$producto = @{
    codigo = "PROD-001"
    nombre = "Tela de Algodon"
    descripcion = "Tela de algodon premium"
    stock = 100
    stock_minimo = 10
    tipo = "Materia Prima"
}

$json = $producto | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method POST -Body $bytes -ContentType "application/json"
```

### 3. GET - Obtener producto por ID
```powershell
# Primero obtener todos para ver IDs
$productos = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET
$productos | Format-Table id, codigo, nombre

# Luego obtener uno específico (reemplazar {id})
Invoke-RestMethod -Uri "http://localhost:8080/api/productos/{id}" -Method GET
```

### 4. PUT - Actualizar un producto
```powershell
$productoActualizado = @{
    codigo = "PROD-001-UPDATED"
    nombre = "Tela de Algodon Premium"
    descripcion = "Tela de algodon premium actualizada"
    stock = 75
    stock_minimo = 8
    tipo = "Materia Prima"
}

$json = $productoActualizado | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/productos/{id}" -Method PUT -Body $bytes -ContentType "application/json"
```

### 5. DELETE - Eliminar un producto
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/productos/{id}" -Method DELETE
```

### 6. GET - Productos con stock bajo
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/productos/stock-bajo" -Method GET
```

### 7. GET - Productos por tipo
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/productos/tipo/Materia Prima" -Method GET
```

---

## 👥 USUARIOS

### 1. GET - Obtener todos los usuarios
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" -Method GET
```

### 2. POST - Crear un usuario
```powershell
$usuario = @{
    nombre = "Juan Perez"
    correo = "juan@empresa.com"
    contraseña = "password123"
    rol = "Administrador"
}

$json = $usuario | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" -Method POST -Body $bytes -ContentType "application/json"
```

### 3. GET - Obtener usuario por ID
```powershell
# Primero obtener todos para ver IDs
$usuarios = Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" -Method GET
$usuarios | Format-Table id, nombre, correo, rol

# Luego obtener uno específico (reemplazar {id})
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/{id}" -Method GET
```

### 4. PUT - Actualizar un usuario
```powershell
$usuarioActualizado = @{
    nombre = "Juan Perez Actualizado"
    correo = "juan.actualizado@empresa.com"
    contraseña = "nuevaPassword123"
    rol = "Gerente"
}

$json = $usuarioActualizado | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/{id}" -Method PUT -Body $bytes -ContentType "application/json"
```

### 5. DELETE - Eliminar un usuario
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/{id}" -Method DELETE
```

### 6. GET - Buscar usuario por correo
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/correo/juan@empresa.com" -Method GET
```

### 7. GET - Buscar usuarios por rol
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/rol/Administrador" -Method GET
```

---

## 🏢 PROVEEDORES

### 1. GET - Obtener todos los proveedores
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/proveedores" -Method GET
```

### 2. POST - Crear un proveedor
```powershell
$proveedor = @{
    nombre = "Textiles ABC"
    ruc = "20123456789"
    direccion = "Av. Principal 123"
    telefono = "01-234-5678"
    correo = "contacto@textilesabc.com"
}

$json = $proveedor | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/proveedores" -Method POST -Body $bytes -ContentType "application/json"
```

### 3. GET - Obtener proveedor por ID
```powershell
# Primero obtener todos para ver IDs
$proveedores = Invoke-RestMethod -Uri "http://localhost:8080/api/proveedores" -Method GET
$proveedores | Format-Table id, nombre, ruc

# Luego obtener uno específico (reemplazar {id})
Invoke-RestMethod -Uri "http://localhost:8080/api/proveedores/{id}" -Method GET
```

### 4. PUT - Actualizar un proveedor
```powershell
$proveedorActualizado = @{
    nombre = "Textiles ABC Actualizado"
    ruc = "20123456789"
    direccion = "Nueva Direccion 789"
    telefono = "01-111-2222"
    correo = "nuevo@textilesabc.com"
}

$json = $proveedorActualizado | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/proveedores/{id}" -Method PUT -Body $bytes -ContentType "application/json"
```

### 5. DELETE - Eliminar un proveedor
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/proveedores/{id}" -Method DELETE
```

### 6. GET - Buscar proveedor por RUC
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/proveedores/ruc/20123456789" -Method GET
```

### 7. GET - Buscar proveedores por nombre
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/proveedores/nombre/Textiles" -Method GET
```

---

## 👤 CLIENTES

### 1. GET - Obtener todos los clientes
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes" -Method GET
```

### 2. POST - Crear un cliente
```powershell
$cliente = @{
    nombre = "Maria Garcia"
    documento = "12345678"
    telefono = "987-654-321"
    correo = "maria@email.com"
}

$json = $cliente | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes" -Method POST -Body $bytes -ContentType "application/json"
```

### 3. GET - Obtener cliente por ID
```powershell
# Primero obtener todos para ver IDs
$clientes = Invoke-RestMethod -Uri "http://localhost:8080/api/clientes" -Method GET
$clientes | Format-Table id, nombre, documento

# Luego obtener uno específico (reemplazar {id})
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes/{id}" -Method GET
```

### 4. PUT - Actualizar un cliente
```powershell
$clienteActualizado = @{
    nombre = "Maria Garcia Actualizada"
    documento = "12345678"
    telefono = "999-888-777"
    correo = "maria.actualizada@email.com"
}

$json = $clienteActualizado | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes/{id}" -Method PUT -Body $bytes -ContentType "application/json"
```

### 5. DELETE - Eliminar un cliente
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes/{id}" -Method DELETE
```

### 6. GET - Buscar cliente por documento
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/clientes/documento/12345678" -Method GET
```

---

## 📂 CATEGORÍAS

### 1. GET - Obtener todas las categorías
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/categorias-productos" -Method GET
```

### 2. POST - Crear una categoría
```powershell
$categoria = @{
    nombre = "Telas Sinteticas"
    descripcion = "Categoria para telas de origen sintetico"
}

$json = $categoria | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/categorias-productos" -Method POST -Body $bytes -ContentType "application/json"
```

### 3. GET - Obtener categoría por ID
```powershell
# Primero obtener todas para ver IDs
$categorias = Invoke-RestMethod -Uri "http://localhost:8080/api/categorias-productos" -Method GET
$categorias | Format-Table id, nombre, descripcion

# Luego obtener una específica (reemplazar {id})
Invoke-RestMethod -Uri "http://localhost:8080/api/categorias-productos/{id}" -Method GET
```

### 4. PUT - Actualizar una categoría
```powershell
$categoriaActualizada = @{
    nombre = "Telas Sinteticas Premium"
    descripcion = "Categoria actualizada para telas sinteticas de alta calidad"
}

$json = $categoriaActualizada | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/categorias-productos/{id}" -Method PUT -Body $bytes -ContentType "application/json"
```

### 5. DELETE - Eliminar una categoría
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/categorias-productos/{id}" -Method DELETE
```

### 6. GET - Buscar categoría por nombre
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/categorias-productos/nombre/Telas Naturales" -Method GET
```

---

## 🏷️ TIPOS DE PRODUCTO

### 1. GET - Obtener todos los tipos de producto
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/tipos-producto" -Method GET
```

### 2. POST - Crear un tipo de producto
```powershell
$tipoProducto = @{
    nombre = "Pantalones"
    descripcion = "Tipo de producto para pantalones"
}

$json = $tipoProducto | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/tipos-producto" -Method POST -Body $bytes -ContentType "application/json"
```

### 3. GET - Obtener tipo por ID
```powershell
# Primero obtener todos para ver IDs
$tipos = Invoke-RestMethod -Uri "http://localhost:8080/api/tipos-producto" -Method GET
$tipos | Format-Table id, nombre, descripcion

# Luego obtener uno específico (reemplazar {id})
Invoke-RestMethod -Uri "http://localhost:8080/api/tipos-producto/{id}" -Method GET
```

### 4. PUT - Actualizar un tipo
```powershell
$tipoActualizado = @{
    nombre = "Pantalones Premium"
    descripcion = "Tipo de producto para pantalones de alta calidad"
}

$json = $tipoActualizado | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/tipos-producto/{id}" -Method PUT -Body $bytes -ContentType "application/json"
```

### 5. DELETE - Eliminar un tipo
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/tipos-producto/{id}" -Method DELETE
```

### 6. GET - Buscar tipo por nombre
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/tipos-producto/nombre/Camisetas" -Method GET
```

### 7. GET - Buscar tipo por descripción
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/tipos-producto/descripcion/Tipo de producto para camisetas" -Method GET
```

---

## 📏 UNIDADES DE MEDIDA

### 1. GET - Obtener todas las unidades de medida
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades-medida" -Method GET
```

### 2. POST - Crear una unidad de medida
```powershell
$unidad = @{
    nombre = "Centimetros"
    simbolo = "cm"
}

$json = $unidad | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades-medida" -Method POST -Body $bytes -ContentType "application/json"
```

### 3. GET - Obtener unidad por ID
```powershell
# Primero obtener todas para ver IDs
$unidades = Invoke-RestMethod -Uri "http://localhost:8080/api/unidades-medida" -Method GET
$unidades | Format-Table id, nombre, simbolo

# Luego obtener una específica (reemplazar {id})
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades-medida/{id}" -Method GET
```

### 4. PUT - Actualizar una unidad
```powershell
$unidadActualizada = @{
    nombre = "Centimetros Cuadrados"
    simbolo = "cm²"
}

$json = $unidadActualizada | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades-medida/{id}" -Method PUT -Body $bytes -ContentType "application/json"
```

### 5. DELETE - Eliminar una unidad
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades-medida/{id}" -Method DELETE
```

### 6. GET - Buscar unidad por nombre
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades-medida/nombre/Metros" -Method GET
```

### 7. GET - Buscar unidad por símbolo
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades-medida/simbolo/m" -Method GET
```

---

## 🌐 HELLO CONTROLLER

### 1. GET - Endpoint de prueba
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/hello" -Method GET
```

---

## 📊 RESUMEN DE ENDPOINTS PROBADOS

| Entidad | GET | POST | PUT | DELETE | Búsquedas | Total |
|---------|-----|------|-----|--------|-----------|-------|
| Productos | ✅ | ✅ | ✅ | ✅ | ✅ | 7/7 |
| Usuarios | ✅ | ✅ | ✅ | ✅ | ✅ | 7/7 |
| Proveedores | ✅ | ✅ | ✅ | ✅ | ✅ | 7/7 |
| Clientes | ✅ | ✅ | ✅ | ✅ | ✅ | 6/6 |
| Categorías | ✅ | ✅ | ✅ | ✅ | ✅ | 6/6 |
| Tipos de Producto | ✅ | ✅ | ✅ | ✅ | ✅ | 7/7 |
| Unidades de Medida | ✅ | ✅ | ✅ | ✅ | ✅ | 7/7 |
| Hello Controller | ✅ | - | - | - | - | 1/1 |

**Total de endpoints probados: 48/48** ✅

---

## 🔧 NOTAS IMPORTANTES

### Codificación UTF-8
Para evitar problemas de codificación, siempre usar:
```powershell
$json = $objeto | ConvertTo-Json -Depth 10
$bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
Invoke-RestMethod -Uri "URL" -Method POST -Body $bytes -ContentType "application/json"
```

### URLs Especiales
- **Categorías**: `/api/categorias-productos` (no `/api/categorias`)
- **Hello**: `/hello` (no `/api/hello`)

### Reemplazar IDs
Para operaciones PUT/DELETE/GET por ID, reemplazar `{id}` con el UUID real obtenido de las operaciones GET.

---

## 🎯 PRÓXIMOS PASOS

1. **Frontend Development**: Conectar React con estas APIs
2. **Authentication**: Implementar JWT
3. **Advanced Features**: Filtros, paginación, reportes
4. **Testing**: Tests automatizados
5. **Documentation**: Swagger UI ya disponible en `/swagger-ui.html` 