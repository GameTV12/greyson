package eu.greyson.bank.general.dto;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.general.enums.CurrencyType;
import eu.greyson.bank.shared.json.DtoView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonView(DtoView.Detail.class)
public class CreateAccountDto {
    @NotNull
    @Schema(description = "IBAN - International Bank Account Number", example = "CZ1111111111111111111111")
    String IBAN;

    @NotNull
    @Schema(description = "Name of the account", example = "Account for Czechia and CZK payments")
    String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Currency of account", example = "CZK")
    CurrencyType currency;
}
