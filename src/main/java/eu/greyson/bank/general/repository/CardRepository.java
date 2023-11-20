package eu.greyson.bank.general.repository;

import eu.greyson.bank.general.model.Card;
import eu.greyson.bank.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends BaseRepository<Card, Long> {
}
