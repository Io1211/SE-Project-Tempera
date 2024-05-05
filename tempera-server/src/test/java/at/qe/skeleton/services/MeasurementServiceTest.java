package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.model.enums.SensorType;
import at.qe.skeleton.model.enums.Unit;
import at.qe.skeleton.repositories.MeasurementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@SpringBootTest
@WebAppConfiguration
class MeasurementServiceTest {
  @Autowired private MeasurementRepository measurementRepository;
  @Autowired private MeasurementService measurementService;
  @Autowired private SensorService sensorService;

  private Sensor getSensor() {
    SensorTemperaCompositeId sensorId = new SensorTemperaCompositeId();
    sensorId.setSensorId(-1L);
    sensorId.setTemperaStationId("tempera_station_1");
    return sensorService.findSensorById(sensorId);
  }

  @DirtiesContext
  @Test
  void loadMeasurement() throws CouldNotFindEntityException {
    // loads a measurement Entity from the database (testdata in data.sql)
    Measurement measurement = measurementService.loadMeasurement(-1L);

    assertEquals(-1L, measurement.getId(), "Measurement ID should be 1");
    assertEquals(-1L, measurement.getSensor().getId().getSensorId(), "Sensor Id should be 1");
    assertEquals(20.0, measurement.getValue(), "Measurement value should be 20.0");
    assertEquals(
        LocalDateTime.of(2016, 1, 1, 0, 0, 0),
        measurement.getTimestamp(),
        "Measurement timestamp should be 2016-01-01 00:00:00");
    assertEquals(
        "tempera_station_1",
        measurement.getSensor().getId().getTemperaStationId(),
        "Tempera Station Id should be tempera_station_1");
  }

  @DirtiesContext
  @Test
  // found this SQL annotation with the help of Copilot.
  // it is described in the documentation:
  // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/context/jdbc/Sql.html
  @Sql(
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      scripts = "classpath:beforeMeasurementTest.sql")
  void saveMeasurement() throws CouldNotFindEntityException {

    // is redundant but good display of different ways to manipulate the database for testing.
    measurementRepository.findAll().forEach(measurementRepository::delete);
    LocalDateTime timestamp = LocalDateTime.now();

    // this sensor should fit the sensor in the data.sql
    Sensor sensor = getSensor();

    Measurement measurement = new Measurement(50.0, timestamp, sensor);

    Measurement savedMeasurement = measurementService.saveMeasurement(measurement);
    assertEquals(measurement.getSensor(), savedMeasurement.getSensor(), "sensor should be equal");
    assertEquals(
        measurement.getTimestamp(), savedMeasurement.getTimestamp(), "timestamp should be equal");
    assertEquals(measurement.getValue(), savedMeasurement.getValue(), "value should be equal");

    assertEquals(
        1,
        measurementRepository.findAll().size(),
        "after saving 1 Measurement, the db should " + "hold exactly 1 measurement");
  }

  @Test
  @DirtiesContext
  void deleteMeasurement() {
    assertEquals(
        1,
        measurementRepository.findAll().size(),
        "before deleting, the db should hold exactly 1 measurement");
    Measurement measurement = measurementRepository.findAll().get(0);
    measurementService.deleteMeasurement(measurement);
    assertEquals(
        0,
        measurementRepository.findAll().size(),
        "after deleting, the db should hold exactly 0 measurements");
  }

  @Test
  @DirtiesContext
  void saveLoadDeleteMeasurement() throws CouldNotFindEntityException {
    measurementRepository.findAll().forEach(measurementRepository::delete);

    LocalDateTime timestamp = LocalDateTime.now();

    Sensor sensor = getSensor();

    Measurement measurement = new Measurement(50.0, timestamp, sensor);
    measurement = measurementService.saveMeasurement(measurement);
    assertEquals(
        1,
        measurementRepository.findAll().size(),
        "after saving 1 Measurement, the db should " + "hold exactly 1 measurement");

    Measurement loadedMeasurement = measurementService.loadMeasurement(measurement.getId());

    measurementService.deleteMeasurement(loadedMeasurement);
    assertEquals(
        0,
        measurementRepository.findAll().size(),
        "after deleting, the db should hold exactly 0 measurements");
  }
}