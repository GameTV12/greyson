package eu.greyson.bank.general.dto;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.shared.json.DtoView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonView(DtoView.Detail.class)
public class PutCardBlockDto {
    @NotNull
    @Schema(description = "Blocked status", example = "true")
    Boolean blocked;
}
