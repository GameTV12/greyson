package eu.greyson.bank.general.dto;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.shared.json.DtoView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@JsonView(DtoView.Detail.class)
public class GetTransactionDto {
    @NotNull
    @Schema(description = "Transaction ID", example = "1")
    Long id;

    @NotNull
    @Schema(description = "Transaction amount", example = "25000")
    Double amount;

    @NotNull
    @Schema(description = "IBAN of sender", example = "CZ123456")
    String creditor;

    @NotNull
    @Schema(description = "IBAN of recipient", example = "CZ123456")
    String debtor;

    @NotNull
    @Schema(description = "Date of creating", example = "Mon Nov 20 14:00:00 EST 2023")
    Date dateCreated;

    @NotNull
    @Schema(description = "Date of execution", example = "Mon Nov 20 14:30:00 EST 2023")
    Date dateExecuted;
}
