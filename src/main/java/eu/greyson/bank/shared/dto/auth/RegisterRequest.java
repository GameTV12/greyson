package eu.greyson.bank.shared.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequest {

    @NotBlank
    @Schema(description = "User first name", example = "John")
    private String firstName;

    @NotBlank
    @Schema(description = "User last name", example = "Doe")
    private String lastName;

    @NotBlank
    @Email
    @Schema(description = "User email", example = "username@mail.cz")
    private String email;

    @NotBlank
    @Schema(description = "User password", example = "123456a")
    private String password;

    @NotNull
    @Schema(description = "Date of user's birth", example = "Mon Nov 20 12:00:00 EST 2023")
    private Date birthDate;
}
