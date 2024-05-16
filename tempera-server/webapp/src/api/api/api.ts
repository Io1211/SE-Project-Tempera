export * from './authController.service';
import { AuthControllerService } from './authController.service';
export * from './dashboardController.service';
import { DashboardControllerService } from './dashboardController.service';
export * from './groupManagementController.service';
import { GroupManagementControllerService } from './groupManagementController.service';
export * from './measurementController.service';
import { MeasurementControllerService } from './measurementController.service';
export * from './peripheryConnectionController.service';
import { PeripheryConnectionControllerService } from './peripheryConnectionController.service';
export * from './projectController.service';
import { ProjectControllerService } from './projectController.service';
export * from './roomController.service';
import { RoomControllerService } from './roomController.service';
export * from './timeRecordController.service';
import { TimeRecordControllerService } from './timeRecordController.service';
export * from './timetableController.service';
import { TimetableControllerService } from './timetableController.service';
export * from './userManagementController.service';
import { UserManagementControllerService } from './userManagementController.service';
export const APIS = [AuthControllerService, DashboardControllerService, GroupManagementControllerService, MeasurementControllerService, PeripheryConnectionControllerService, ProjectControllerService, RoomControllerService, TimeRecordControllerService, TimetableControllerService, UserManagementControllerService];
