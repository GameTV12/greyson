package eu.greyson.bank.general.dto;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.shared.json.DtoView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@JsonView(DtoView.Detail.class)
public class GetCardDto {
    @NotNull
    @Schema(description = "Card ID", example = "1")
    Long id;

    @NotNull
    @Schema(description = "Card name", example = "Travel card")
    String name;

    @NotNull
    @Schema(description = "Blocked status", example = "false")
    Boolean blocked = false;

    @Schema(description = "Date of block", example = "Mon Nov 20 18:00:00 EST 2023")
    Date dateLocked;
}
