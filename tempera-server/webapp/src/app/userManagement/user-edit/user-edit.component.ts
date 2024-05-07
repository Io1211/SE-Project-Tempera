import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { UserManagementControllerService, UserxDto } from '../../../api';
import RolesEnum = UserxDto.RolesEnum;

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, NgForOf, DialogModule, InputTextModule, ButtonModule, MessageModule, NgIf],
})
export class UserEditComponent implements OnInit {
  userForm: FormGroup;
  username!: string;
  roles: RolesEnum[];
  @Input({ required: true }) user!: UserxDto;
  @Output() editCompleted = new EventEmitter<boolean>();


  constructor(private fb: FormBuilder, private usersService: UserManagementControllerService) {
    this.roles = ['ADMIN', 'EMPLOYEE', 'MANAGER', 'GROUPLEAD'];
    this.userForm = this.fb.group({
      username: '',
      password: ['', Validators.minLength(6)],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.email],
      enabled: '',
      roles: this.fb.group({
        ADMIN: false,
        EMPLOYEE: false,
        MANAGER: false,
        GROUPLEAD: false,
      }),
    });
  }

  ngOnInit() {
    this.username = this.user.username;
    this.usersService.getUser(this.username).subscribe({
      next: (data) => {
        this.user = data;
        this.populateForm();
      },
      error: (error) => {
        console.error('Failed to load user details:', error);
      },
    });
  }

  ngOnChanges(changes: SimpleChanges) {
    // Check if 'user' input has changed
    if (changes['user']?.currentValue) {
      this.username = this.user.username;
      this.populateForm();
    }
  }

  private populateForm() {
    if (this.user) {
      this.userForm.patchValue({
        username: this.user.username,
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        password: this.user.password,
        email: this.user.email,
        enabled: this.user.enabled,
      });
      const rolesControl = this.userForm.get('roles') as FormGroup;
      this.roles.forEach(role => {
        rolesControl.get(role)?.setValue(this.user.roles?.includes(role));
      });
    }
  }

  onSubmit() {
    this.userForm.value.roles = Object.keys(this.userForm.value.roles).filter((role) => this.userForm.value.roles[role]);
    console.log(this.userForm.value);
    this.usersService.updateUser(this.userForm.value).subscribe({
      next: (response) => {
        console.log('User updated successfully:', response);
        this.editCompleted.emit(true);
      },
      error: (error) => {
        console.error('Error updating user:', error);
        this.editCompleted.emit(false);
      },
    });
  }

}
