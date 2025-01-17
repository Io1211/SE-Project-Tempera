package at.qe.skeleton.repositories;

import at.qe.skeleton.model.GroupxProject;
import at.qe.skeleton.model.GroupxProjectId;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.dtos.GroupxProjectStateTimeDbDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleUserDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GroupxProjectRepository extends AbstractRepository<GroupxProject, GroupxProjectId> {



    public List<GroupxProject> findAllByProjectId(Long projectId);

    public Optional<GroupxProject> findByGroup_IdAndProject_Id(Long groupId, Long projectId);
    @EntityGraph(attributePaths = {"group", "project"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT gxp From GroupxProject gxp where gxp.group.id = :groupId AND gxp.project.id = :projectId")
    public Optional<GroupxProject> findByGroup_IdAndProject_IdFetchGroupAndProjectEagerly(Long groupId, Long projectId);

    @EntityGraph(attributePaths = {"contributors"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT gxp From GroupxProject gxp where gxp.group.id = :groupId AND gxp.project.id = :projectId")
    public Optional<GroupxProject> findByGroup_IdAndProject_IdFetchContributorsEagerly(Long groupId, Long projectId);

    @Query("SELECT gxp From GroupxProject gxp where gxp.project.id = :projectId AND :contributor member of gxp.contributors")
    public List<GroupxProject> findAllByProjectIdAndContributorsContaining(@Param("projectId")Long projectId, @Param("contributor")Userx contributor);

    @EntityGraph(attributePaths = {"group", "contributors", "contributors.groupxProjects"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT gxp From GroupxProject gxp JOIN gxp.group g JOIN gxp.contributors c WHERE g.id = :groupId AND c.username = :username ")
    public List<GroupxProject> findAllByGroup_IdAndContributorsContain(@Param("groupId") Long groupId,@Param("username") String username);

    public List<GroupxProject> findAllByContributorsContains(Userx user);

    public List<GroupxProject> findAllByGroup_Id(Long groupId);

    @Query("SELECT new at.qe.skeleton.rest.frontend.dtos.SimpleUserDto(u.username, u.firstName, u.lastName, u.email) From GroupxProject gxp JOIN gxp.contributors u where gxp.project.id = :projectId")
    public List<SimpleUserDto> findAllContributorsByProject_Id(Long projectId);

    @Query("SELECT new at.qe.skeleton.model.dtos.GroupxProjectStateTimeDbDto(gxp.project.id, gxp.group.id, g.active, pr.isActive, er.state, ir.start, ir.end) From GroupxProject gxp JOIN gxp.internalRecords ir JOIN ir.externalRecord er Join gxp.project pr JOIN gxp.group g where pr.manager.username = :manager")
    public List<GroupxProjectStateTimeDbDto> getAllgxpStateTimeDtosByManager(String manager);

    @Query("SELECT new at.qe.skeleton.model.dtos.GroupxProjectStateTimeDbDto(gxp.project.id, gxp.group.id, g.active, p.isActive, er.state, ir.start, ir.end) From GroupxProject gxp JOIN gxp.internalRecords ir JOIN ir.externalRecord er JOIN gxp.group g JOIN gxp.project p where g.groupLead.username = :groupLead")
    public List<GroupxProjectStateTimeDbDto> getAllgxpStateTimeDtosByGroupLead(String groupLead);

    @Query("SELECT new at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto(CAST(g.id AS string), g.name, CAST(p.id AS string), p.name) FROM GroupxProject gxp JOIN gxp.group g JOIN gxp.project p  JOin gxp.contributors con where con.username =  :username")
    public Set<SimpleGroupxProjectDto> getSimpleGroupxProjectDtoByUser(String username);

    /**
     * Loads a GroupxProject by its group and project id while fetchin group and project details.
     */
    @EntityGraph(value = "GroupxProject.detail", type= EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT gxp FROM GroupxProject gxp WHERE gxp.group.id = :groupId AND gxp.project.id = :projectId")
    public Optional<GroupxProject> findByGroupAndProjectDetailedGP(@Param(value = "groupId") Long groupId, @Param(value = "projectId") Long projectId);

    @EntityGraph(attributePaths = {"contributors"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT gxp FROM GroupxProject gxp WHERE gxp.group.id = :groupId AND gxp.project.id = :projectId")
    public Optional<GroupxProject> findByGroupAndProjectDetailedC(@Param(value = "groupId") Long groupId, @Param(value = "projectId") Long projectId);

}
