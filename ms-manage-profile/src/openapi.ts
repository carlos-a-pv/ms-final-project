import swaggerJSDoc from "swagger-jsdoc";

export const openApiSpec = swaggerJSDoc({
  definition: {
    openapi: "3.0.3",
    info: {
      title: "Manage Profile API",
      version: "1.0.0"
    },
    servers: [{ url: "http://localhost:3000" }],
    tags: [{ name: "Profiles" }],
    components: {
      schemas: {
        Profile: {
          type: "object",
          properties: {
            employeeId: { type: "string" },
            firstName: { type: "string" },
            lastName: { type: "string" },
            email: { type: "string", format: "email" },
            phone: { type: "string", nullable: true },
            title: { type: "string", nullable: true },
            department: { type: "string", nullable: true },
            createdAt: { type: "string", format: "date-time" },
            updatedAt: { type: "string", format: "date-time" }
          },
          required: ["employeeId", "firstName", "lastName", "email"]
        },
        UpdateProfileRequest: {
          type: "object",
          properties: {
            firstName: { type: "string" },
            lastName: { type: "string" },
            email: { type: "string", format: "email" },
            phone: { type: "string" },
            title: { type: "string" },
            department: { type: "string" }
          },
          required: ["firstName", "lastName", "email"]
        },
        ErrorResponse: {
          type: "object",
          properties: {
            message: { type: "string" }
          },
          required: ["message"]
        }
      }
    },
    paths: {
      "/profiles": {
        get: {
          tags: ["Profiles"],
          summary: "List all profiles",
          responses: {
            "200": {
              description: "OK",
              content: {
                "application/json": {
                  schema: { type: "array", items: { $ref: "#/components/schemas/Profile" } }
                }
              }
            }
          }
        }
      },
      "/profiles/{employeeId}": {
        get: {
          tags: ["Profiles"],
          summary: "Get profile by employeeId",
          parameters: [
            {
              name: "employeeId",
              in: "path",
              required: true,
              schema: { type: "string" }
            }
          ],
          responses: {
            "200": {
              description: "OK",
              content: {
                "application/json": {
                  schema: { $ref: "#/components/schemas/Profile" }
                }
              }
            },
            "404": {
              description: "Not found",
              content: {
                "application/json": {
                  schema: { $ref: "#/components/schemas/ErrorResponse" }
                }
              }
            }
          }
        },
        put: {
          tags: ["Profiles"],
          summary: "Create or update a profile by employeeId",
          parameters: [
            {
              name: "employeeId",
              in: "path",
              required: true,
              schema: { type: "string" }
            }
          ],
          requestBody: {
            required: true,
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/UpdateProfileRequest" }
              }
            }
          },
          responses: {
            "200": {
              description: "OK",
              content: {
                "application/json": {
                  schema: { $ref: "#/components/schemas/Profile" }
                }
              }
            },
            "400": {
              description: "Validation error",
              content: {
                "application/json": {
                  schema: { $ref: "#/components/schemas/ErrorResponse" }
                }
              }
            }
          }
        }
      }
    }
  },
  apis: []
});
