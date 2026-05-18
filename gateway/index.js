require('dotenv').config();

const express = require('express');
const authMiddleware = require('./middleware/auth');
const proxyRoutes = require('./routes/proxy');

const app = express();

const PORT = process.env.PORT || 3000;

app.use(express.json());

app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET,POST,PUT,PATCH,DELETE,OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Authorization,Content-Type');
  if (req.method === 'OPTIONS') {
    return res.sendStatus(204);
  }
  next();
});

app.get('/health', (req, res) => {
  res.status(200).json({ status: 'ok' });
});

app.use(authMiddleware);

proxyRoutes(app);

app.listen(PORT, () => {
  console.log(`API Gateway corriendo en puerto ${PORT}`);
});