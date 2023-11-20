package eu.greyson.bank.general.service;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.dto.CreateCardDto;
import eu.greyson.bank.general.dto.GetCardDto;
import eu.greyson.bank.general.dto.PutCardBlockDto;
import eu.greyson.bank.general.model.Account;
import eu.greyson.bank.general.model.Card;
import eu.greyson.bank.general.repository.AccountRepository;
import eu.greyson.bank.general.repository.CardRepository;
import eu.greyson.bank.shared.exception.BadRequestException;
import eu.greyson.bank.shared.exception.ResourceNotFoundException;
import eu.greyson.bank.shared.service.BaseService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService extends BaseService<Card, Long, CardRepository> implements InitializingBean {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper mapper;
    private final JwtService jwtService;


    @Override
    protected CardRepository getDefaultRepository() {
        return cardRepository;
    }

    @Override
    public void afterPropertiesSet() {
        mapper.addMappings(new CardDtoMapper());
    }

    private static class CardDtoMapper extends PropertyMap<Card, GetCardDto> {
        @Override
        protected void configure() {
            map().setName(source.getName());
            map().setBlocked(source.getBlocked());
            map().setDateLocked(source.getDateLocked());
        }
    }

    public GetCardDto createDto(Card card) {
        return mapper.map(card, GetCardDto.class);
    }

    public List<GetCardDto> getCards(Long accountId) {
        return cardRepository.findAll().stream().
                filter(card -> Objects.equals(card.getAccount().getId(), accountId)).
                map(this::createDto).collect(Collectors.toList());
    }

    public GetCardDto getCardById(Long accountId, Long cardId) {
        if (!Objects.equals(cardRepository.findUnsafe(cardId).getAccount().getId(), accountId)) throw new ResourceNotFoundException("This account not allowed");
        return createDto(getUnsafe(cardId));
    }

    public GetCardDto changeBlockStatus(HttpServletRequest request, Long accountId, Long cardId, PutCardBlockDto dto) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new IOException("Not user");
        String token = authHeader.substring(7);
        if (
            !Objects.equals(jwtService.extractUserId(token), accountRepository.findUnsafe(accountId).getUser().getId()+"") ||
            !Objects.equals(jwtService.extractUserId(token), cardRepository.findUnsafe(cardId).getAccount().getUser().getId()+"")
        ) throw new BadRequestException("User isn't allowed");

        if (!Objects.equals(cardRepository.findUnsafe(cardId).getAccount().getId(), accountId)) throw new ResourceNotFoundException("This account not allowed");
        Card card = this.getUnsafe(cardId);
        card.setBlocked(dto.getBlocked());
        if (dto.getBlocked()) {
            card.setDateLocked(new Date());
        }
        else {
            card.setDateLocked(null);
        }
        cardRepository.save(card);
        return createDto(card);
    }

    public GetCardDto createCard(HttpServletRequest request, CreateCardDto dto, Long accountId) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new IOException("Not user");
        String token = authHeader.substring(7);
        if (!Objects.equals(jwtService.extractUserId(token), accountRepository.findUnsafe(accountId).getUser().getId()+"")) throw new BadRequestException("User isn't allowed");


        Card newCard = new Card();
        newCard.setBlocked(true);
        newCard.setName(dto.getName());
        Account account = accountRepository.findUnsafe(accountId);
        newCard.setAccount(account);
        cardRepository.save(newCard);
        return createDto(newCard);
    }
}
