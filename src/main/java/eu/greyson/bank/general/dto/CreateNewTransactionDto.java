package eu.greyson.bank.general.dto;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.shared.json.DtoView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonView(DtoView.Detail.class)
public class CreateNewTransactionDto {
    @NotNull
    @Schema(description = "Amount of new transaction", example = "1500")
    Double amount;

    @NotNull
    @Schema(description = "IBAN of recipient", example = "CZ123456")
    String debtor;
}
