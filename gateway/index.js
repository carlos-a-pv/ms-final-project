const express = require('express');
const authMiddleware = require('./middleware/auth');
const proxyRoutes = require('./routes/proxy').default;

const app = express();

// Middleware global de autenticación
app.use(authMiddleware);

// Rutas proxy
proxyRoutes(app);

app.listen(3000, () => {
  console.log('API Gateway corriendo en puerto 3000');
});