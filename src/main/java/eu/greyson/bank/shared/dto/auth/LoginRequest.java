package eu.greyson.bank.shared.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Email
    @Schema(description = "User email", example = "username@mail.cz")
    private String email;

    @NotBlank
    @Schema(description = "User password", example = "123456a")
    private String password;

}
