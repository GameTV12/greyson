package eu.greyson.bank.general.rest.user;

import eu.greyson.bank.general.dto.GetUserDto;
import eu.greyson.bank.shared.dto.auth.AuthResponse;
import eu.greyson.bank.shared.dto.auth.LoginRequest;
import eu.greyson.bank.shared.dto.auth.RegisterRequest;
import eu.greyson.bank.shared.swagger.ApiResponses_200_401_404_500;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "User Controller")
public interface UserController {
    @Operation(
            summary = "Get user by id",
            description = "This endpoint returns information about user."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetUserDto> getUser(@NotNull HttpServletRequest request);

    @Operation(
            summary = "Information about new updated user",
            description = "This endpoint returns information about new updated user."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetUserDto> updateUser(@NotNull HttpServletRequest request, @NotNull RegisterRequest dto, @NotNull Long id);
}
