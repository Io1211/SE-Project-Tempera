package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.exceptions.CouldNotFindEntityException;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.ClimateQuality;
import at.qe.skeleton.model.enums.State;
import at.qe.skeleton.model.enums.Visibility;
import at.qe.skeleton.rest.frontend.dtos.ColleagueStateDto;
import at.qe.skeleton.rest.frontend.dtos.FrontendMeasurementDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupxProjectDto;
import at.qe.skeleton.services.DashboardDataService;
import at.qe.skeleton.rest.frontend.payload.request.UpdateDashboardDataRequest;
import at.qe.skeleton.rest.frontend.payload.response.DashboardDataResponse;
import at.qe.skeleton.rest.frontend.payload.response.MessageResponse;
import at.qe.skeleton.services.UserxService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DashboardControllerTest {

  private DashboardController dashboardController;
  @Mock private DashboardDataService dashboardDataService;
  @Mock private UserxService userXService;

  @BeforeEach
  void setUp() {
    TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken("johndoe", null);
    SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    dashboardController = new DashboardController(dashboardDataService, userXService);
  }


  @AfterEach
  void tearDown() {}

  @Test
  @WithMockUser(username ="johndoe", authorities = "EMPLOYEE")
  void homeData() {
    String johnny = "johndoe";
    List<String> noGroups = new ArrayList<>();
    var colleagueStates =
        List.of(
            new ColleagueStateDto("Max Mustermann", "Raum 1", State.DEEPWORK, true, List.of("Gruppe 1")),
            new ColleagueStateDto("Jane Doe", "Raum 3", State.AVAILABLE, true, List.of("Gruppe1","Gruppe 2")),
            new ColleagueStateDto("Cooler Typ", "Raum 1", State.MEETING, false, noGroups));
    var projects =
        List.of(
            new SimpleGroupxProjectDto("1", "group1", "1", "project1"),
            new SimpleGroupxProjectDto("2", "group2", "2", "project2"),
            new SimpleGroupxProjectDto("3", "group3", "3", "project3"));
    DashboardDataResponse dashboardDataResponse =
        new DashboardDataResponse(
            new FrontendMeasurementDto(20.0, ClimateQuality.GOOD),
            new FrontendMeasurementDto(50.0, ClimateQuality.GOOD),
            new FrontendMeasurementDto(1000.0, ClimateQuality.MEDIOCRE),
            new FrontendMeasurementDto(100.0, ClimateQuality.POOR),
            Visibility.HIDDEN,
            State.DEEPWORK,
            "10.05.2024T12:20:10",
            null,
            projects.get(0),
            projects,
            colleagueStates);

    when(dashboardDataService.mapUserToHomeDataResponse(johnny)).thenReturn(dashboardDataResponse);

    ResponseEntity<DashboardDataResponse> returnValue = dashboardController.getDashboardData(johnny);
    DashboardDataResponse response = returnValue.getBody();

    verify(dashboardDataService, times(1)).mapUserToHomeDataResponse(johnny);
    assertEquals(dashboardDataResponse, response);
  }


    @Test
  void updateDashboardData() throws CouldNotFindEntityException {
    Userx userx = new Userx();
    MessageResponse messageResponse = new MessageResponse("Dashboard data updated successfully!");
    when(userXService.loadUser("johndoe")).thenReturn(userx);
    when(dashboardDataService.updateUserVisibilityAndTimeStampProject(any(), any())).thenReturn(messageResponse);

    ResponseEntity<MessageResponse> response =
        dashboardController.updateDashboardData(
            new UpdateDashboardDataRequest(
                Visibility.HIDDEN,
                new SimpleGroupxProjectDto(
                    "-1", "Group1", "1", "Projekt1")));
    assertEquals(messageResponse, response.getBody());
    verify(userXService, times(1)).loadUser("johndoe");
    verify(dashboardDataService, times(1)).updateUserVisibilityAndTimeStampProject(any(), any());
  }

}
