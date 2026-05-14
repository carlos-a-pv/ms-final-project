import { type Request, type Response, type NextFunction } from "express";
import { z } from "zod";
import { ProfileService } from "../services/profileService";

const employeeIdSchema = z.string().min(1);

const updateProfileSchema = z.object({
  firstName: z.string().min(1),
  lastName: z.string().min(1),
  email: z.string().email(),
  phone: z.string().min(1).optional(),
  title: z.string().min(1).optional(),
  department: z.string().min(1).optional()
});

export class ProfileController {
  constructor(private readonly service: ProfileService) {}

  getAll = async (_req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const profiles = await this.service.getAllProfiles();
      res.status(200).json(profiles);
    } catch (err) {
      next(err);
    }
  };

  getByEmployeeId = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const employeeId = employeeIdSchema.parse(req.params.employeeId);
      const profile = await this.service.getProfileByEmployeeId(employeeId);

      if (!profile) {
        res.status(404).json({ message: "Profile not found" });
        return;
      }

      res.status(200).json(profile);
    } catch (err) {
      next(err);
    }
  };

  updateByEmployeeId = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const employeeId = employeeIdSchema.parse(req.params.employeeId);
      const body = updateProfileSchema.parse(req.body);

      const updated = await this.service.updateProfile(employeeId, body);
      res.status(200).json(updated);
    } catch (err) {
      next(err);
    }
  };
}
