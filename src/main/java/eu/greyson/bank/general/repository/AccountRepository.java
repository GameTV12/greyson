package eu.greyson.bank.general.repository;

import eu.greyson.bank.general.model.Account;
import eu.greyson.bank.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends BaseRepository<Account, Long> {
}