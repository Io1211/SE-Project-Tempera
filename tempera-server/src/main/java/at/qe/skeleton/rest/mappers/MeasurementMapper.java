package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.MeasurementService;
import org.springframework.stereotype.Service;

/**
 * Mapper for {@link Measurement} entities.
 */
@Service
public class MeasurementMapper implements DTOMapper<Measurement, MeasurementDto> {
  private final MeasurementService measurementService;
  private final SensorService sensorService;

  public MeasurementMapper(
      MeasurementService measurementService,
      SensorService sensorService) {
    this.measurementService = measurementService;
    this.sensorService = sensorService;
  }

  @Override
  public MeasurementDto mapToDto(Measurement entity) {
    if (entity == null) {
      return null;
    }
    if (entity.getId() == null) {
      throw new IllegalArgumentException("Measurement entity must have an id.");
    }
    if (entity.getTimestamp() == null) {
      throw new IllegalArgumentException("Measurement entity must have a timestamp.");
    }
    Sensor sensor = entity.getSensor();
    TemperaStation temperaStation = sensor.getTemperaStation();
    AccessPoint accesspoint =
    return new MeasurementDto(
        entity.getId(),
        sensor.getId().getSensorId(),
        temperaStation.getId(),
        entity.getSensor().getTemperaStation().getId(),
        entity.getValue(),
        entity.getSensor().getUnit(),
        entity.getTimestamp());
  }

  @Override
  public Measurement mapFromDto(MeasurementDto dto) throws CouldNotFindEntityException {
    if (dto == null) {
      throw new IllegalArgumentException("Measurement DTO must not be null.");
    }
    if ()
    Measurement measurement;
    SensorTemperaCompositeId sensorTemperaCompositeId= new SensorTemperaCompositeId();
    sensorTemperaCompositeId.setSensorId(dto.sensorId());
    sensorTemperaCompositeId.setTemperaStationId(dto.stationId());
    Sensor sensor = sensorService.findSensorById(sensorTemperaCompositeId);
    if (dto.id() != null) {
      measurement = measurementService.findMeasurementById(dto.id());
      measurement.setSensor(sensor);
      measurement.setTimestamp(dto.timestamp());
      measurement.setValue(dto.value());
      return measurement;
    }
    measurement = new Measurement(dto.value(), sensor);
    return measurement;
  }
}
