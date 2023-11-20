package eu.greyson.bank.shared.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountExistsDto {
    @NotNull
    @Schema(description = "Existing of account", example = "true")
    private boolean exist;
}
