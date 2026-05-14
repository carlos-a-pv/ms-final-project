import { createApp } from "./app";
import { connectToDatabase } from "./config/database";

const port = Number(process.env.PORT ?? 3000);

async function bootstrap(): Promise<void> {
  await connectToDatabase();

  const app = createApp();

  app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
  });
}

bootstrap().catch((err) => {
  console.error("Failed to start server", err);
  process.exit(1);
});
