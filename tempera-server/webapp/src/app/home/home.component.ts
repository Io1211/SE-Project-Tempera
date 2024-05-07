import { Component, OnInit } from '@angular/core';
import { DatePipe, NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ColleagueStateDto, HomeControllerService, HomeDataResponse } from '../../api';
import StateEnum = ColleagueStateDto.StateEnum;

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
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  public homeData?: HomeDataResponse;


  constructor(private homeService: HomeControllerService) {
  }

  ngOnInit(): void {
    this.homeService.homeData().subscribe({
      next: data => {
        this.homeData = data;
      },
      error: err => {
        console.log(err);
      },
    });
  }

  getSeverity(state: StateEnum) {
    switch (state) {
      case StateEnum.Available:
        return 'success';
      case StateEnum.Meeting:
        return 'warning';
      case StateEnum.Deepwork:
        return 'info';
      case StateEnum.OutOfOffice:
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
