import { Router } from "express";
import { MongoProfileRepository } from "../repositories/profileRepository";
import { ProfileService } from "../services/profileService";
import { ProfileController } from "../controllers/profileController";

const repo = new MongoProfileRepository();
const service = new ProfileService(repo);
const controller = new ProfileController(service);

export const profileRouter = Router();

profileRouter.get("/", controller.getAll);
profileRouter.get("/:employeeId", controller.getByEmployeeId);
profileRouter.put("/:employeeId", controller.updateByEmployeeId);
