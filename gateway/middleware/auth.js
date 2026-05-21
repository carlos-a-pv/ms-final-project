const jwt = require('jsonwebtoken');
const SECRET = process.env.JWT_SECRET || 'mi_clave_secreta';

/** Roles admitidos en el claim `role` del JWT (minúsculas al comparar) */
const ALLOWED_ROLES = new Set(['user', 'administrator']);
const ROLE_ADMIN = 'administrator';

/**
 * Rutas que solo puede usar `administrator`.
 * GET y HEAD no pasan por aquí: ambos roles pueden leer (según reglas abajo).
 *
 * Añade entradas `{ methods: ['POST'], pattern: /^\/ruta$/ }` según necesites.
 */
const ADMIN_ONLY_RULES = [
  { methods: ['POST'], pattern: /^\/employees$/ },
  { methods: ['PUT', 'PATCH', 'DELETE'], pattern: /^\/employees\/[^/]+$/ },
];

const publicRoutes = [
  '/health',
  '/auth/login',
  '/auth/recover-password',
  '/auth/reset-password',
  '/docs',
  '/api-docs'
];

function requiresAdministrator(method, path) {
 const m = method.toUpperCase();
 const pathname = path.split('?')[0];

 return ADMIN_ONLY_RULES.some(
   (rule) => rule.methods.includes(m) && rule.pattern.test(pathname)
 );
}

// const requiresAdministrator = () => {
//   const m = method.toUpperCase();
//   const pathname = path.split('?')[0];

//   return ADMIN_ONLY_RULES.some(
//     (rule) => rule.methods.includes(m) && rule.pattern.test(pathname)
//   );
// }

module.exports = (req, res, next) => {
  const isPublic = publicRoutes.some(route =>
    req.path === route || req.path.startsWith(route + '/')
  );

  if (isPublic) {
    return next();
  }

  const token = req.headers['authorization'];

  if (!token) {
    return res.status(401).json({ error: 'Token requerido' });
  }

  try {
    const rawToken = token.replace('Bearer ', '');
    const decoded = jwt.verify(rawToken, SECRET);

    const role = decoded && typeof decoded.role === 'string' ? decoded.role.toLowerCase() : '';

    console.log(role)

    if (!ALLOWED_ROLES.has(role)) {
      return res.status(403).json({
        error: 'Rol no autorizado; se esperaba user o administrator',
      });
    }

    const method = req.method.toUpperCase();
    const readOnlyMethod = method === 'GET' || method === 'HEAD';

    if (!readOnlyMethod && requiresAdministrator(method, req.path)) {
      if (role !== ROLE_ADMIN) {
        return res.status(403).json({
          error: 'Se requiere rol administrator para esta operación',
        });
      }
    }

    req.user = decoded;
    next();
  } catch (err) {
    // ESTO TE DIRÁ EL ERROR REAL EN LA TERMINAL DEL GATEWAY
    console.error("ERROR DETECTADO EN JWT:", err.message); 
    return res.status(403).json({ error: 'Token inválido', detalle: err.message });
  }
};
