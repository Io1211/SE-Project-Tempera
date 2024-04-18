import {Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {UsersService} from "../../_services/users.service";
import {NgForOf, NgIf} from "@angular/common";
import { ReactiveFormsModule } from "@angular/forms";
@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    ReactiveFormsModule
  ],
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {
  user: any;
  userId: string | null | undefined;

  constructor(
    private route: ActivatedRoute,
    private usersService: UsersService
  ) {
  }

  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('id');
    if (this.userId) {
      this.fetchUserDetails(this.userId);
    }
  }

  fetchUserDetails(id: string) {
    this.usersService.getUserById(id).subscribe({
      next: (data) => {
        this.user = data;
        console.log("User details: ", this.user);
      },
      error: (error) => {
        console.error('Failed to load user details:', error);
      }
    });
  }
}