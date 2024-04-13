package at.qe.skeleton.rest.mappers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Measurement;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.rest.dtos.MeasurementDto;
import at.qe.skeleton.services.SensorService;
import at.qe.skeleton.services.TemperaStationService;
import at.qe.skeleton.services.MeasurementService;

public class MeasurementMapper implements DTOMapper<Measurement, MeasurementDto> {
  private final MeasurementService measurementService;
  private final TemperaStationService temperaStationService;
  private final SensorService sensorService;

  public MeasurementMapper(
      MeasurementService measurementService,
      TemperaStationService temperaStationService,
      SensorService sensorService) {
    this.measurementService = measurementService;
    this.temperaStationService = temperaStationService;
    this.sensorService = sensorService;
  }

  @Override
  public MeasurementDto mapToDto(Measurement entity) {
    if (entity == null) {
      return null;
    }
    if (entity.getId() != null) {
      throw new IllegalArgumentException("Measurement entity must have an id.");
    }
    if (entity.getTimestamp() == null) {
      throw new IllegalArgumentException("Measurement entity must have a timestamp.");
    }
    return new MeasurementDto(
        entity.getId(),
        entity.getSensor().getId(),
        entity.getSensor().getTemperaStation().getId(),
        entity.getValue(),
        entity.getSensor().getUnit(),
        entity.getTimestamp());
  }

  @Override
  public Measurement mapFromDto(MeasurementDto dto) throws CouldNotFindEntityException {
    if (dto == null) {
      return null;
    }
    Measurement measurement;
    Sensor sensor = sensorService.findSensorById(dto.id());
    if (dto.id() != null) {
      measurement = measurementService.findMeasurementById(dto.id());
      measurement.setSensor(sensor);
      measurement.setTimestamp(dto.timestamp());
      measurement.setValue(dto.value());
    }
    measurement = new Measurement(dto.value(), sensor);
    return measurement;
  }
}
