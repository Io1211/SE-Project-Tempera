package at.qe.skeleton.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

/** seems like this is a Value Object (in DDD lingo) */

// todo: Hier noch die embeddedId einbauen!

@Entity
public class Measurement {
  @EmbeddedId
  MeasurementId id;

  @Column(name = "measurement_value")
  private double value;

  @ManyToOne(optional = false)
    @MapsId("sensorId")
  private Sensor sensor;

  protected Measurement() {}

  // todo: sanity checks for the value of the measurement?? temperature can't be 1000 degrees

  /**
   * Constructor for Measurement. All Parameters must not be null. Id is automatically generated by
   * the database once the object is persisted.
   *
   * @param value
   * @param timestamp
   * @param sensor
   */
  public Measurement(double value, @NotNull LocalDateTime timestamp, @NotNull Sensor sensor) {
    this.value = value;
    this.id = new MeasurementId();
    id.setSensorId(sensor.getId());
    id.setTimestamp(Objects.requireNonNull(timestamp, "timestamp must not be null"));
    this.sensor = Objects.requireNonNull(sensor, "sensor must not be null");
  }

  public void setId(MeasurementId id){
    this.id = id ;
  }

  public void setValue(double value) {
    this.value = value;
  }


  /**
   * Sets the sensor of the measurement. To add a measurement to a sensor, use the addMeasurement in the sensor.
   * @param sensor
   */
  public void setSensor(Sensor sensor) {
    this.sensor = sensor;
  }
  public Sensor getSensor() {
    return sensor;
  }

  public MeasurementId getId() {
    return id;
  }

  public double getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Measurement that = (Measurement) o;
    return Objects.equals(id, that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Measurement{id: %s,sensor: %s,value: %s}\n".formatted(id, sensor, value);
  }
}
