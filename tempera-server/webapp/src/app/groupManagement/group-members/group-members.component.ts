import {Component, OnInit} from '@angular/core';
import {User} from "../../models/user.model";
import {ActivatedRoute, Router} from "@angular/router";
import {GroupService} from "../../_services/group.service";
import {SharedModule} from "primeng/api";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {UsersService} from "../../_services/users.service";
import {DialogModule} from "primeng/dialog";
@Component({
  selector: 'app-group-members',
  standalone: true,
  imports: [
    SharedModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    DialogModule
  ],
  templateUrl: './group-members.component.html',
  styleUrl: './group-members.component.css'
})
export class GroupMembersComponent implements OnInit{

  members: User[] = [];
  users: User[] = [];
  displayAddDialog: boolean = false;
  groupId: string | null | undefined;
  filteredMembers: User[] = [];
  filteredUsers: User[] = [];

  constructor(private groupService: GroupService, private userService: UsersService ,private route: ActivatedRoute) {

  }
  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get('id');
    this.loadMembersAndUsers(this.groupId!);
  }


  loadMembersAndUsers(groupId: string) {
    // Load members
    this.groupService.getGroupMembers(groupId).subscribe({
      next: members => {
        this.members = members;
        this.filteredMembers = members;
        // Load all users
        this.userService.getAllUsers().subscribe({
          next: users => {
            this.users = users.filter((user: { id: string; }) =>
              !this.members.some(member => member.id === user.id));
              this.filteredUsers = [...this.users];
          },
          error: err => console.error("Error loading users:", err)
        });
      },
      error: err => console.error("Error loading group members:", err)
    });
  }

  applyFilterMembers(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredMembers = this.members.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName.toLowerCase().includes(filterValue.toLowerCase()),
      );
    } else {
      this.filteredMembers = this.members;
    }
  }

  applyFilterUsers(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredUsers = this.users.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName.toLowerCase().includes(filterValue.toLowerCase()),
      );
    } else {
      this.filteredUsers = this.users;
    }
  }

  addMember(){
    this.displayAddDialog = true;
  }
}
