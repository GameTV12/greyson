package eu.greyson.bank.general.repository;

import eu.greyson.bank.general.model.User;
import eu.greyson.bank.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
}
