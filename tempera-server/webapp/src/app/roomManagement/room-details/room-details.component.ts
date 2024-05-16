import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RoomControllerService, Room } from '../../../api';
import { RoomService } from '../../_services/room.service';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { NgForOf } from '@angular/common';

@Component({
  selector: 'app-room-details',
  standalone: true,
  imports: [
    TableModule,
    CardModule,
    NgForOf,
  ],
  templateUrl: './room-details.component.html',
  styleUrl: './room-details.component.css',
})
export class RoomDetailsComponent {
  private roomId!: string;
  room: Room | undefined;

  constructor(
    private route: ActivatedRoute,
    private roomService: RoomControllerService,
  ) {
  }

  ngOnInit() {
    this.roomId = this.route.snapshot.paramMap.get('id')!;
    if (this.roomId) {
      this.fetchRoomDetails(this.roomId);
    }
  }

  private fetchRoomDetails(roomId: string) {
    this.roomService.getRoomById(roomId).subscribe({
      next: (data) => {
        console.log('Room details: ', data);
        this.room = data;
      },
      error: (error) => {
        console.error('Failed to load room details:', error);
      },
    });
  }
}
