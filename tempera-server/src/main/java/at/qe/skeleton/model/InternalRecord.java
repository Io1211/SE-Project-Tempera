package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Basic unit to measure time serverside. We want to preserve the original timerecords measured by
 * the TemperaStation and at the same time allow Users to assign these subordinate timerecords to
 * projects and divide them in smaller subunits. As soon as a ExternalRecord has been initiated, a
 * Subordinate TimeRecord with exactly the same characteristics as the ExternalRecord is
 * initialized. The End will be set to null and updated as soon as the new ExternalRecord is
 * received from TemperaStation. While End equals null, InternalRecord may not be further divided,
 * meaning start and end stay as they are.
 *
 * <p>A InternalRecord stores the Project and Group it is assigned to. But it does not have to be
 * assigned to a Project or a Group. Once a Group or Project gets deleted, all the TR that were
 * assigned to that Group or Project reference null as assigned Group/Project.
 */
@Entity
@IdClass(InternalRecordId.class)
public class InternalRecord {
  //  @Id
  //  @GeneratedValue(strategy = GenerationType.AUTO)
  //  private Long id;

  @Id
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime start;

  @Id
  @ManyToOne(optional = false)
  @JoinColumns({
    @JoinColumn(name = "userName", referencedColumnName = "user_username"),
    @JoinColumn(name = "ext_rec_start", referencedColumnName = "start")
  })
  private ExternalRecord externalRecord;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_end")
  private LocalDateTime end;

  // bidirectional one-to-many association
  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project assignedProject;

  @ManyToOne
  @JoinColumn(name = "groupx_id")
  private Group assignedGroup;

  // these are the foreign keys from externalRecord. The ext_rec_start will often be the same
  // as the start of the InternalRecord but once the user starts to divide the ExternalRecord
  // they can certainly differ from each other.

  public LocalDateTime getStart() {
    return start;
  }

  public LocalDateTime getEnd() {
    return end;
  }

  public InternalRecord(LocalDateTime start) {
    this.start = start;
    this.end = null;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public ExternalRecord getExternalRecord() {
    return externalRecord;
  }

  public void setExternalRecord(ExternalRecord externalRecord) {
    this.externalRecord = externalRecord;
  }

  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  protected InternalRecord() {}
  ;

  public Project getAssignedProject() {
    return assignedProject;
  }

  public void setAssignedProject(Project assignedProject) {
    this.assignedProject = assignedProject;
  }

  public Group getAssignedGroup() {
    return assignedGroup;
  }

  public void setAssignedGroup(Group assignedGroup) {
    this.assignedGroup = assignedGroup;
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, externalRecord);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (!(o instanceof InternalRecord other)) {
      return false;
    }
    return other.start.equals(this.start) && other.externalRecord.equals(this.externalRecord);
  }

  @Override
  public String toString() {
    return "[InternalRecord start: %s, end: %s, project: %s]"
        .formatted(
            start.toString(),
            end == null ? "null" : end,
            assignedProject == null ? "null" : assignedProject);
  }
}