## 📌 Requerimientos Funcionales

### ✅ REQUERIMIENTO Nro. 01: Registro y Gestión de Artículos de Inventario

- **Tipo:** Funcional  
- **Usuario:** Sí  
- **Sistema:** No  
- **Pre-condición:** Usuario debe haber iniciado sesión con permisos de "Administrador" o "Gestor de Inventario".  
- **Post-condición:** Se crea o actualiza un artículo en la base de datos. El listado de inventario se actualiza.  
- **Fecha de creación:** 10/06/2025  
- **Responsable:** Grupo 1  

#### 📋 Descripción:
El sistema debe permitir registrar, consultar y modificar los artículos del inventario, incluyendo materias primas y productos terminados.

**Campos del formulario:**
- Código ✅
- Nombre ✅
- Descripción
- Stock ✅
- Unidad de medida
- Proveedor
- Nivel de stock mínimo

**Procesamiento:**
- Validar que los campos obligatorios no estén vacíos.
- Verificar que el código no esté duplicado.

**Salidas:**
- ✔️ "Artículo guardado exitosamente"
- ❌ "Error: El código de producto ya existe"
- Tabla de inventario actualizada en tiempo real

---

### ✅ REQUERIMIENTO Nro. 02: Registro de Ventas

- **Tipo:** Funcional  
- **Usuario:** Sí  
- **Sistema:** No  
- **Pre-condición:** Usuario autenticado. Debe existir al menos un cliente y un producto con stock positivo.  
- **Post-condición:** Se registra una venta y se descuenta el stock correspondiente.  
- **Fecha de creación:** 10/06/2025  
- **Responsable:** Grupo 1  

#### 📋 Descripción:
El sistema debe permitir registrar una nueva transacción de venta.

**Entradas:**
- Selección de cliente
- Selección de uno o más productos con cantidades
- Fecha de la venta

**Procesamiento:**
- Calcular total de venta:  
  `Total = Σ(Precio_Producto × Cantidad)`
- Descontar del inventario:  
  `Nuevo_Stock = Stock_Actual - Cantidad_Vendida`

**Salidas:**
- ✔️ "Venta registrada con éxito"
- Generación de comprobante en PDF (ver RF-05)
- Historial de ventas actualizado

---

### ✅ REQUERIMIENTO Nro. 03: Generación de Reporte de Ventas

- **Tipo:** Funcional  
- **Usuario:** Sí  
- **Sistema:** No  
- **Pre-condición:** Usuario con permisos de "Gerente" o "Administrador".  
- **Post-condición:** Visualización o descarga de resumen de ventas por periodo.  
- **Fecha de creación:** 10/06/2025  
- **Responsable:** Juan Pablo Callacondo Acero  

#### 📋 Descripción:
El sistema debe generar reportes de ventas filtrados por rango de fechas.

**Entradas:**
- Fecha de inicio  
- Fecha de fin  
- Botón "Generar Reporte"

**Procesamiento:**
- Consultar registros entre fechas
- Totalizar ingresos y agrupar (por día o producto)

**Salidas:**
- Tabla y/o gráficos en pantalla
- Opción de descarga en PDF o Excel

---

### ✅ REQUERIMIENTO Nro. 04: Registro y Gestión de Órdenes de Producción y Subprocesos

- **Tipo:** Funcional  
- **Usuario:** Sí  
- **Sistema:** No  
- **Pre-condición:** Usuario autenticado con rol "Gerente de Producción" o "Administrador". Existencia de materias primas y personal/equipo registrado.  
- **Post-condición:** Se genera orden de producción y se descuenta stock. Se visualiza el progreso por subproceso.  
- **Fecha de creación:** 23/06/2025  
- **Responsable:** Grupo 1  

#### 📋 Descripción:
El sistema debe permitir crear, consultar y actualizar órdenes de producción con sus subprocesos (corte, costura, acabado, etc.).

**Entradas:**
- Producto a fabricar y cantidad
- Responsable general
- Subprocesos definidos con:
  - Encargado
  - Fecha inicio y fin programadas
  - Observaciones

**Procesamiento:**
- Validar stock disponible
- Descontar stock al iniciar primer subproceso (Corte):  
  `Nuevo Stock MP = Stock Actual - (Consumo x Cantidad)`
- Cambiar estados: Pendiente → En Proceso → Finalizado
- Incrementar stock al finalizar último subproceso:  
  `Nuevo Stock Producto = Stock Actual + Producido`

**Salidas:**
- ✔️ "Orden de producción y sus etapas han sido creadas exitosamente"
- Ficha PDF con subprocesos, materiales y responsables
- Panel de control actualizado con el avance

---

### ✅ REQUERIMIENTO Nro. 05: Gestión de Alertas y Conflictos en Subprocesos

- **Tipo:** Funcional  
- **Usuario:** Sí  
- **Sistema:** Sí  
- **Pre-condición:** Orden en estado "En Proceso". Usuario con permisos para gestionar incidencias.  
- **Post-condición:** Conflicto registrado, notificado y actualizado. Estado del subproceso cambiado.  
- **Fecha de creación:** 05/07/2025  
- **Responsable:** Grupo 1  

#### 📋 Descripción:
El sistema debe permitir reportar conflictos en subprocesos y mostrar soluciones sugeridas.

**Entradas:**
- Botón "Reportar Incidencia"
- Selección de tipo de error:
  - Falta de material
  - Falla de máquina
  - Error de calidad
- Descripción opcional

**Procesamiento:**
- Cambiar estado del subproceso a "En Conflicto"
- Registrar detalles en PostgreSQL
- Consultar base de conocimiento
- Notificar a gerente y responsable

**Salidas:**
- ✔️ "Conflicto reportado exitosamente"
- Diagnóstico, causas posibles y soluciones sugeridas:
  1. Control de calidad a lote
  2. Solicitud de mantenimiento
  3. Reasignar tarea
- Posibilidad de generar acciones desde la solución seleccionada