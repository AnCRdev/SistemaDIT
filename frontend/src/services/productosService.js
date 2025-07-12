// src/services/productosService.js
import API_BASE_URL from "./api";

// Obtener todos los productos
export async function getProductos() {
  const res = await fetch(`${API_BASE_URL}/productos`);
  if (!res.ok) throw new Error("Error al obtener productos");
  return res.json();
}

// Crear un producto
export async function crearProducto(data) {
  const res = await fetch(`${API_BASE_URL}/productos`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
  if (!res.ok) throw new Error("Error al crear producto");
  return res.json();
} 