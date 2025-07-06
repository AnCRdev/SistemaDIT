# Sistema de Información - Backend

## Descripción
Backend del Sistema de Información para Grupo Textil desarrollado con Spring Boot 3.5.3.

## Características Implementadas

### ✅ Funcionalidades Core
- **Gestión de Productos**: CRUD completo con validaciones
- **Gestión de Usuarios**: CRUD con roles y validaciones
- **Gestión de Proveedores**: CRUD con validación de RUC único
- **Gestión de Clientes**: CRUD con validación de documento único
- **Gestión de Categorías**: CRUD para categorización de productos
- **Gestión de Tipos de Producto**: CRUD para tipos de producto
- **Gestión de Unidades de Medida**: CRUD para unidades de medida

### ✅ Validaciones y Seguridad
- **Validaciones completas** en todos los controladores
- **Validaciones a nivel de modelo** con Bean Validation
- **Manejo global de errores** con @ControllerAdvice
- **Validación de datos únicos** (código, correo, RUC, documento)

### ✅ Documentación y Testing
- **Documentación API** con Swagger/OpenAPI
- **Tests unitarios** para controladores
- **Tests de integración** con base de datos H2
- **Configuración de CORS** para frontend

### ✅ Configuración y Despliegue
- **Dockerización** completa
- **Configuración dual** (local y Docker)
- **Base de datos PostgreSQL** con inicialización automática
- **Logging estructurado**

## Tecnologías Utilizadas

- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **PostgreSQL**
- **SpringDoc OpenAPI (Swagger)**
- **Bean Validation**
- **Docker**
- **JUnit 5**
- **H2 Database** (para tests)

## Instalación y Configuración

### Prerrequisitos
- Java 17
- Maven 3.6+
- PostgreSQL 15+
- Docker (opcional)

### Configuración Local

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd sistemaDeInformación/backend
```

2. **Configurar base de datos**
```sql
CREATE DATABASE sistema_informacion;
```

3. **Configurar application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sistema_informacion
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

4. **Ejecutar la aplicación**
```bash
./mvnw spring-boot:run
```

### Configuración con Docker

1. **Ejecutar con Docker Compose**
```bash
docker-compose up -d
```

## Endpoints de la API

### Productos
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `POST /api/productos` - Crear nuevo producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto
- `GET /api/productos/stock-bajo` - Productos con stock bajo
- `GET /api/productos/tipo/{tipo}` - Productos por tipo

### Usuarios
- `GET /api/usuarios` - Listar todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `POST /api/usuarios` - Crear nuevo usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario
- `GET /api/usuarios/correo/{correo}` - Buscar por correo
- `GET /api/usuarios/rol/{rol}` - Usuarios por rol

### Proveedores
- `GET /api/proveedores` - Listar todos los proveedores
- `GET /api/proveedores/{id}` - Obtener proveedor por ID
- `POST /api/proveedores` - Crear nuevo proveedor
- `PUT /api/proveedores/{id}` - Actualizar proveedor
- `DELETE /api/proveedores/{id}` - Eliminar proveedor
- `GET /api/proveedores/ruc/{ruc}` - Buscar por RUC
- `GET /api/proveedores/nombre/{nombre}` - Buscar por nombre

## Documentación API

Una vez ejecutada la aplicación, la documentación Swagger estará disponible en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Testing

### Ejecutar Tests Unitarios
```bash
./mvnw test
```

### Ejecutar Tests de Integración
```bash
./mvnw test -Dtest=*IntegrationTest
```

### Ejecutar Todos los Tests
```bash
./mvnw verify
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/Grupotextil/SDI/
│   │   ├── config/           # Configuraciones
│   │   ├── controller/       # Controladores REST
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Repositorios
│   │   └── SdiApplication.java
│   └── resources/
│       ├── application.properties
│       └── application-docker.properties
└── test/
    ├── java/Grupotextil/SDI/
    │   ├── controller/      # Tests unitarios
    │   └── integration/     # Tests de integración
    └── resources/
        └── application-test.properties
```

## Validaciones Implementadas

### Producto
- Código obligatorio y único
- Nombre obligatorio
- Stock obligatorio y >= 0
- Stock mínimo >= 0
- Tipo debe ser "Materia Prima" o "Producto Terminado"

### Usuario
- Nombre obligatorio
- Correo obligatorio, único y formato válido
- Contraseña obligatoria
- Rol obligatorio y válido

### Proveedor
- Nombre obligatorio
- RUC único (si se proporciona)
- Formato de correo válido (si se proporciona)

### Cliente
- Nombre obligatorio
- Documento obligatorio y único
- Formato de correo válido (si se proporciona)

## Manejo de Errores

El sistema incluye manejo global de errores con respuestas consistentes:

```json
{
  "error": "Descripción del error",
  "mensaje": "Mensaje detallado",
  "timestamp": 1234567890,
  "detalles": {
    "campo": "error específico"
  }
}
```

## Próximas Mejoras

- [ ] Implementar autenticación JWT
- [ ] Agregar autorización por roles
- [ ] Implementar paginación en endpoints
- [ ] Agregar filtros avanzados
- [ ] Implementar auditoría de cambios
- [ ] Agregar métricas y monitoreo
- [ ] Implementar cache con Redis
- [ ] Agregar validaciones de negocio más complejas

## Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. 