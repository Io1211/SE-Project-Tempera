package at.qe.skeleton.rest.frontend.controllers;

import at.qe.skeleton.rest.frontend.dtos.CredentialsDto;
import at.qe.skeleton.rest.frontend.dtos.UserxDto;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.AuthenticationService;
import at.qe.skeleton.services.UserxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequestMapping("/api/users")
public class UserManagementController {

  private final UserxService userxService;
  private final AuthenticationService authenticationService;

  public UserManagementController(
      UserxService userxService, AuthenticationService authenticationService) {
    this.userxService = userxService;
    this.authenticationService = authenticationService;
  }

  @GetMapping("/all")
  public ResponseEntity<List<UserxDto>> getAllUsers() {
    List<UserxDto> users =
        userxService.getAllUsers().stream().map(userxService::convertToDTO).toList();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/load/{id}")
  public ResponseEntity<UserxDto> getUser(@PathVariable String id) {
    UserxDto user = userxService.loadUserDTOById(id);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
    userxService.deleteUser(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", "User deleted");
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update")
  public ResponseEntity<UserxDto> updateUser(@RequestBody UserxDto userxDto) {
    Userx updatedUser = userxService.updateUser(userxDto);
    return ResponseEntity.ok(userxService.convertToDTO(updatedUser));
  }

  @PostMapping("/create")
  public ResponseEntity<UserxDto> createUser(@RequestBody UserxDto userxDto) {
    UserxDto createdUser = authenticationService.registerUser(userxDto);
    return ResponseEntity.ok(createdUser);
  }

  @PostMapping("/validate")
  public ResponseEntity<UserxDto> validateUser(@RequestBody CredentialsDto credentials) {
    UserxDto isValidUser = userxService.validateUser(credentials.username(), credentials.password());
    return ResponseEntity.ok(isValidUser);
  }

  @PostMapping("/enable")
  public ResponseEntity<Map<String, String>> enableUser(
      @RequestBody CredentialsDto credentials) {
    userxService.enableUser(credentials.username(), credentials.password());
    return ResponseEntity.ok(Map.of("message", "User enabled"));
  }

  @GetMapping("/managers")
    public ResponseEntity<List<UserxDto>> getManagers() {
        List<UserxDto> managers = userxService.getManagers().stream().map(userxService::convertToDTO).toList();
        return ResponseEntity.ok(managers);
    }

}
