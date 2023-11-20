package eu.greyson.bank.general.model;

import eu.greyson.bank.shared.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "gr_card")
public class Card extends BaseEntity<Long> {
    @NotBlank
    private String name;

    @NotNull
    private Boolean blocked = false;

    private Date dateLocked = null;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
