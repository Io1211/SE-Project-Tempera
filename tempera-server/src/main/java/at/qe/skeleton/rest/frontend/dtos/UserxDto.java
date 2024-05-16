package at.qe.skeleton.rest.frontend.dtos;

import at.qe.skeleton.model.enums.UserxRole;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserxDto (
        @NotNull String username,
        String firstName,
        String lastName,
        String email,
        String password,
        boolean enabled,
        List<UserxRole> roles
) {}