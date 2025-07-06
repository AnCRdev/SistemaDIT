#!/bin/bash

echo "🔄 Deteniendo y limpiando contenedores..."
docker-compose down -v

echo "🧹 Limpiando imágenes..."
docker system prune -f

echo "🏗️ Reconstruyendo y levantando servicios..."
docker-compose up --build

echo "✅ ¡Listo! Los servicios deberían estar funcionando en:"
echo "   - Frontend: http://localhost:3000"
echo "   - Backend API: http://localhost:8080"
echo "   - Swagger: http://localhost:8080/swagger-ui.html" 