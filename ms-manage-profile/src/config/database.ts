import mongoose from "mongoose";

export async function connectToDatabase(): Promise<void> {
  const mongoUri = process.env.MONGODB_URI;
  if (!mongoUri) {
    throw new Error("Missing MONGODB_URI environment variable");
  }

  await mongoose.connect(mongoUri);
}
