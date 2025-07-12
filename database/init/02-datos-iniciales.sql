-- DATOS INICIALES DEL SISTEMA
-- Este script inserta todos los datos que ya fueron creados anteriormente

-- USUARIOS
INSERT INTO usuarios (id, nombre, correo, contraseña, rol) VALUES
  ('f8b555b5-cdce-447d-a3dd-90cc2ef5a662', 'Vendedor Uno', 'vendedor@empresa.com', 'vendedor123', 'Vendedor'),
  ('a6e83235-ee70-4d01-9f7f-f2a3f017b3f8', 'Admin Principal', 'admin@empresa.com', 'admin123', 'Administrador'),
  ('f7d3c117-fdfb-4182-9090-4e1d846f857c', 'Gerente General', 'gerente@empresa.com', 'gerente123', 'Gerente'),
  ('335ebfe3-2c85-4525-ad86-55488eb2bf39', 'Inventario User', 'inventario@empresa.com', 'inventario123', 'Gestor de Inventario');

-- PROVEEDORES
INSERT INTO proveedores (id, nombre, ruc, direccion, telefono, correo) VALUES
  ('5996ec61-8779-4d69-8e69-c64964f8dde3', 'Proveedor Prueba', '12345678901', 'Calle Falsa 123', '999999999', 'proveedor@prueba.com'),
  ('89c67019-ed3b-4a87-bff3-5b878de8e866', 'Textiles del Sur', '20123456789', 'Av. Industrial 123, Lima', '987654321', 'contacto@textilessur.com');

-- CATEGORÍAS DE PRODUCTOS
INSERT INTO categorias_productos (id, nombre, descripcion) VALUES
  ('6cf947b0-e5fe-4853-bb5c-b93c91f6b412', 'Ropa', 'Categoría de ropa'),
  ('08d850d4-b4bf-4b90-bbbe-e6930fab4bee', 'Prendas de vestir', 'Ropa terminada lista para la venta');

-- UNIDADES DE MEDIDA
INSERT INTO unidades_medida (id, nombre, simbolo) VALUES
  ('b886113a-a9b1-4607-8320-9b17a8d52f7d', 'Unidad', 'U'),
  ('e61d3edc-98e3-47dc-882a-46c9bdf7c80c', 'Unidadw', 'uni');

-- TIPOS DE PRODUCTO
INSERT INTO tipos_producto (id, nombre, descripcion) VALUES
  ('8a3c7ef9-8e83-46d1-ae0c-513d4614086b', 'Textil', 'Tipo textil'),
  ('e82e98e6-7f03-49dd-ad6a-287049195ff6', 'Camisa', 'Camisas de diferentes tallas y colores');

-- PRODUCTOS
INSERT INTO productos (id, codigo, nombre, descripcion, stock, stock_minimo, precio, proveedor_id, categoria_id, unidad_id, tipo, tipo_producto_id) VALUES
  ('2808bedd-acc9-48da-848f-56373f8064af', 'CAM-001', 'Camiseta Basica', 'Camiseta de algodón', 98, NULL, 25.50, '5996ec61-8779-4d69-8e69-c64964f8dde3', '6cf947b0-e5fe-4853-bb5c-b93c91f6b412', 'b886113a-a9b1-4607-8320-9b17a8d52f7d', 'Producto Terminado', '8a3c7ef9-8e83-46d1-ae0c-513d4614086b'),
  ('9077b509-01e2-4e83-bddd-3864f170a7bb', 'CAM001', 'Camisa Azul', 'Camisa de algodón manga larga', 50, 10, 45.00, '5996ec61-8779-4d69-8e69-c64964f8dde3', '08d850d4-b4bf-4b90-bbbe-e6930fab4bee', 'e61d3edc-98e3-47dc-882a-46c9bdf7c80c', 'Producto Terminado', 'e82e98e6-7f03-49dd-ad6a-287049195ff6');

-- CLIENTES
INSERT INTO clientes (id, nombre, documento, telefono, correo) VALUES
  ('da431a5d-5d46-4e93-83f0-ce20e625ab3b', 'Juan Pérez', '12345678', '987654321', 'juan@email.com');

-- VENTAS
INSERT INTO ventas (id, cliente_id, usuario_id, fecha, total) VALUES
  ('0bed43ba-a0c0-4344-a027-4a23ff059f26', 'da431a5d-5d46-4e93-83f0-ce20e625ab3b', 'f8b555b5-cdce-447d-a3dd-90cc2ef5a662', '2025-07-12T04:38:21.742052604', 90.0);

-- DETALLES DE VENTA
INSERT INTO detalles_venta (id, venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
  ('6af2ba5d-c07a-4e3f-af40-0ee07c7c3e81', '0bed43ba-a0c0-4344-a027-4a23ff059f26', '2808bedd-acc9-48da-848f-56373f8064af', 2, 45.0, 90.0);

-- ÓRDENES DE PRODUCCIÓN
INSERT INTO ordenes_produccion (id, producto_id, cantidad, responsable_id, fecha_inicio, fecha_fin, observaciones, estado) VALUES
  ('94fd54df-21a0-4f17-a45e-96611cc3b285', '2808bedd-acc9-48da-848f-56373f8064af', 100, '335ebfe3-2c85-4525-ad86-55488eb2bf39', '2025-07-12', '2025-07-20', NULL, 'Pendiente');

-- ETAPAS DEFINIDAS
INSERT INTO etapas_definidas (id, nombre, descripcion) VALUES
  ('c4273368-2aef-43f8-b762-3c559b76d590', 'Corte', 'Corte de tela'),
  ('40939e45-8180-402a-89e3-ba87c5e56efd', 'Costura', 'Ensamblaje de piezas'),
  ('221191ab-1c9e-4ec0-a247-585b967681f3', 'Acabado', 'Planchado y empaquetado');

-- ETAPAS ASIGNADAS
INSERT INTO etapas_asignadas (id, orden_id, etapa_id, estado, fecha_inicio, fecha_fin, tiempo_estimado, tiempo_real) VALUES
  ('7c95c9b5-dd2d-4dd0-81c8-70df9d57c807', '94fd54df-21a0-4f17-a45e-96611cc3b285', 'c4273368-2aef-43f8-b762-3c559b76d590', 'Pendiente', '2025-07-12', '2025-07-14', NULL, NULL); 