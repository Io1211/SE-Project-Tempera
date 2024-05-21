package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.rest.frontend.mappersAndFrontendServices.TimetableDataService;
import at.qe.skeleton.rest.frontend.payload.request.SplitTimeRecordRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDescriptionRequest;
import at.qe.skeleton.rest.frontend.payload.request.UpdateProjectRequest;
import at.qe.skeleton.rest.frontend.payload.response.GetTimetableDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.UserxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping(value = "/api/timetable", produces = "application/json")
public class TimetableController {
  private static final Logger timeTabeControllerLogger =
      LoggerFactory.getLogger(TimetableController.class);
  private final TimetableDataService timeTableDataService;
  private final UserxService userService;

  public TimetableController(TimetableDataService timeTableDataService, UserxService userService) {
    this.timeTableDataService = timeTableDataService;
    this.userService = userService;
  }

  @GetMapping("/data")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<GetTimetableDataResponse> getTimetableData() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Userx user = userService.loadUser(username);
    // for now we set the page and size in the controller itself, but can easily be sent by Frontend
    int page = 0;
    int size = 20;
    GetTimetableDataResponse response = timeTableDataService.getTimetableData(user, page, size);
    timeTabeControllerLogger.info(
        "created TimeTableDataResponse page %d with size %d".formatted(page, size));
    return ResponseEntity.ok(response);
  }

  @PostMapping("/update/project")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateProject(@RequestBody UpdateProjectRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    MessageResponse response;
    try {
      response = timeTableDataService.updateProject(username, request);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
    timeTabeControllerLogger.info(response.getMessage());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/update/description")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> updateDescription(
      @RequestBody UpdateDescriptionRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    MessageResponse response;
    try {
      response = timeTableDataService.updateProjectDescription(username, request);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
    timeTabeControllerLogger.info(response.getMessage());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/split/time-record")
  @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MODERATOR') or hasAuthority('ADMIN')")
  public ResponseEntity<MessageResponse> splitTimeRecord(
      @RequestBody SplitTimeRecordRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    MessageResponse response;
    try {
      response = timeTableDataService.splitTimeRecord(username, request);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
    timeTabeControllerLogger.info(response.getMessage());
    return ResponseEntity.ok(response);
  }
}
