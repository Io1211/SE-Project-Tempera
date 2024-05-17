import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AccessPointService} from "../../_services/access-point.service";
import {AccessPoint} from "../../models/accessPoint.model";
import {NgIf} from "@angular/common";
import {CardModule} from "primeng/card";
import {Message} from "primeng/api";
import {MessagesModule} from "primeng/messages";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {AccessPointCreateComponent} from "../access-point-create/access-point-create.component";
import {AccessPointEditComponent} from "../access-point-edit/access-point-edit.component";
import {InputTextModule} from "primeng/inputtext";


@Component({
  selector: 'app-accespoints',
  standalone: true,
  imports: [
    NgIf,
    CardModule,
    MessagesModule,
    TableModule,
    ButtonModule,
    DialogModule,
    AccessPointCreateComponent,
    AccessPointEditComponent,
    InputTextModule
  ],
  templateUrl: './accespoints.component.html',
  styleUrl: './accespoints.component.css'
})
export class AccesspointsComponent implements OnInit{

  accessPoints : AccessPoint[] | undefined ;
  filteredAccessPoints: AccessPoint[] = [];
  selectedAccessPoint: AccessPoint | null = null;
  displayCreateDialog: boolean = false;
  displayEditDialog: boolean = false;
  messages: Message[] = [];

  constructor(private accessPointService: AccessPointService, private router: Router) {}
  ngOnInit(): void {
    this.loadAccessPoints();
  }


  private loadAccessPoints() {
    this.accessPointService.getAllAccesspoints().subscribe({
      next: (accessPoints) => {
        this.accessPoints = accessPoints;
        this.filteredAccessPoints = accessPoints;
        console.log("Loaded accesspoints:", accessPoints);
      },
      error: (error) => {
        console.error("Error loading accesspoints:", error);
      }
    });
  }
  deleteAccesspoints(accessPoint: AccessPoint) {
    if (accessPoint) {
        this.accessPointService.deleteAccesspoint(accessPoint.id).subscribe({
          next: () => {
            console.log(`Deleted access point with id: ${accessPoint.id}`);
          },
          error: (error) => {
            console.error(`Error deleting access point with id: ${accessPoint.id}`, error);
          }
        });
    }
  }

  applyFilter(event: any): void {
    const filterValue = (event.target as HTMLInputElement).value.toLowerCase();
    this.filteredAccessPoints = this.accessPoints!.filter(accessPoint =>
      accessPoint.room.id.toLowerCase().includes(filterValue)
    );
  }

  createAccessPoint(): void {
    this.displayCreateDialog = true;
  }

  editAccessPoint(accessPoint: AccessPoint): void {
    this.selectedAccessPoint = accessPoint;
    this.displayEditDialog = true;
  }

  onCreateComplete(event: any): void {
    this.displayCreateDialog = false;
    this.loadAccessPoints();
    this.messages = [{ severity: 'success', summary: 'Success', detail: 'Access Point created successfully' }];
  }

  onEditComplete(event: any): void {
    this.displayEditDialog = false;
    this.loadAccessPoints();
    this.messages = [{ severity: 'success', summary: 'Success', detail: 'Access Point updated successfully' }];
  }

  viewAccessPointDetails(accessPoint: AccessPoint) {
    this.router.navigate(['/accessPoint', accessPoint.id])
  }
}
