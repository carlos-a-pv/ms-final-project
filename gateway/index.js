require('dotenv').config();
const express = require('express');
const authMiddleware = require('./middleware/auth');
const proxyRoutes = require('./routes/proxy');
const swaggerRoutes = require('./routes/docs');

const app = express();
const PORT = process.env.PORT || 3000;

// 1. CORS Middleware (Global)
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET,POST,PUT,PATCH,DELETE,OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Authorization,Content-Type');
  if (req.method === 'OPTIONS') {
    return res.sendStatus(204);
  }
  next();
});

// 2. Ruta de Health Check (Pública y rápida)
app.get('/health', (req, res) => {
  res.status(200).json({ status: 'ok' });
});

// 3. Documentación Swagger (Debe ir ANTES de cualquier protección de JWT)
swaggerRoutes(app);

// 4. Proxies de los Microservicios
// NOTA: Pasamos authMiddleware directamente aquí adentro o lo dejamos correr antes del proxy.
// Eliminamos express.json() de aquí para que no rompa el stream de los proxies.
app.use(authMiddleware);
proxyRoutes(app);

// 5. Manejador de errores global (Para evitar que Express oculte los fallos con un 500 genérico)
app.use((err, req, res, next) => {
  console.error("Error capturado en el Gateway:", err);
  res.status(500).json({ error: 'Internal Server Error', details: err.message });
});

app.listen(PORT, () => {
  console.log(`API Gateway corriendo en puerto ${PORT}`);
});