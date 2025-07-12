// src/services/api.js
// Centraliza la URL base del backend para toda la app

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

export default API_BASE_URL; 