package eu.greyson.bank.general.model;

import eu.greyson.bank.general.enums.CurrencyType;
import eu.greyson.bank.shared.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "gr_account")
public class Account extends BaseEntity<Long> {
    @NotBlank
    @Column(unique = true)
    private String IBAN;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Boolean activeStatus = true;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards;

    public Double getBalance() {
        Double answer = 0.00;
        if (transactions != null) {
            for (Transaction transaction :
                    transactions) {
                if (Objects.equals(transaction.getCreditor(), IBAN)){
                    answer -= transaction.getAmount();
                }
                else answer += transaction.getAmount();
            }
        }
        return answer;
    }
}
