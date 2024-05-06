import { Component, OnInit } from '@angular/core';
import { State, User } from '../models/user.model';
import { DatePipe, NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AirQualityPipe } from '../_pipes/air-quality.pipe';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { ColleagueStateDto, HomeControllerService, HomeDataResponse, UserxDto } from '../../api';
import StateEnum = ColleagueStateDto.StateEnum;
import VisibilityEnum = HomeDataResponse.VisibilityEnum;
import { StorageService } from '../_services/storage.service';
import { ButtonModule } from 'primeng/button';
import RolesEnum = UserxDto.RolesEnum;

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgIf,
    MessageModule,
    DropdownModule,
    TableModule,
    TagModule,
    DatePipe,
    AirQualityPipe,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    ButtonModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  public homeData?: HomeDataResponse;
  public user?: User;

  public filterFields: string[] = [];

  // todo: delete when actual data is available
  public visibilityOptions: VisibilityEnum[] = Object.values(VisibilityEnum);

  protected readonly RolesEnum = RolesEnum;

  constructor(private homeService: HomeControllerService, private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.homeService.homeData().subscribe({
      next: data => {
        this.homeData = data;
        this.filterFields = Object.keys(this.homeData?.colleagueStates?.[0] ?? []);
      },
      error: err => {
        console.log(err);
      },
    });

    this.user = this.storageService.getUser();
  }

  getSeverity(state: State) {
    switch (state) {
      case State.AVAILABLE:
        return 'success';
      case State.MEETING:
        return 'warning';
      case State.DEEPWORK:
        return 'info';
      case State.OUT_OF_OFFICE:
        return 'danger';
      default:
        return 'primary';
    }
  }

  showState(state: StateEnum | undefined) {
    switch (state) {
      case StateEnum.Available:
        return 'Available';
      case StateEnum.Meeting:
        return 'In a meeting';
      case StateEnum.Deepwork:
        return 'Deep work';
      case StateEnum.OutOfOffice:
        return 'Out of office';
      default:
        return 'Unknown';

    }
  }
}
