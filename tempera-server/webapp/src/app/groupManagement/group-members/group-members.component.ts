import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GroupService } from '../../_services/group.service';
import { SharedModule } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import {
  UserManagementControllerService,
  GroupManagementControllerService,
  UserxDto,
  MemberAssigmentDto,
} from '../../../api';
import { DialogModule } from 'primeng/dialog';
import { MessagesModule } from 'primeng/messages';
import { GroupMemberDTO } from '../../models/groupDtos';
import { from } from 'rxjs';
import { concatMap } from 'rxjs/operators';

@Component({
  selector: 'app-group-members',
  standalone: true,
  imports: [
    SharedModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    DialogModule,
    MessagesModule,
  ],
  templateUrl: './group-members.component.html',
  styleUrl: './group-members.component.css',
})
export class GroupMembersComponent implements OnInit {

  members: UserxDto[] = [];
  users: UserxDto[] = [];
  displayAddDialog: boolean = false;
  groupId: number | null | undefined;
  filteredMembers: UserxDto[] = [];
  filteredUsers: UserxDto[] = [];
  selectedUsers: UserxDto[] = [];
  messages: any;

  constructor(private groupService: GroupManagementControllerService, private userService: UserManagementControllerService, private route: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.groupId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadMembersAndUsers(this.groupId!);
  }


  loadMembersAndUsers(groupId: number) {
    // Load members
    // todo: check if correct despite toString() (same with deleteMember)
    this.groupService.getMembers(groupId.toString()).subscribe({
      next: members => {
        this.members = members;
        this.filteredMembers = [...members];
        // Load all users that are not members
        this.userService.getAllUsers().subscribe({
          next: users => {
            this.users = users.filter((user: { username: string; }) =>
              !this.members.some(member => member.username === user.username));
            this.filteredUsers = [...this.users];
          },
          error: err => console.error('Error loading users:', err),
        });
      },
      error: err => console.error('Error loading group members:', err),
    });
  }

  applyFilterMembers(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredMembers = this.members.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName?.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName?.toLowerCase().includes(filterValue.toLowerCase()),
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
        user.firstName?.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName?.toLowerCase().includes(filterValue.toLowerCase()),
      );
    } else {
      this.filteredUsers = this.users;
    }
  }

  addMemberDialog() {
    this.loadMembersAndUsers(this.groupId!);
    this.displayAddDialog = true;
  }

  addMembers() {
    from(this.selectedUsers.map(user => user.username))
      .pipe(concatMap(userId => this.addMember(userId)))
      .subscribe({
        next: response => {
          console.log('Member added successfully:', response);
          this.loadMembersAndUsers(this.groupId!);
          this.messages = [{ severity: 'success', summary: 'Success', detail: 'Members added successfully' }];
        },
        error: err => console.error('Error adding member:', err),
      });
    this.displayAddDialog = false;
    this.selectedUsers = [];
  }

  private addMember(userId: string) {
    const dto: MemberAssigmentDto = {
      groupId: this.groupId!,
      memberId: userId,
    };

    return this.groupService.addMember(dto);
  }

  deleteMember(userId: string) {
    this.groupService.removeMember(this.groupId?.toString()!, userId).subscribe({
      next: response => {
        console.log('Member deleted successfully:', response);
        this.loadMembersAndUsers(this.groupId!);
        this.messages = [{ severity: 'success', summary: 'Success', detail: 'Member deleted successfully' }];
      },
      error: err => console.error('Error deleting member:', err),
    });
  }
}
