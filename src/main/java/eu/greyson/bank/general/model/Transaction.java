package eu.greyson.bank.general.model;

import eu.greyson.bank.shared.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "gr_transaction")
public class Transaction extends BaseEntity<Long> {
    @NotNull
    @DecimalMin("0.00")
    private Double amount;

    @NotBlank
    private String creditor;

    @NotBlank
    private String debtor;

    @NotNull
    private Date dateCreated = new Date();

    private Date dateExecuted;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
