const swaggerJsdoc = require('swagger-jsdoc');

const options = {
  definition: {
    openapi: '3.0.0',
    info: {
      title: 'API Gateway',
      version: '1.0.0',
      description: 'Gateway centralizado para microservicios RRHH'
    },

    servers: [
      {
        url: process.env.API_GATEWAY_URL || 'http://localhost:3000'
      }
    ],

    components: {
      securitySchemes: {
        bearerAuth: {
          type: 'http',
          scheme: 'bearer',
          bearerFormat: 'JWT'
        }
      }
    },

    security: [
      {
        bearerAuth: []
      }
    ]
  },

  apis: ['./routes/*.js']
};

module.exports = swaggerJsdoc(options);