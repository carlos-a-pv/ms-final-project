import { type Profile } from "../models/Profile";
import { type ProfileRepository, type ProfileUpsertInput } from "../repositories/profileRepository";

export class ProfileService {
  constructor(private readonly repo: ProfileRepository) {}

  async getAllProfiles(): Promise<Profile[]> {
    return this.repo.findAll();
  }

  async getProfileByEmployeeId(employeeId: string): Promise<Profile | null> {
    return this.repo.findByEmployeeId(employeeId);
  }

  async updateProfile(employeeId: string, data: ProfileUpsertInput): Promise<Profile> {
    return this.repo.upsertByEmployeeId(employeeId, data);
  }
}
