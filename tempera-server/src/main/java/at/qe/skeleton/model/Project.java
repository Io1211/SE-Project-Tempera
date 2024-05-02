package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    //bidirectional one-to-many association:
    @OneToMany(mappedBy = "assignedProject")
    private List<SubordinateTimeRecord> subordinateTimeRecords;
    @ManyToOne
    private Userx manager;
    @ManyToMany
    private List<Userx> contributors;
    @ManyToMany
    private List<Group> groups;

    public Project(String name, String description, Userx manager) {
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.contributors = new ArrayList<>();
    }
    public Project() {}



    public List<SubordinateTimeRecord> getSubordinateTimeRecords() {
        return subordinateTimeRecords;
    }

    public Userx getManager() {
        return manager;
    }

    public List<Userx> getContributors() {
        return contributors;
    }

    public List<Group> getGroups() { return groups; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManager(Userx manager) {
        if(manager == null) {
            throw new NullPointerException("User should not be null when set as ProjectManager");
        }
        this.manager = manager;
    }

    public void addGroup(Group contributor) {
        if(contributor == null){
            throw new NullPointerException("Contributor should not be null when added to Project");
        }
        this.groups.add(contributor);
    }

    public void removeGroup(Group contributor) {
        if(contributor == null){
            throw new NullPointerException("Contributor should not be null when removed from Project");
        }
        this.groups.remove(contributor);
    }

    public void addContributor(Userx contributor) {
        if(contributor == null){
            throw new NullPointerException("Contributor should not be null when added to Project");
        }
        this.contributors.add(contributor);
    }

    public void removeContributor(Userx contributor) {
        if(contributor == null){
            throw new NullPointerException("Contributor should not be null when removed from Project");
        }
        this.contributors.remove(contributor);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() { return id; }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if (!(obj instanceof Project other)) {
            return false;
        }
        return other.name.equals(this.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
