import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable, throwError} from 'rxjs';
import {Project} from "../models/project.model";
import {Group} from "../models/group.model";
import {ContributorAssignmentDTO, GroupAssignmentDTO, ProjectCreateDTO, ProjectUpdateDTO} from "../models/projectDtos";


@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private API_URL = 'http://localhost:8080/api/project/';

  constructor(private http: HttpClient) { }

  getAllProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(this.API_URL + 'all');
  }

  createProject(dto: ProjectCreateDTO): Observable<Project> {
    return this.http.post<Project>(`${this.API_URL}create`, dto);
  }

  updateProject(dto: ProjectUpdateDTO): Observable<Project> {
    return this.http.put<Project>(`${this.API_URL}update`, dto);
  }

  deleteProject(projectId: number): Observable<string> {
    return this.http.delete(`${this.API_URL}delete/${projectId}`, { responseType: 'text' })
      .pipe(
        catchError(error => throwError(() => new Error('Error deleting project: ' + error.message)))
      );
  }

  getProjectById(projectId: number): Observable<Project> {
    return this.http.get<Project>(`${this.API_URL}load/${projectId}`);
  }

  addGroupToProject(dto: GroupAssignmentDTO): Observable<void> {
    return this.http.post<void>(`${this.API_URL}addGroup`, dto);
  }

  getGroups(projectId: string): Observable<Group[]> {
    return this.http.get<Group[]>(`${this.API_URL}getGroups/${projectId}`);
  }

  deleteGroupFromProject(dto: GroupAssignmentDTO): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}deleteGroup/${dto.projectId}/${dto.groupId}`);
  }

  addMemberToProject(dto: ContributorAssignmentDTO): Observable<void> {
    return this.http.post<void>(`${this.API_URL}addContributor`, dto);
  }

  removeMemberFromProject(dto: ContributorAssignmentDTO): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}deleteContributor/${dto.projectId}/${dto.contributorId}`);
  }

  getProjectsOfGroup(groupId: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.API_URL}allOfGroup/${groupId}`);
  }
}
