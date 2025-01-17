package at.qe.skeleton.rest.frontend.mappersAndFrontendServices;

import at.qe.skeleton.model.Groupx;
import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.Project;
import at.qe.skeleton.rest.frontend.dtos.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GroupxProjectMapper {
    private final UserMapper userMapper;

    public GroupxProjectMapper(UserMapper userMapper) {
        this.userMapper = userMapper;

    }

    public GroupxProjectDto mapToGroupxProjectDto(GroupxProject groupxProject) {
        Project project = groupxProject.getProject();
        Groupx group = groupxProject.getGroup();
        SimpleGroupDto simpleGroupDto = new SimpleGroupDto(group.getId().toString(), group.isActive(), group.getName(), group.getDescription(), group.getGroupLead().getUsername());
        SimpleProjectDto simpleProjectDto = new SimpleProjectDto(project.getId().toString(), project.isActive(), project.getName(), project.getDescription(), project.getManager().getUsername());
        SimpleUserDto managerDetails = userMapper.getSimpleUser(project.getManager());
        List<SimpleUserDto> contributors = groupxProject.getContributors().stream().map(userMapper::getSimpleUser).toList();
        return new GroupxProjectDto(
                simpleGroupDto,
                simpleProjectDto,
                managerDetails,
                contributors,
                groupxProject.isActive()
        );
    }

  public SimpleProjectDto mapToSimpleProjectDto(GroupxProject groupxProject) {
    return new SimpleProjectDto(
        groupxProject.getProject().getId().toString(),
        groupxProject.getProject().isActive(),
        groupxProject.getProject().getName(),
        groupxProject.getProject().getDescription(),
        groupxProject.getProject().getManager().getUsername());
  }

  public SimpleGroupxProjectDto mapToDto(GroupxProject entity) {
        if (entity == null) {
            return null;
        }
        return new SimpleGroupxProjectDto(
                entity.getGroup().getId().toString(),
                entity.getGroup().getName(),
                entity.getProject().getId().toString(),
                entity.getProject().getName());
    }
}
