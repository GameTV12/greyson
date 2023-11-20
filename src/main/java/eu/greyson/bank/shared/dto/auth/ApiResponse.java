package eu.greyson.bank.shared.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    @Schema(description = "Success flag", example = "true")
    private boolean success;

    @Schema(description = "Result message")
    private String message;
}
