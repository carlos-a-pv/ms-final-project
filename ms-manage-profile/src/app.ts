import dotenv from "dotenv";
dotenv.config();

import express, { type Express } from "express";
import { profileRouter } from "./routes/profileRoutes";
import { errorHandler } from "./middlewares/errorHandler";
import swaggerUi from "swagger-ui-express";
import { openApiSpec } from "./openapi";

export function createApp(): Express {
  const app = express();

  app.use(express.json());

  app.get("/health", (_req, res) => {
    res.status(200).json({ status: "ok" });
  });

  app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(openApiSpec));

  app.use("/profiles", profileRouter);

  app.use(errorHandler);

  return app;
}
