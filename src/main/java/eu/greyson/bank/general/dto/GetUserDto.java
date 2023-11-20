package eu.greyson.bank.general.dto;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.shared.json.DtoView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@JsonView(DtoView.Detail.class)
public class GetUserDto {
    @NotNull
    @Schema(description = "User ID", example = "1")
    Long id;

    @NotNull
    @Schema(description = "Fist name of user", example = "John")
    String firstName;

    @NotNull
    @Schema(description = "Last (family) name of user", example = "Doe")
    String lastName;

    @NotNull
    @Schema(description = "Date of user's birth", example = "Mon Nov 20 12:00:00 EST 2023")
    Date birthDate;
}
