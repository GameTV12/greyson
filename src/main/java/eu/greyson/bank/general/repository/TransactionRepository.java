package eu.greyson.bank.general.repository;

import eu.greyson.bank.general.model.Transaction;
import eu.greyson.bank.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction, Long> {
}
