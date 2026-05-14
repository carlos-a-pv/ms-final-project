import mongoose, { type InferSchemaType } from "mongoose";

const ProfileSchema = new mongoose.Schema(
  {
    employeeId: { type: String, required: true, unique: true, index: true },
    firstName: { type: String, required: true },
    lastName: { type: String, required: true },
    email: { type: String, required: true },
    phone: { type: String, required: false },
    title: { type: String, required: false },
    department: { type: String, required: false }
  },
  {
    timestamps: true,
    versionKey: false
  }
);

export type Profile = InferSchemaType<typeof ProfileSchema>;

export const ProfileModel = mongoose.model("Profile", ProfileSchema);
