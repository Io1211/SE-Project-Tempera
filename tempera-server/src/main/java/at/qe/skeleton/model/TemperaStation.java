package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class TemperaStation implements Persistable<String> {

    // wir wählen UUID, weil sie nicht nur innerhalb eines DBS sondern weltweit einmalig sind.
    // Daher ist eine eindeutige identifikation Problemlos möglich.
    @Id
    private String id;
    @OneToOne
    private Userx user;
    private boolean enabled;


    // We need to implement Persistable since we set Id manually
    // the following strategy for the isNew Method comes from spring documentation:
    // https://docs.spring.io/spring-data/jpa/reference/jpa/entity-persistence.html
    @Transient
    private boolean isNew = true;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public TemperaStation (String id) {
        this.id = id;
    }
    protected TemperaStation(){};

    public void setUser (Userx user) {
        this.user = user;
    }

    public Userx getUser() {
        return user;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperaStation that = (TemperaStation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.id;
    }
}
