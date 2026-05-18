const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = (app) => {
  const employeesTarget = process.env.EMPLOYEES_TARGET || 'http://ms-employees:8080';
  const departmentsTarget = process.env.DEPARTMENTS_TARGET || 'http://ms-departments:8081';

  app.use(
    '/api/employees',
    createProxyMiddleware({
      target: employeesTarget,
      changeOrigin: true,
      pathRewrite: {
        '^/api/employees': '',
      },
    })
  );

  app.use(
    '/api/departments',
    createProxyMiddleware({
      target: departmentsTarget,
      changeOrigin: true,
      pathRewrite: {
        '^/api/departments': '',
      },
    })
  );
};