-- EXTENSIÓN PARA USAR UUIDs
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- USUARIOS Y CLIENTES
CREATE TABLE usuarios (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre TEXT NOT NULL,
  correo TEXT UNIQUE NOT NULL,
  contraseña TEXT NOT NULL,
  rol TEXT CHECK (rol IN ('Administrador', 'Gestor de Inventario', 'Gerente', 'Vendedor')) NOT NULL
);

CREATE TABLE clientes (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre TEXT NOT NULL,
  documento TEXT UNIQUE NOT NULL,
  telefono TEXT,
  correo TEXT
);

-- TABLAS AUXILIARES
CREATE TABLE proveedores (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre TEXT NOT NULL,
  ruc TEXT UNIQUE,
  direccion TEXT,
  telefono TEXT,
  correo TEXT
);

CREATE TABLE categorias_productos (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre TEXT UNIQUE NOT NULL,
  descripcion TEXT
);

CREATE TABLE unidades_medida (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre TEXT UNIQUE NOT NULL,
  simbolo TEXT
);

-- TIPOS DE PRODUCTO / FICHA TÉCNICA
CREATE TABLE tipos_producto (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre TEXT NOT NULL,
  descripcion TEXT
);

-- PRODUCTOS
CREATE TABLE productos (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  codigo TEXT UNIQUE NOT NULL,
  nombre TEXT NOT NULL,
  descripcion TEXT,
  stock INTEGER CHECK (stock >= 0) NOT NULL,
  stock_minimo INTEGER CHECK (stock_minimo >= 0) DEFAULT 0,
  precio NUMERIC(10, 2) CHECK (precio > 0),
  proveedor_id UUID REFERENCES proveedores(id),
  categoria_id UUID REFERENCES categorias_productos(id),
  unidad_id UUID REFERENCES unidades_medida(id),
  tipo TEXT CHECK (tipo IN ('Materia Prima', 'Producto Terminado')) NOT NULL,
  tipo_producto_id UUID REFERENCES tipos_producto(id)
);

-- VENTAS
CREATE TABLE ventas (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  cliente_id UUID REFERENCES clientes(id) ON DELETE RESTRICT,
  usuario_id UUID REFERENCES usuarios(id),
  fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total NUMERIC(10, 2)
);

CREATE TABLE detalles_venta (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  venta_id UUID REFERENCES ventas(id) ON DELETE CASCADE,
  producto_id UUID REFERENCES productos(id),
  cantidad INTEGER CHECK (cantidad > 0),
  precio_unitario NUMERIC(10, 2),
  subtotal NUMERIC(10, 2)
);

-- PRODUCCIÓN
CREATE TABLE ordenes_produccion (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  producto_id UUID REFERENCES productos(id),
  cantidad INTEGER CHECK (cantidad > 0),
  responsable_id UUID REFERENCES usuarios(id),
  fecha_inicio DATE,
  fecha_fin DATE,
  observaciones TEXT,
  estado TEXT CHECK (estado IN ('Pendiente', 'En Proceso', 'Finalizado')) DEFAULT 'Pendiente'
);

-- ETAPAS DEFINIDAS
CREATE TABLE etapas_definidas (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nombre TEXT UNIQUE NOT NULL,
  descripcion TEXT
);

-- ETAPAS POR TIPO DE PRODUCTO
CREATE TABLE etapas_por_tipo (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tipo_producto_id UUID REFERENCES tipos_producto(id),
  etapa_id UUID REFERENCES etapas_definidas(id),
  orden_etapa INT
);

-- ETAPAS ASIGNADAS A UNA ORDEN
CREATE TABLE etapas_asignadas (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  orden_id UUID REFERENCES ordenes_produccion(id) ON DELETE CASCADE,
  etapa_id UUID REFERENCES etapas_definidas(id) ON DELETE CASCADE,
  estado TEXT CHECK (estado IN ('Pendiente', 'En Proceso', 'Finalizado')) DEFAULT 'Pendiente',
  fecha_inicio DATE,
  fecha_fin DATE,
  tiempo_estimado INTERVAL,
  tiempo_real INTERVAL
);

-- ENCARGADOS DE ETAPAS
CREATE TABLE encargados_etapas (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  etapa_asignada_id UUID REFERENCES etapas_asignadas(id) ON DELETE CASCADE,
  usuario_id UUID REFERENCES usuarios(id) ON DELETE SET NULL,
  observaciones TEXT,
  fecha_asignacion DATE DEFAULT CURRENT_DATE
);

-- INSUMOS USADOS EN ORDEN DE PRODUCCIÓN
CREATE TABLE insumos_orden (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  orden_id UUID REFERENCES ordenes_produccion(id),
  producto_id UUID REFERENCES productos(id), -- materia prima
  cantidad_utilizada NUMERIC CHECK (cantidad_utilizada >= 0)
);

-- --------------------------------------------------
-- ===== IMPLEMENTACIÓN DE RF‑05: CONFLICTOS & SOLUCIONES

-- 1) Incidencias/conflictos en subprocesos
CREATE TABLE conflictos_subprocesos (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  etapa_asignada_id UUID NOT NULL
    REFERENCES etapas_asignadas(id)
    ON DELETE CASCADE,
  usuario_reporta_id UUID NOT NULL
    REFERENCES usuarios(id)
    ON DELETE SET NULL,
  tipo_error TEXT NOT NULL
    CHECK (tipo_error IN ('Falta de material','Falla de máquina','Error de calidad')),
  descripcion TEXT,
  estado TEXT NOT NULL
    CHECK (estado IN ('En Conflicto','Resuelto','Escalado')) DEFAULT 'En Conflicto',
  fecha_reporte TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_resolucion TIMESTAMP,
  diagnostico TEXT,
  posibles_causas TEXT[],         -- lista de causas sugeridas por el sistema
  soluciones_sugeridas TEXT[],    -- lista de opciones propuestas
  solucion_seleccionada TEXT,     -- la que finalmente eligió el gerente
  responsable_resolucion_id UUID  -- quién lo resolvió o escaló
    REFERENCES usuarios(id)
    ON DELETE SET NULL
);

-- 2) Base de conocimiento de soluciones
CREATE TABLE base_conocimiento_soluciones (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tipo_error TEXT NOT NULL
    CHECK (tipo_error IN ('Falta de material','Falla de máquina','Error de calidad')),
  causa TEXT NOT NULL,
  solucion TEXT NOT NULL
);

-- Poblado inicial de la base de conocimiento
INSERT INTO base_conocimiento_soluciones (tipo_error, causa, solucion) VALUES
  ('Falta de material',       'Materia prima agotada en stock principal', 'Usar stock alternativo o solicitar reposición urgente'),
  ('Falla de máquina',        'Desalineación del cabezal de costura',       'Notificar a mantenimiento para calibración'),
  ('Error de calidad',        'Tensión de hilo incorrecta',                 'Realizar control de calidad en muestra de lote');

-- 3) Índices de apoyo para consultas frecuentes
CREATE INDEX idx_conflictos_etapa ON conflictos_subprocesos(etapa_asignada_id);
CREATE INDEX idx_conflictos_estado ON conflictos_subprocesos(estado);

-- NOTIFICACIONES DE STOCK
CREATE TABLE notificaciones (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  producto_id UUID REFERENCES productos(id),
  mensaje TEXT NOT NULL,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  leido BOOLEAN DEFAULT FALSE,
  conflicto_id UUID REFERENCES conflictos_subprocesos(id)  -- agregado para vincular alertas a conflictos
);

-- LOG DE ACTIVIDADES
CREATE TABLE logs_actividad (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  usuario_id UUID REFERENCES usuarios(id),
  accion TEXT NOT NULL,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  descripcion TEXT
); 