export interface ProjectCreateDTO {
  projectId: 1;
  name: string;
  description: string;
  manager: string;
}

export interface ProjectUpdateDTO {
  projectId: number;
  name: string;
  description: string;
  manager: string;
}

export interface GroupAssignmentDTO {
  projectId: number;
  groupId: number;
}

export interface ContributorAssignmentDTO {
  projectId: number;
  contributorId: string;
}
