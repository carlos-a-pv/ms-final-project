const { createProxyMiddleware, fixRequestBody } = require('http-proxy-middleware');

const aggregator = require('../services/aggregator');

module.exports = (app) => {
  const employeesTarget = process.env.EMPLOYEES_TARGET || 'http://ms-employees:8080/api/v1/employees';
  const departmentsTarget = process.env.DEPARTMENTS_TARGET || 'http://ms-departments:8081/api/v1';
  const notificationsTarget = process.env.NOTIFICATIONS_TARGET || 'http://ms-notifications:8083';
  const profileTarget = process.env.PROFILE_TARGET || 'http://ms-profile:8084';
  const authTarget = process.env.AUTH_TARGET || 'http://ms-auth:8080/api/v1/auth';
  const vacationsTarget = process.env.VACATIONS_TARGET || 'http://ms-vacations:8086';
  
/**
 * @swagger
 * tags:
 *   name: Auth
 *   description: Operaciones de autenticación
 */

/**
 * @swagger
 * /auth/login:
 *   post:
 *     summary: Iniciar sesión
 *     description: |
 *       Permite autenticar un usuario mediante correo y contraseña.
 *       Retorna un JWT válido para consumir endpoints protegidos.
 *
 *     tags:
 *       - Auth
 *
 *     security: []
 *
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - email
 *               - password
 *             properties:
 *               username:
 *                 type: string
 *                 example: admin@empresa.com
 *
 *               password:
 *                 type: string
 *                 example: admin123
 *
 *     responses:
 *
 *       200:
 *         description: Autenticación exitosa
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 token:
 *                   type: string
 *                   example: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
 *
 *                 expiresIn:
 *                   type: string
 *                   example: 1h
 *
 *                 user:
 *                   type: object
 *                   properties:
 *                     id:
 *                       type: integer
 *                       example: 10
 *
 *                     email:
 *                       type: string
 *                       example: admin@empresa.com
 *
 *                     role:
 *                       type: string
 *                       example: ADMIN
 *
 *       400:
 *         description: Datos inválidos
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Email y contraseña son requeridos
 *
 *       401:
 *         description: Credenciales inválidas
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Credenciales inválidas
 *
 *       403:
 *         description: Usuario bloqueado o sin acceso
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Usuario deshabilitado
 *
 *       500:
 *         description: Error interno del servidor
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Error interno del servidor
 */

/**
 * @swagger
 * /auth/recover-password:
 *   post:
 *     summary: Recuperar contraseña
 *     description: |
 *       Inicia el proceso de recuperación de contraseña.
 *       Envía un correo al usuario con instrucciones o un token
 *       para restablecer su contraseña.
 *
 *     tags:
 *       - Auth
 *
 *     security: []
 *
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - email
 *             properties:
 *               email:
 *                 type: string
 *                 example: admin@empresa.com
 *
 *     responses:
 *
 *       200:
 *         description: Solicitud de recuperación enviada
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 message:
 *                   type: string
 *                   example: Se enviaron instrucciones de recuperación al correo indicado
 *
 *       400:
 *         description: Datos inválidos
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Email es requerido
 *
 *       404:
 *         description: Email no registrado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Email no encontrado
 *
 *       500:
 *         description: Error interno del servidor
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Error al procesar la recuperación de contraseña
 */

/**
 * @swagger
 * /auth/reset-password:
 *   post:
 *     summary: Restablecer contraseña
 *     description: |
 *       Permite restablecer la contraseña de un usuario mediante correo.
 *
 *     tags:
 *       - Auth
 *
 *     security: []
 *
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - email
 *               - password
 *             properties:
 *               email:
 *                 type: string
 *                 example: admin@empresa.com
 *
 *               password:
 *                 type: string
 *                 example: Admin123*
 *
 *     responses:
 *
 *       200:
 *         description: Contraseña restablecida exitosamente
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 message:
 *                   type: string
 *                   example: Contraseña restablecida exitosamente
 *
 *       400:
 *         description: Datos inválidos
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Email es requerido
 *
 *       401:
 *         description: Email no encontrado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Email no encontrado
 *
 *       403:
 *         description: Error al restablecer la contraseña
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Error al restablecer la contraseña
 *
 *       500:
 *         description: Error interno del servidor
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Error al restablecer la contraseña
 */

app.use(
  '/auth',
  createProxyMiddleware({
    target: authTarget, 
    changeOrigin: true,
    logLevel: 'debug',
    pathRewrite: {
      '^/auth': '/auth', 
    },
    on: {
      proxyReq: (proxyReq, req, res) => {
        if (req.body && Object.keys(req.body).length > 0) {
          const bodyData = JSON.stringify(req.body);
          proxyReq.setHeader('Content-Type', 'application/json');
          proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
          proxyReq.write(bodyData);
        }
      },
      proxyRes: (proxyRes, req, res) => {
        console.log(`[Proxy] Respuesta recibida del microservicio. Status: ${proxyRes.statusCode}`);
      }
    }
  })
);


/**
 * @swagger
 * tags:
 *   name: Employees
 *   description: Gestión de empleados
 */

/**
 * @swagger
 * /employees:
 *   get:
 *     summary: Consultar empleados
 *     description: |
 *       Obtiene el listado de todos los empleados registrados.
 *     tags:
 *       - Employees
 *     security:
 *       - bearerAuth: []
 *     responses:
 *       200:
 *         description: Listado de empleados
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   id:
 *                     type: integer
 *                     example: 5
 *                   name:
 *                     type: string
 *                     example: Juan Pérez
 *                   email:
 *                     type: string
 *                     example: juan@empresa.com
 *                   departmentId:
 *                     type: integer
 *                     example: 2
 *       401:
 *         description: Token requerido
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       500:
 *         description: Error interno del servidor
 *   post:
 *     summary: Crear empleado
 *     description: |
 *       Registra un nuevo empleado en el sistema.
 *     tags:
 *       - Employees
 *     security:
 *       - bearerAuth: []
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - name
 *               - position
 *               - email
 *               - departmentId
 *               - hiringDate
 *             properties:
 *               name:
 *                 type: string
 *                 example: María López
 *               position:
 *                 type: string
 *                 example: Desarrollador
 *               email:
 *                 type: string
 *                 example: maria@empresa.com
 *               departmentId:
 *                 type: integer
 *                 example: 2
 *               hiringDate:
 *                 type: string
 *                 format: date
 *                 example: 2026-05-20T21:19:40.544Z
 *     responses:
 *       201:
 *         description: Empleado creado exitosamente
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 id:
 *                   type: integer
 *                   example: 12
 *                 name:
 *                   type: string
 *                   example: María López
 *                 position:
 *                   type: string
 *                   example: Desarrollador
 *                 email:
 *                   type: string
 *                   example: maria@empresa.com
 *                 departmentId:
 *                   type: integer
 *                   example: 2               
 *                 hiringDate:
 *                   type: string
 *                   format: date
 *                   example: 2026-05-20
 *       400:
 *         description: Datos inválidos
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       409:
 *         description: El correo ya está registrado
 *       500:
 *         description: Error interno del servidor
 */

/**
 * @swagger
 * /employees/{id}:
 *   get:
 *     summary: Consultar empleado por ID
 *     description: |
 *       Obtiene la información completa del empleado,
 *       incluyendo perfil asociado.
 *
 *     tags:
 *       - Employees
 *
 *     security:
 *       - bearerAuth: []
 *
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del empleado
 *         example: 5
 *
 *     responses:
 *
 *       200:
 *         description: Empleado encontrado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 id:
 *                   type: integer
 *                   example: 5
 *
 *                 name:
 *                   type: string
 *                   example: Juan Pérez
 *
 *                 email:
 *                   type: string
 *                   example: juan@empresa.com
 *
 *                 department:
 *                   type: string
 *                   example: Tecnología
 *
 *                 profile:
 *                   type: object
 *                   properties:
 *                     phone:
 *                       type: string
 *                       example: 3001234567
 *
 *                     address:
 *                       type: string
 *                       example: Bogotá
 *
 *       401:
 *         description: Token requerido
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Token requerido
 *
 *       403:
 *         description: Token inválido o expirado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Token inválido
 *
 *       404:
 *         description: Empleado no encontrado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Empleado no encontrado
 *
 *       500:
 *         description: Error interno
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Error obteniendo empleado
 *   put:
 *     summary: Actualizar empleado
 *     description: |
 *       Actualiza los datos de un empleado existente.
 *     tags:
 *       - Employees
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del empleado
 *         example: 5
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               name:
 *                 type: string
 *                 example: Juan Pérez García
 *               email:
 *                 type: string
 *                 example: juan.perez@empresa.com
 *               departmentId:
 *                 type: integer
 *                 example: 3
 *     responses:
 *       200:
 *         description: Empleado actualizado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 id:
 *                   type: integer
 *                   example: 5
 *                 name:
 *                   type: string
 *                   example: Juan Pérez García
 *                 email:
 *                   type: string
 *                   example: juan.perez@empresa.com
 *                 departmentId:
 *                   type: integer
 *                   example: 3
 *       400:
 *         description: Datos inválidos
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       404:
 *         description: Empleado no encontrado
 *       500:
 *         description: Error interno del servidor
 * 
 *   delete:
 *     summary: Eliminar empleado
 *     description: |
 *       Elimina un empleado del sistema por su ID.
 *     tags:
 *       - Employees
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del empleado
 *         example: 5
 *     responses:
 *       204:
 *         description: Empleado eliminado exitosamente
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       404:
 *         description: Empleado no encontrado
 *       500:
 *         description: Error interno del servidor
 */

  app.use(
    '/employees',
    createProxyMiddleware({
      target: employeesTarget,
      changeOrigin: true,
      logLevel: 'debug',
      pathRewrite: {
        '^/employees': '/employees',
      },
      on: {
        proxyReq: (proxyReq, req, res) => {
          if(req.body) {
            const bodyData = JSON.stringify(req.body);
            proxyReq.setHeader('Content-Type', 'application/json');
            proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
            proxyReq.write(bodyData);
          }
        }
      }
    })
  );


/**
 * @swagger
 * tags:
 *   name: Departments
 *   description: Gestión de departamentos
 */

/**
 * @swagger
 * /departments:
 *   get:
 *     summary: Consultar departamentos
 *     description: |
 *       Obtiene el listado de todos los departamentos registrados.
 *     tags:
 *       - Departments
 *     security:
 *       - bearerAuth: []
 *     responses:
 *       200:
 *         description: Listado de departamentos
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   id:
 *                     type: integer
 *                     example: 1
 *                   name:
 *                     type: string
 *                     example: Tecnología
 *                   description:
 *                     type: string
 *                     example: Área de desarrollo y sistemas
 *       401:
 *         description: Token requerido
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       500:
 *         description: Error interno del servidor
 *   post:
 *     summary: Crear departamento
 *     description: |
 *       Registra un nuevo departamento en el sistema.
 *     tags:
 *       - Departments
 *     security:
 *       - bearerAuth: []
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - name
 *             properties:
 *               name:
 *                 type: string
 *                 example: Recursos Humanos
 *               description:
 *                 type: string
 *                 example: Gestión del talento humano
 *     responses:
 *       201:
 *         description: Departamento creado exitosamente
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 id:
 *                   type: integer
 *                   example: 3
 *                 name:
 *                   type: string
 *                   example: Recursos Humanos
 *                 description:
 *                   type: string
 *                   example: Gestión del talento humano
 *       400:
 *         description: Datos inválidos
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       409:
 *         description: El departamento ya existe
 *       500:
 *         description: Error interno del servidor
 */

/**
 * @swagger
 * /departments/{id}:
 *   get:
 *     summary: Consultar departamento por ID
 *     description: |
 *       Obtiene la información de un departamento por su identificador.
 *     tags:
 *       - Departments
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del departamento
 *         example: 1
 *     responses:
 *       200:
 *         description: Departamento encontrado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 id:
 *                   type: integer
 *                   example: 1
 *                 name:
 *                   type: string
 *                   example: Tecnología
 *                 description:
 *                   type: string
 *                   example: Área de desarrollo y sistemas
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       404:
 *         description: Departamento no encontrado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 error:
 *                   type: string
 *                   example: Departamento no encontrado
 *       500:
 *         description: Error interno del servidor
 *   put:
 *     summary: Actualizar departamento
 *     description: |
 *       Actualiza los datos de un departamento existente.
 *     tags:
 *       - Departments
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del departamento
 *         example: 1
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               name:
 *                 type: string
 *                 example: Tecnología e Innovación
 *               description:
 *                 type: string
 *                 example: Desarrollo de software y transformación digital
 *     responses:
 *       200:
 *         description: Departamento actualizado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 id:
 *                   type: integer
 *                   example: 1
 *                 name:
 *                   type: string
 *                   example: Tecnología e Innovación
 *                 description:
 *                   type: string
 *                   example: Desarrollo de software y transformación digital
 *       400:
 *         description: Datos inválidos
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       404:
 *         description: Departamento no encontrado
 *       500:
 *         description: Error interno del servidor
 *   delete:
 *     summary: Eliminar departamento
 *     description: |
 *       Elimina un departamento del sistema por su ID.
 *     tags:
 *       - Departments
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del departamento
 *         example: 1
 *     responses:
 *       204:
 *         description: Departamento eliminado exitosamente
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       404:
 *         description: Departamento no encontrado
 *       409:
 *         description: No se puede eliminar; tiene empleados asociados
 *       500:
 *         description: Error interno del servidor
 */

  app.use(
    '/departments',
    createProxyMiddleware({
      target: departmentsTarget,
      changeOrigin: true
    })
  );

/**
 * @swagger
 * tags:
 *   name: Notifications
 *   description: Gestión de notificaciones
 */

/**
 * @swagger
 * /notifications:
 *   get:
 *     summary: Obtener notificaciones
 *     description: |
 *       Obtiene el listado de todas las notificaciones del sistema.
 *     tags:
 *       - Notifications
 *     security:
 *       - bearerAuth: []
 *     responses:
 *       200:
 *         description: Listado de notificaciones
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   id:
 *                     type: integer
 *                     example: 1
 *                   employeeId:
 *                     type: integer
 *                     example: 5
 *                   message:
 *                     type: string
 *                     example: Su solicitud de vacaciones fue aprobada
 *                   type:
 *                     type: string
 *                     example: VACATION_APPROVED
 *                   read:
 *                     type: boolean
 *                     example: false
 *                   createdAt:
 *                     type: string
 *                     format: date-time
 *                     example: 2026-05-18T10:30:00Z
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       500:
 *         description: Error interno del servidor
 */

/**
 * @swagger
 * /notifications/employee/{employeeId}:
 *   get:
 *     summary: Notificaciones por empleado
 *     description: |
 *       Obtiene las notificaciones asociadas a un empleado específico.
 *     tags:
 *       - Notifications
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: employeeId
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del empleado
 *         example: 5
 *     responses:
 *       200:
 *         description: Notificaciones del empleado
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   id:
 *                     type: integer
 *                     example: 1
 *                   employeeId:
 *                     type: integer
 *                     example: 5
 *                   message:
 *                     type: string
 *                     example: Bienvenido al sistema
 *                   type:
 *                     type: string
 *                     example: WELCOME
 *                   read:
 *                     type: boolean
 *                     example: true
 *                   createdAt:
 *                     type: string
 *                     format: date-time
 *                     example: 2026-05-18T08:00:00Z
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       404:
 *         description: Empleado no encontrado
 *       500:
 *         description: Error interno del servidor
 */

  app.use(
    '/notifications',
    createProxyMiddleware({
      target: notificationsTarget,
      changeOrigin: true
    })
  );

/**
 * @swagger
 * tags:
 *   name: Profile
 *   description: Perfil de empleados
 */

/**
 * @swagger
 * /profile/{employeeId}:
 *   get:
 *     summary: Consultar perfil
 *     description: |
 *       Obtiene el perfil asociado a un empleado por su ID.
 *     tags:
 *       - Profile
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: employeeId
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del empleado
 *         example: 5
 *     responses:
 *       200:
 *         description: Perfil encontrado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 employeeId:
 *                   type: integer
 *                   example: 5
 *                 phone:
 *                   type: string
 *                   example: 3001234567
 *                 address:
 *                   type: string
 *                   example: Bogotá, Colombia
 *                 birthDate:
 *                   type: string
 *                   format: date
 *                   example: 1990-03-15
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       404:
 *         description: Perfil no encontrado
 *       500:
 *         description: Error interno del servidor
 *   put:
 *     summary: Actualizar perfil
 *     description: |
 *       Actualiza la información de perfil de un empleado.
 *     tags:
 *       - Profile
 *     security:
 *       - bearerAuth: []
 *     parameters:
 *       - in: path
 *         name: employeeId
 *         required: true
 *         schema:
 *           type: integer
 *         description: ID del empleado
 *         example: 5
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               phone:
 *                 type: string
 *                 example: 3009876543
 *               address:
 *                 type: string
 *                 example: Medellín, Colombia
 *               birthDate:
 *                 type: string
 *                 format: date
 *                 example: 1990-03-15
 *     responses:
 *       200:
 *         description: Perfil actualizado
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 employeeId:
 *                   type: integer
 *                   example: 5
 *                 phone:
 *                   type: string
 *                   example: 3009876543
 *                 address:
 *                   type: string
 *                   example: Medellín, Colombia
 *                 birthDate:
 *                   type: string
 *                   format: date
 *                   example: 1990-03-15
 *       400:
 *         description: Datos inválidos
 *       401:
 *         description: Token requerido
 *       403:
 *         description: Token inválido o expirado
 *       404:
 *         description: Perfil no encontrado
 *       500:
 *         description: Error interno del servidor
 */

  app.use(
    '/profile',
    createProxyMiddleware({
      target: profileTarget,
      changeOrigin: true
    })
  );

  app.use(
    '/vacations',
    createProxyMiddleware({
      target: vacationsTarget,
      changeOrigin: true
    })
  );


  app.get('/employees/:id', async (req, res) => {
    try {

      const result =
        await aggregator.getEmployeeComplete(
          req.params.id,
          req.headers.authorization
        );
  
      res.json(result);
  
    } catch (error) {
  
      res.status(500).json({
        error: 'Error obteniendo empleado completo'
      });
    }
  })
};