package eu.greyson.bank.general.dto;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.general.enums.CurrencyType;
import eu.greyson.bank.shared.json.DtoView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@JsonView(DtoView.Detail.class)
public class GetAccountDto {
    @NotNull
    @Schema(description = "Account ID", example = "5")
    Long id;

    @NotNull
    @Schema(description = "Account IBAN", example = "CZ123456")
    String IBAN;

    @NotNull
    @Schema(description = "Account name", example = "Account for EU")
    String name;

    @NotNull
    @Schema(description = "Account balance", example = "100")
    Double balance;

    @NotNull
    @Schema(description = "Currency type", example = "EUR")
    CurrencyType currency;
}
