import { createProxyMiddleware } from 'http-proxy-middleware';

export default (app) => {

  app.use('/api/employees', createProxyMiddleware({
    target: 'http://ms-employees:8080',
    changeOrigin: true,
    pathRewrite: {
      '^/api/employees': '',
    },
  }));

  app.use('/api/departments', createProxyMiddleware({
    target: 'http://ms-departments:8081',
    changeOrigin: true,
    pathRewrite: {
      '^/api/departments': '',
    },
  }));

};