import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {MessageModule} from "primeng/message";
import {NgIf} from "@angular/common";
import {PaginatorModule} from "primeng/paginator";
import {User} from "../../models/user.model";
import {UsersService} from "../../_services/users.service";
import { ExtendedProjectDto, ProjectControllerService, SimpleProjectDto } from '../../../api';
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-project-edit',
  standalone: true,
  imports: [
    ButtonModule,
    InputTextModule,
    MessageModule,
    NgIf,
    PaginatorModule,
    ReactiveFormsModule
  ],
  templateUrl: './project-edit.component.html',
  styleUrl: './project-edit.component.css'
})
export class ProjectEditComponent implements OnChanges, OnInit {

  projectForm: FormGroup;
  managers: any[] = [];
  extendedProject!: ExtendedProjectDto;
  @Input({required: true}) project!: SimpleProjectDto;
  @Output() editComplete = new EventEmitter<boolean>();

  constructor(
    private fb: FormBuilder,
    private projectService: ProjectControllerService,
    private usersService: UsersService,
    private messageService: MessageService) {
    this.projectForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      manager: [null]
    });
  }

  ngOnInit() {
    this.loadProjectDetails();

  }

  loadProjectDetails() {
    this.projectService.getProjectDetailedById(this.project?.projectId).subscribe({
      next: (data) => {
        this.extendedProject = data;
        this.fetchManagers();
      },
      error: (error) => {
        console.error('Failed to load project details:', error);
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Failed to load project details'});
      }
    });
  }
  ngOnChanges(changes: SimpleChanges): void {
    this.loadProjectDetails();
  }

  private populateForm() {
    if (this.project) {
      this.projectForm.patchValue({
        name: this.extendedProject.simpleProjectDto.name,
        description: this.extendedProject.simpleProjectDto.description,
        manager: this.managers.find(manager => manager.value === this.extendedProject.manager.username)
      });
      console.log('Populated form:', this.projectForm.value);
    }
  }

  onSubmit() {
    console.log('Submitting form:', this.project);
    if (this.projectForm.valid) {
      const dto: SimpleProjectDto = {
        projectId: this.extendedProject.simpleProjectDto.projectId,
        isActive: this.extendedProject.simpleProjectDto.isActive,
        name: this.projectForm.value.name,
        description: this.projectForm.value.description,
        manager: this.projectForm.value.manager.value
      }
      console.log('Dto', dto);
      this.projectService.updateProject(dto).subscribe({
        next: (response) => {
          console.log('Project updated successfully:', response);
          this.messageService.add({severity: 'success', summary: 'Success', detail: 'Project updated successfully'});
          this.editComplete.emit(true);
        },
        error: (error) => {
          console.error("Error updating project:", error);
          this.messageService.add({severity: 'error', summary: 'Error', detail: 'Error updating project'});
          this.editComplete.emit(false);
        }
      });
    } else {
      console.error('Invalid form');
    }
  }
  fetchManagers() {
    this.usersService.getAllManagers().subscribe({
      next: (users: User[]) => {
        this.managers = users.map(user => ({ label: `${user.firstName} ${user.lastName}`, value: user.username }));
        this.populateForm();
      },
      error: (error) => this.messageService.add({severity: 'error', summary: 'Error', detail: 'Failed to load managers'})
    });
  }
}
