package at.qe.skeleton.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * seems like this is a Value Object (in DDD lingo)
 */

@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="measurement_value")
    private double value;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    @ManyToOne
    private Sensor sensor;

    /**
     * When Creating a Measurement, this Constructor should be used.
     * @param value the Value of the Measurement. Together with sensor.unit the meaning can be inferred.
     */
    public Measurement (double value) {
        this.value = value;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * JPA needs a non-arg Constructor
     */
    protected Measurement () {}

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
