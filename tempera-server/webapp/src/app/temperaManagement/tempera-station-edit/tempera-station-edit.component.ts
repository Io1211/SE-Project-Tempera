import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TemperaStationService } from '../../_services/tempera-station.service';
import { TemperaStation } from '../../models/temperaStation.model';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {User} from "../../models/user.model";
import {DropdownModule} from "primeng/dropdown";
import {UsersService} from "../../_services/users.service";

@Component({
  selector: 'app-tempera-station-edit',
  templateUrl: './tempera-station-edit.component.html',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule,
    FormsModule,
    CheckboxModule,
    ButtonModule,
    DropdownModule
  ],
  styleUrls: ['./tempera-station-edit.component.css']
})
export class TemperaStationEditComponent implements OnInit {

  temperaForm: FormGroup;

  @Input() temperaStation!: TemperaStation;
  @Output() onEditComplete = new EventEmitter<boolean>();
  users!: { label: string, value: User }[] | undefined;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private temperaStationService: TemperaStationService,
    private formBuilder: FormBuilder,
    private userService: UsersService
  ) {
    this.temperaForm = this.formBuilder.group({
      user: '',
      enabled: '',
      active: '',
    });
  }

  ngOnInit() {
    this.fetchUsers();
  }

  private fetchTemperaStationDetails(temperaStationId: string) {
    this.temperaStationService.getTemperaStationById(temperaStationId).subscribe({
      next: (data) => {
       this.temperaStation = data;
        this.populateForm();
      },
      error: (error) => {
        console.error('Failed to load temperaStation details:', error);
      },
    });
  }

  onSubmit() {
    if (this.temperaForm?.valid) {
      this.temperaStationService.updateTemperaStation(this.temperaStation.id, this.temperaForm.value).subscribe({
        next: () => {
          console.log('TemperaStation updated successfully');
          this.temperaForm?.reset();
          this.onEditComplete.emit(true);

        },
        error: (error) => {
          console.error('Failed to update temperaStation:', error);
          this.onEditComplete.emit(false);
        },
      });
    }
  }

  private fetchUsers() {
    this.userService.getAllUsers().subscribe({
      next: (users: User[]) => {
        this.users = users.map(user => ({
          label: `${user.firstName} ${user.lastName}`, value: user
        }));
      },
      error: (error) => console.error('Error loading managers:', error)
    });
  }

  populateForm() {
    this.temperaForm = this.formBuilder.group({
      user: this.temperaStation.user,
      enabled: this.temperaStation.enabled,
      active: this.temperaStation.active,
    });
  }
}