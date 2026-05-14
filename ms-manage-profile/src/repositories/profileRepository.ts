import { ProfileModel, type Profile } from "../models/Profile";

export type ProfileUpsertInput = Omit<Profile, "employeeId" | "createdAt" | "updatedAt">;

export interface ProfileRepository {
  findAll(): Promise<Profile[]>;
  findByEmployeeId(employeeId: string): Promise<Profile | null>;
  upsertByEmployeeId(employeeId: string, data: ProfileUpsertInput): Promise<Profile>;
}

export class MongoProfileRepository implements ProfileRepository {
  async findAll(): Promise<Profile[]> {
    return ProfileModel.find({}).lean<Profile[]>().exec();
  }

  async findByEmployeeId(employeeId: string): Promise<Profile | null> {
    return ProfileModel.findOne({ employeeId }).lean<Profile | null>().exec();
  }

  async upsertByEmployeeId(employeeId: string, data: ProfileUpsertInput): Promise<Profile> {
    const updated = await ProfileModel.findOneAndUpdate(
      { employeeId },
      { $set: { ...data, employeeId } },
      { new: true, upsert: true }
    )
      .lean<Profile>()
      .exec();

    if (!updated) {
      throw new Error("Failed to upsert profile");
    }

    return updated;
  }
}
