package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.UserxRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UserxDto {
  @NotNull @NotEmpty private String username;
  @NotNull @NotEmpty private String firstName;
  @NotNull @NotEmpty private String lastName;
  @NotNull @NotEmpty private String email;
  @NotNull @NotEmpty private String password;
  private boolean enabled;
  /**
   * Use list because frontend has problems with Set
   */
  private List<UserxRole> roles;

  public void UserxDTO(
      String username,
      String firstName,
      String lastName,
      String email,
      boolean enabled,
      List<UserxRole> roles) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.enabled = enabled;
    this.roles = roles;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public List<UserxRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserxRole> roles) {
    this.roles = roles;
  }
}
