package eu.greyson.bank.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Dto for application errors")
public class ExceptionDto {

    @NotNull
    @Schema(description = "HTTP status value")
    private Integer status;

    @NotBlank
    @Schema(description = "Message from exception")
    private String exceptionMessage;

    @Schema(description = "Exception stack trace", required = false)
    private String stackTrace;

    @Schema(description = "Validation errors", required = false)
    private List<ValidationErrorDto> validationErrors;

    public ExceptionDto(@NotNull Integer status, @NotBlank String exceptionMessage, String stackTrace) {
        this.status = status;
        this.exceptionMessage = exceptionMessage;
        this.stackTrace = stackTrace;
    }

    public ExceptionDto(@NotNull Integer status, @NotBlank String exceptionMessage, List<ValidationErrorDto> validationErrors) {
        this.status = status;
        this.exceptionMessage = exceptionMessage;
        this.validationErrors = validationErrors;
    }
}
