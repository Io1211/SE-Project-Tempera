package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.exceptions.InternalRecordOutOfBoundsException;
import at.qe.skeleton.exceptions.ExternalRecordOutOfBoundsException;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.InternalRecordRepository;
import at.qe.skeleton.repositories.ExternalRecordRepository;
import at.qe.skeleton.repositories.UserxRepository;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@Scope("application")
public class TimeRecordService {

  private final Logger logger = Logger.getLogger("TimeRecordServiceLogger");
  private final InternalRecordRepository internalRecordRepository;
  private final ExternalRecordRepository externalRecordRepository;
  private final UserxRepository userxRepository;

  public TimeRecordService(
      ExternalRecordRepository externalRecordRepository,
      InternalRecordRepository internalRecordRepository,
      UserxRepository userxRepository) {
    this.externalRecordRepository = externalRecordRepository;
    this.internalRecordRepository = internalRecordRepository;
    this.userxRepository = userxRepository;
  }

  public ExternalRecord findSuperiorTimeRecordByUserAndStart(Userx user, LocalDateTime start)
      throws CouldNotFindEntityException {
    ExternalRecordId id = new ExternalRecordId(start, user.getUsername());
    return externalRecordRepository
        .findById(id)
        .orElseThrow(() -> new CouldNotFindEntityException("ExternalRecord %s".formatted(id)));
  }

  /**
   * this method saves a new ExternalRecord and adds the start-Time of the new TimeRecord minus
   * 1sec as the End-Time to the ExternalRecord entity with the latest start datetime before the
   * current one. Furthermore the method instantiates a InternalRecord with the identical
   * properties and adds this to the list of Subordinate TimeRecords stored in the
   * SuperiorTimeRecords. In case this is the first ExternalRecord that was saved for User of
   * the stored TemperaStation, the method just saves the new ExternalRecord and its
   * InternalRecord to the database.
   *
   * @param newExternalRecord
   * @return the ExternalRecord that was newly created.
   */
  @Transactional(
      rollbackFor = {
        ExternalRecordOutOfBoundsException.class,
        CouldNotFindEntityException.class
      })
  public ExternalRecord addRecord(ExternalRecord newExternalRecord)
      throws CouldNotFindEntityException, IOException {
    if (newExternalRecord.getStart() == null) {
      throw new NullPointerException(
          "ExternalRecord must have a Start Time when being added to db.");
    }
    Userx user = newExternalRecord.getUser();
    finalizeOldTimeRecord(user, newExternalRecord);
    return saveNewTimeRecord(newExternalRecord, user);
  }

  public ExternalRecord saveNewTimeRecord(ExternalRecord externalRecord, Userx user) {
    InternalRecord internalRecord =
        new InternalRecord(externalRecord.getStart());
    internalRecord = internalRecordRepository.save(internalRecord);
    logger.info("saved new %s".formatted(internalRecord.toString()));
    externalRecord.addSubordinateTimeRecord(internalRecord);
    logger.info("Added new %s to new %s".formatted(internalRecord, externalRecord));
    externalRecord = externalRecordRepository.save(externalRecord);
    logger.info("saved new %s".formatted(externalRecord.toString()));
    if (externalRecordRepository.findAllByUserAndEndIsNull(user).size() > 1) {
      throw new InternalRecordOutOfBoundsException(
          "There are more than one SuperiorTimeRecords with no End for user %s".formatted(user));
    }
    return externalRecord;
  }

  /**
   * internal Method, that retrieves the InternalRecord form the oldSuperiorTimeRecord and
   * sets the End for both of them to one second before the start of the newExternalRecord.
   * After that it saves both old TimeRecord entities to the database.
   */
  public void finalizeOldTimeRecord(Userx user, ExternalRecord newExternalRecord)
      throws IOException {
    Optional<ExternalRecord> oldExternalRecordOptional =
        findLatestExternalRecordByUser(user);
    if (oldExternalRecordOptional.isEmpty()) {
      logger.info("No old ExternalRecord found for user %s".formatted(user));
      return;
    }
    ExternalRecord oldExternalRecord = oldExternalRecordOptional.get();
    logger.info("found old ExternalRecord %s".formatted(oldExternalRecord));

    ExternalRecordId incomingId = newExternalRecord.getId();
    if (oldExternalRecord.getId().equals(incomingId)) {
      logger.info("incoming ExternalRecord is the same as the old one. No need to finalize");
      return;
    }

    // fetch the subordinate Timerecord of this old superior TimeRecord
    InternalRecord oldInternalRecord =
        oldExternalRecord.getInternalRecords().get(0);
    LocalDateTime oldEnd = newExternalRecord.getStart().minusSeconds(1);
    if (ChronoUnit.SECONDS.between(oldEnd, newExternalRecord.getStart()) != 1) {
      throw new RuntimeException(
          "End TimeStamp is not one second before the new TimeRecord starts.");
    }
    LocalDateTime oldStart = oldExternalRecord.getStart();
    long durationInSeconds = ChronoUnit.SECONDS.between(oldStart, oldEnd);
    if (durationInSeconds <= 0)
      throw new ExternalRecordOutOfBoundsException(
          "the new TimeRecord starts before the old one.");

    // time setting
    oldExternalRecord.setDuration(durationInSeconds);
    oldExternalRecord.setEnd(oldEnd);
    oldInternalRecord.setEnd(oldEnd);
    logger.info(
        "set End TimeStamp for %s and %s to %s"
            .formatted(oldExternalRecord, oldInternalRecord, oldEnd));

    // persisting the Entities
    internalRecordRepository.save(oldInternalRecord);
    logger.info("saved %s".formatted(oldInternalRecord.toString()));
    externalRecordRepository.save(oldExternalRecord);
    logger.info("saved %s".formatted(oldExternalRecord.toString()));
  }

  public void delete(ExternalRecord externalRecord) {
    externalRecordRepository.delete(externalRecord);
  }

  private InternalRecord saveSubordinateTimeRecord(
      InternalRecord internalRecord) {
    return this.internalRecordRepository.save(internalRecord);
  }

  public Optional<ExternalRecord> findLatestExternalRecordByUser(Userx user) {
    return externalRecordRepository.findFirstByUserAndEndIsNull(user);
  }

  public Optional<ExternalRecord> findExternalRecordByStartAndUser(
      LocalDateTime start, Userx user) {
    return externalRecordRepository.findByUserAndId_Start(user, start);
  }
}
