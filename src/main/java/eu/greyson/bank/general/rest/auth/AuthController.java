package eu.greyson.bank.general.rest.auth;

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

@Tag(name = "Auth", description = "Auth Controller")
public interface AuthController {
    @Operation(
            summary = "Login",
            description = "This endpoint is used for login to the accounts."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<AuthResponse> authenticate(@NotNull LoginRequest dto);

    @Operation(
            summary = "Refresh token",
            description = "This endpoint is used for login to the accounts."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<AuthResponse> refreshToken(@NotNull HttpServletRequest request);

    @Operation(
            summary = "Create a new user",
            description = "This endpoint returns information about new created user."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetUserDto> createUser(@NotNull RegisterRequest dto);
}
