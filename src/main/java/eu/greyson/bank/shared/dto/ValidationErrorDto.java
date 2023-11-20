package eu.greyson.bank.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
@AllArgsConstructor
public class ValidationErrorDto {

    @NotEmpty
    @Schema(description = "Name of field with error")
    private String field;

    @NotEmpty
    @Schema(description = "Field validation error")
    private String error;

    public static ValidationErrorDto from(FieldError fieldError) {
        return new ValidationErrorDto(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
