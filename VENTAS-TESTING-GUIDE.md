# Guía de Pruebas - Endpoints de Ventas (RF-02)

## Información General
- **Base URL**: `http://localhost:8080/api/ventas`
- **Documentación Swagger**: `http://localhost:8080/swagger-ui/index.html`

## 🔍 Obtener IDs Existentes (Datos de Pruebas Anteriores)

Antes de crear ventas, obtén los IDs de los datos que ya creamos:

```powershell
# Obtener clientes existentes
$clientes = Invoke-RestMethod -Uri "http://localhost:8080/api/clientes" -Method GET
$clientes | Format-Table id, nombre, documento

# Obtener usuarios existentes  
$usuarios = Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" -Method GET
$usuarios | Format-Table id, nombre, correo, rol

# Obtener productos existentes
$productos = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET
$productos | Format-Table id, codigo, nombre, stock, precio
```

**Ejemplo de salida esperada:**
- **Cliente**: Maria Garcia (ID: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)
- **Usuario**: Juan Perez (ID: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)  
- **Producto**: Tela de Algodon (ID: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)

## Endpoints Disponibles

### 1. Crear Venta
```powershell
# Crear una nueva venta
$body = @{
    clienteId = "af387438-0b85-4005-804b-bf4fe98d7507"
    usuarioId = "id=7405652f-0c31-4816-8570-21831763b2bc"
    detalles = @(
        @{
            productoId = "86a155a1-1b30-4c9c-9763-cc8dee553d75"
            cantidad = 2
            precioUnitario = 25.50
        }
    )
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Uri "http://localhost:8080/api/ventas" -Method POST -Body $body -ContentType "application/json"
```

### 2. Obtener Todas las Ventas
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ventas" -Method GET
```

### 3. Obtener Venta por ID
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/b12b2029-8e3b-427c-a0e2-cda3152632c6" -Method GET
```

### 4. Actualizar Venta
```powershell
$body = @{
    clienteId = "ID_DEL_CLIENTE"
    usuarioId = "ID_DEL_USUARIO"
    detalles = @(
        @{
            productoId = "ID_DEL_PRODUCTO"
            cantidad = 3
            precioUnitario = 30.00
        }
    )
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/ID_DE_LA_VENTA" -Method PUT -Body $body -ContentType "application/json"
```

### 5. Eliminar Venta
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/ID_DE_LA_VENTA" -Method DELETE
```

### 6. Buscar Ventas por Cliente
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/cliente/ID_DEL_CLIENTE" -Method GET
```

### 7. Buscar Ventas por Usuario
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/usuario/ID_DEL_USUARIO" -Method GET
```

### 8. Buscar Ventas por Rango de Fechas
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/fechas?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59" -Method GET
```

### 9. Obtener Ventas del Día Actual
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/hoy" -Method GET
```

### 10. Reporte de Ventas por Rango de Fechas
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/reporte?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59" -Method GET
```

## Pasos para Probar

### Paso 1: Verificar que existen datos base
Antes de crear ventas, asegúrate de tener:
- Al menos un cliente
- Al menos un usuario
- Al menos un producto

### Paso 2: Crear una venta de prueba
1. Obtén los IDs necesarios de las entidades existentes
2. Crea una venta con detalles
3. Verifica que se guardó correctamente

### Paso 3: Probar funcionalidades
1. Buscar la venta por ID
2. Actualizar la venta
3. Probar los reportes
4. Eliminar la venta

## Notas Importantes

- **IDs**: Los IDs son UUIDs, asegúrate de usar IDs válidos
- **Fechas**: Usa formato ISO 8601 (YYYY-MM-DDTHH:mm:ss)
- **Precios**: Usa números decimales con punto (ej: 25.50)
- **Cantidades**: Usa números enteros positivos

## Ejemplo Completo de Prueba

```powershell
# 1. Obtener un cliente existente
$clientes = Invoke-RestMethod -Uri "http://localhost:8080/api/clientes" -Method GET
$clienteId = $clientes[0].id
Write-Host "Cliente ID: $clienteId"

# 2. Obtener un usuario existente
$usuarios = Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" -Method GET
$usuarioId = $usuarios[0].id
Write-Host "Usuario ID: $usuarioId"

# 3. Obtener un producto existente
$productos = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET
$productoId = $productos[0].id
Write-Host "Producto ID: $productoId"

# 4. Crear una venta
$body = @{
    clienteId = $clienteId
    usuarioId = $usuarioId
    detalles = @(
        @{
            productoId = $productoId
            cantidad = 2
            precioUnitario = 25.50
        }
    )
} | ConvertTo-Json -Depth 3

$venta = Invoke-RestMethod -Uri "http://localhost:8080/api/ventas" -Method POST -Body $body -ContentType "application/json"
Write-Host "Venta creada con ID: $($venta.id)"

# 5. Obtener la venta creada
$ventaObtenida = Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/$($venta.id)" -Method GET
Write-Host "Venta obtenida: $($ventaObtenida | ConvertTo-Json -Depth 3)"
```

## Ejemplo con Datos Específicos (Basado en Pruebas Anteriores)

Si ya tienes datos creados de las pruebas anteriores, puedes usar estos IDs directamente:

```powershell
# Ejemplo con datos específicos (reemplazar con tus IDs reales)
$clienteId = "TU_CLIENTE_ID_AQUI"  # Ej: Maria Garcia
$usuarioId = "TU_USUARIO_ID_AQUI"  # Ej: Juan Perez  
$productoId = "TU_PRODUCTO_ID_AQUI" # Ej: Tela de Algodon

$body = @{
    clienteId = $clienteId
    usuarioId = $usuarioId
    detalles = @(
        @{
            productoId = $productoId
            cantidad = 3
            precioUnitario = 30.00
        },
        @{
            productoId = $productoId
            cantidad = 1
            precioUnitario = 25.50
        }
    )
} | ConvertTo-Json -Depth 3

$venta = Invoke-RestMethod -Uri "http://localhost:8080/api/ventas" -Method POST -Body $body -ContentType "application/json"
Write-Host "Venta creada exitosamente!"
```

## Posibles Errores y Soluciones

- **400 Bad Request**: Verifica el formato del JSON y los tipos de datos
- **404 Not Found**: Verifica que los IDs existan en la base de datos
- **500 Internal Server Error**: Revisa los logs del backend para más detalles 

PS D:\Proyecto e\sistemaDeInformación> Invoke-RestMethod -Uri "http://localhost:8080/api/ventas" -Method GET


id       : 9eb4044f-bb1d-4c23-9732-6085dd47d1c8
cliente  : @{id=af387438-0b85-4005-804b-bf4fe98d7507; nombre=Maria Garcia; documento=12345678; telefono=987-654-321; correo=maria@email.com}
usuario  : @{id=7405652f-0c31-4816-8570-21831763b2bc; nombre=Juan Perez; correo=juan@empresa.com; contraseÃ±a=password123; rol=Administrador}
fecha    : 2025-07-06T23:31:12.843762
total    : 51.00
detalles : {@{id=f9569e18-c092-453b-b54a-2535d405f3bc; producto=; cantidad=2; precioUnitario=25.50; subtotal=51.00}}

id       : 63eba789-60ba-41f2-921a-63671410832c
cliente  : @{id=af387438-0b85-4005-804b-bf4fe98d7507; nombre=Maria Garcia; documento=12345678; telefono=987-654-321; correo=maria@email.com}
usuario  : @{id=7405652f-0c31-4816-8570-21831763b2bc; nombre=Juan Perez; correo=juan@empresa.com; contraseÃ±a=password123; rol=Administrador}
fecha    : 2025-07-06T23:36:07.528453
total    : 51.00
detalles : {@{id=c9d02de1-8ffd-4f57-aa5c-fb80cf0d4ce8; producto=; cantidad=2; precioUnitario=25.50; subtotal=51.00}}

id       : b12b2029-8e3b-427c-a0e2-cda3152632c6
cliente  : @{id=af387438-0b85-4005-804b-bf4fe98d7507; nombre=Maria Garcia; documento=12345678; telefono=987-654-321; correo=maria@email.com}
usuario  : @{id=7405652f-0c31-4816-8570-21831763b2bc; nombre=Juan Perez; correo=juan@empresa.com; contraseÃ±a=password123; rol=Administrador}
fecha    : 2025-07-06T23:36:52.700479
total    : 51.00
detalles : {@{id=c8fc1619-b646-40f7-a020-5c1ddd10ecc1; producto=; cantidad=2; precioUnitario=25.50; subtotal=51.00}}



PS D:\Proyecto e\sistemaDeInformación> Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/ID_DE_LA_VENTA" -Method GET
Invoke-RestMethod : Error en el servidor remoto: (500) Error interno del servidor.
En línea: 1 Carácter: 1
+ Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/ID_DE_LA_VEN ...
+ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    + CategoryInfo          : InvalidOperation: (System.Net.HttpWebRequest:HttpWebRequest) [Invoke-RestMethod], WebException
    + FullyQualifiedErrorId : WebCmdletWebResponseException,Microsoft.PowerShell.Commands.InvokeRestMethodCommand

PS D:\Proyecto e\sistemaDeInformación> Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/b12b2029-8e3b-427c-a0e2-cda3152632c6" -Method GET


id       : b12b2029-8e3b-427c-a0e2-cda3152632c6
cliente  : @{id=af387438-0b85-4005-804b-bf4fe98d7507; nombre=Maria Garcia; documento=12345678; telefono=987-654-321; correo=maria@email.com}
usuario  : @{id=7405652f-0c31-4816-8570-21831763b2bc; nombre=Juan Perez; correo=juan@empresa.com; contraseÃ±a=password123; rol=Administrador}        
fecha    : 2025-07-06T23:36:52.700479
total    : 51.00
detalles : {@{id=c8fc1619-b646-40f7-a020-5c1ddd10ecc1; producto=; cantidad=2; precioUnitario=25.50; subtotal=51.00}}



PS D:\Proyecto e\sistemaDeInformación> Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/reporte?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59"


totalIngresos : 0.0
fechaInicio   : 2024-01-01T00:00:00
totalVentas   : 0
ventas        : {}
fechaFin      : 2024-12-31T23:59:59
