declare module "swagger-ui-express" {
  import type { RequestHandler } from "express";

  export function setup(...args: unknown[]): RequestHandler;
  export function serve(...args: unknown[]): RequestHandler;
  export function serveFiles(...args: unknown[]): RequestHandler;

  const swaggerUi: {
    setup: typeof setup;
    serve: typeof serve;
    serveFiles: typeof serveFiles;
  };

  export default swaggerUi;
}
