const jwt = require('jsonwebtoken');

const SECRET = 'mi_clave_secreta';

module.exports = (req, res, next) => {
  const token = req.headers['authorization'];

  if (!token) {
    return res.status(401).json({ error: 'Token requerido' });
  }

  try {
    const decoded = jwt.verify(token.replace('Bearer ', ''), SECRET);
    req.user = decoded;
    next();
  } catch (err) {
    return res.status(403).json({ error: 'Token inválido' });
  }
};