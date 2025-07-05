# Sistema de Información

Proyecto full-stack con Spring Boot, React + Vite y PostgreSQL usando Docker.

## 🏗️ Arquitectura

- **Backend**: Spring Boot 3.5.3 + Java 17
- **Frontend**: React + Vite + TypeScript
- **Base de Datos**: PostgreSQL 15
- **Contenedores**: Docker + Docker Compose

## 🚀 Cómo ejecutar

### Prerrequisitos
- Docker Desktop instalado
- Docker Compose instalado

### Pasos para ejecutar

1. **Clonar el repositorio**
```bash
git clone <tu-repositorio>
cd sistemaDeInformación
```

2. **Ejecutar con Docker Compose**
```bash
docker-compose up --build
```

3. **Acceder a las aplicaciones**
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Base de datos**: localhost:5432

## 📁 Estructura del proyecto

```
sistemaDeInformación/
├── docker-compose.yml      # Configuración de contenedores
├── env.example            # Variables de entorno de ejemplo
├── backend/               # Spring Boot API
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/              # React + Vite
│   ├── src/
│   ├── package.json
│   └── Dockerfile
└── database/              # Scripts de PostgreSQL
    └── init/
```

## 🔧 Comandos útiles

### Ver logs de todos los servicios
```bash
docker-compose logs -f
```

### Ver logs de un servicio específico
```bash
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres
```

### Detener todos los servicios
```bash
docker-compose down
```

### Reconstruir y ejecutar
```bash
docker-compose up --build
```

### Ejecutar en segundo plano
```bash
docker-compose up -d
```

## 🛠️ Desarrollo

### Backend (Spring Boot)
- Puerto: 8080
- Base de datos: PostgreSQL
- JPA/Hibernate para ORM
- Lombok para reducir boilerplate

### Frontend (React + Vite)
- Puerto: 3000
- TypeScript
- Hot reload habilitado

### Base de Datos (PostgreSQL)
- Puerto: 5432
- Base de datos: sistema_informacion
- Usuario: postgres
- Contraseña: password123

## 📝 Notas

- Los cambios en el código se reflejan automáticamente gracias a los volúmenes de Docker
- La base de datos se inicializa automáticamente con los scripts en `database/init/`
- Spring Boot creará las tablas automáticamente con `ddl-auto=update` 