package eu.greyson.bank.general.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.dto.CreateCardDto;
import eu.greyson.bank.general.dto.GetCardDto;
import eu.greyson.bank.general.dto.PutCardBlockDto;
import eu.greyson.bank.general.enums.CurrencyType;
import eu.greyson.bank.general.model.Account;
import eu.greyson.bank.general.model.Card;
import eu.greyson.bank.general.model.Transaction;
import eu.greyson.bank.general.model.User;
import eu.greyson.bank.general.repository.AccountRepository;
import eu.greyson.bank.general.repository.CardRepository;
import eu.greyson.bank.shared.exception.BadRequestException;
import eu.greyson.bank.shared.exception.ResourceNotFoundException;
import eu.greyson.bank.shared.model.BaseEntity;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CardService.class})
@ExtendWith(SpringExtension.class)
class CardServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ModelMapper modelMapper;

    /**
     * Method under test: {@link CardService#createDto(Card)}
     */
    @Test
    void testCreateDto() {
        GetCardDto getCardDto = new GetCardDto();
        getCardDto.setBlocked(true);
        getCardDto.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getCardDto.setId(1L);
        getCardDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any())).thenReturn(getCardDto);

        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        Account account = new Account();
        account.setActiveStatus(true);
        account.setCards(new ArrayList<>());
        account.setCurrency(CurrencyType.EUR);
        account.setIBAN("IBAN");
        account.setId(BaseEntity.serialVersionUID);
        account.setName("Name");
        account.setTransactions(new ArrayList<>());
        account.setUser(user);

        Card card = new Card();
        card.setAccount(account);
        card.setBlocked(true);
        card.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        card.setId(BaseEntity.serialVersionUID);
        card.setName("Name");
        GetCardDto actualCreateDtoResult = cardService.createDto(card);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any());
        assertSame(getCardDto, actualCreateDtoResult);
    }

    /**
     * Method under test: {@link CardService#createDto(Card)}
     */
    @Test
    void testCreateDto2() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        Account account = new Account();
        account.setActiveStatus(true);
        account.setCards(new ArrayList<>());
        account.setCurrency(CurrencyType.EUR);
        account.setIBAN("IBAN");
        account.setId(BaseEntity.serialVersionUID);
        account.setName("Name");
        account.setTransactions(new ArrayList<>());
        account.setUser(user);

        Card card = new Card();
        card.setAccount(account);
        card.setBlocked(true);
        card.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        card.setId(BaseEntity.serialVersionUID);
        card.setName("Name");
        assertThrows(ResourceNotFoundException.class, () -> cardService.createDto(card));
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any());
    }

    /**
     * Method under test: {@link CardService#getCards(Long)}
     */
    @Test
    void testGetCards() {
        when(cardRepository.findAll()).thenReturn(new ArrayList<>());
        List<GetCardDto> actualCards = cardService.getCards(1L);
        verify(cardRepository).findAll();
        assertTrue(actualCards.isEmpty());
    }

    /**
     * Method under test: {@link CardService#getCards(Long)}
     */
    @Test
    void testGetCards2() {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        Account account = new Account();
        account.setActiveStatus(true);
        account.setCards(new ArrayList<>());
        account.setCurrency(CurrencyType.EUR);
        account.setIBAN("IBAN");
        account.setId(BaseEntity.serialVersionUID);
        account.setName("Name");
        account.setTransactions(new ArrayList<>());
        account.setUser(user);

        Card card = new Card();
        card.setAccount(account);
        card.setBlocked(true);
        card.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        card.setId(BaseEntity.serialVersionUID);
        card.setName("Name");

        ArrayList<Card> cardList = new ArrayList<>();
        cardList.add(card);
        when(cardRepository.findAll()).thenReturn(cardList);

        GetCardDto getCardDto = new GetCardDto();
        getCardDto.setBlocked(true);
        getCardDto.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getCardDto.setId(1L);
        getCardDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any())).thenReturn(getCardDto);
        List<GetCardDto> actualCards = cardService.getCards(1L);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any());
        verify(cardRepository).findAll();
        assertEquals(1, actualCards.size());
    }

    /**
     * Method under test: {@link CardService#getCards(Long)}
     */
    @Test
    void testGetCards3() {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        Account account = new Account();
        account.setActiveStatus(true);
        account.setCards(new ArrayList<>());
        account.setCurrency(CurrencyType.EUR);
        account.setIBAN("IBAN");
        account.setId(BaseEntity.serialVersionUID);
        account.setName("Name");
        account.setTransactions(new ArrayList<>());
        account.setUser(user);

        Card card = new Card();
        card.setAccount(account);
        card.setBlocked(true);
        card.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        card.setId(BaseEntity.serialVersionUID);
        card.setName("Name");

        User user2 = new User();
        user2.setAccounts(new ArrayList<>());
        user2.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user2.setEmail("john.smith@example.org");
        user2.setFirstName("John");
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setPassword("Password");

        Account account2 = new Account();
        account2.setActiveStatus(false);
        account2.setCards(new ArrayList<>());
        account2.setCurrency(CurrencyType.USD);
        account2.setIBAN("eu.greyson.bank.general.model.Account");
        account2.setId(2L);
        account2.setName("eu.greyson.bank.general.model.Account");
        account2.setTransactions(new ArrayList<>());
        account2.setUser(user2);

        Card card2 = new Card();
        card2.setAccount(account2);
        card2.setBlocked(false);
        card2.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        card2.setId(2L);
        card2.setName("eu.greyson.bank.general.model.Card");

        ArrayList<Card> cardList = new ArrayList<>();
        cardList.add(card2);
        cardList.add(card);
        when(cardRepository.findAll()).thenReturn(cardList);

        GetCardDto getCardDto = new GetCardDto();
        getCardDto.setBlocked(true);
        getCardDto.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getCardDto.setId(1L);
        getCardDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any())).thenReturn(getCardDto);
        List<GetCardDto> actualCards = cardService.getCards(1L);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any());
        verify(cardRepository).findAll();
        assertEquals(1, actualCards.size());
    }

    /**
     * Method under test: {@link CardService#getCardById(Long, Long)}
     */
    @Test
    void testGetCardById() throws EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        Account account = new Account();
        account.setActiveStatus(true);
        account.setCards(new ArrayList<>());
        account.setCurrency(CurrencyType.EUR);
        account.setIBAN("IBAN");
        account.setId(BaseEntity.serialVersionUID);
        account.setName("Name");
        account.setTransactions(new ArrayList<>());
        account.setUser(user);

        Card card = new Card();
        card.setAccount(account);
        card.setBlocked(true);
        card.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        card.setId(BaseEntity.serialVersionUID);
        card.setName("Name");
        when(cardRepository.findUnsafe(Mockito.<Long>any())).thenReturn(card);

        GetCardDto getCardDto = new GetCardDto();
        getCardDto.setBlocked(true);
        getCardDto.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getCardDto.setId(1L);
        getCardDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any())).thenReturn(getCardDto);
        GetCardDto actualCardById = cardService.getCardById(1L, 1L);
        verify(cardRepository, atLeast(1)).findUnsafe(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any());
        assertSame(getCardDto, actualCardById);
    }

    /**
     * Method under test: {@link CardService#getCardById(Long, Long)}
     */
    @Test
    void testGetCardById2() throws EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        Account account = new Account();
        account.setActiveStatus(true);
        account.setCards(new ArrayList<>());
        account.setCurrency(CurrencyType.EUR);
        account.setIBAN("IBAN");
        account.setId(BaseEntity.serialVersionUID);
        account.setName("Name");
        account.setTransactions(new ArrayList<>());
        account.setUser(user);

        Card card = new Card();
        card.setAccount(account);
        card.setBlocked(true);
        card.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        card.setId(BaseEntity.serialVersionUID);
        card.setName("Name");
        when(cardRepository.findUnsafe(Mockito.<Long>any())).thenReturn(card);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> cardService.getCardById(1L, 1L));
        verify(cardRepository, atLeast(1)).findUnsafe(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetCardDto>>any());
    }

    /**
     * Method under test: {@link CardService#changeBlockStatus(HttpServletRequest, Long, Long, PutCardBlockDto)}
     */
    @Test
    void testChangeBlockStatus() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        PutCardBlockDto dto = new PutCardBlockDto();
        dto.setBlocked(true);
        assertThrows(IOException.class, () -> cardService.changeBlockStatus(request, 1L, 1L, dto));
    }

    /**
     * Method under test: {@link CardService#changeBlockStatus(HttpServletRequest, Long, Long, PutCardBlockDto)}
     */
    @Test
    void testChangeBlockStatus2() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");

        PutCardBlockDto dto = new PutCardBlockDto();
        dto.setBlocked(true);
        assertThrows(IOException.class, () -> cardService.changeBlockStatus(request, 1L, 1L, dto));
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CardService#changeBlockStatus(HttpServletRequest, Long, Long, PutCardBlockDto)}
     */
    @Test
    void testChangeBlockStatus3() throws EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        Account account = new Account();
        account.setActiveStatus(true);
        account.setCards(new ArrayList<>());
        account.setCurrency(CurrencyType.EUR);
        account.setIBAN("IBAN");
        account.setId(BaseEntity.serialVersionUID);
        account.setName("Name");
        account.setTransactions(new ArrayList<>());
        account.setUser(user);
        when(accountRepository.findUnsafe(Mockito.<Long>any())).thenReturn(account);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        PutCardBlockDto dto = new PutCardBlockDto();
        dto.setBlocked(true);
        assertThrows(BadRequestException.class, () -> cardService.changeBlockStatus(request, 1L, 1L, dto));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(accountRepository).findUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CardService#changeBlockStatus(HttpServletRequest, Long, Long, PutCardBlockDto)}
     */
    @Test
    void testChangeBlockStatus4() {
        when(jwtService.extractUserId(Mockito.<String>any())).thenThrow(new ResourceNotFoundException("An error occurred"));
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        PutCardBlockDto dto = new PutCardBlockDto();
        dto.setBlocked(true);
        assertThrows(ResourceNotFoundException.class, () -> cardService.changeBlockStatus(request, 1L, 1L, dto));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CardService#createCard(HttpServletRequest, CreateCardDto, Long)}
     */
    @Test
    void testCreateCard() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        CreateCardDto dto = new CreateCardDto();
        dto.setName("Name");
        assertThrows(IOException.class, () -> cardService.createCard(request, dto, 1L));
    }

    /**
     * Method under test: {@link CardService#createCard(HttpServletRequest, CreateCardDto, Long)}
     */
    @Test
    void testCreateCard2() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");

        CreateCardDto dto = new CreateCardDto();
        dto.setName("Name");
        assertThrows(IOException.class, () -> cardService.createCard(request, dto, 1L));
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CardService#createCard(HttpServletRequest, CreateCardDto, Long)}
     */
    @Test
    void testCreateCard3() throws EntityNotFoundException {
        User user = new User();
        user.setAccounts(new ArrayList<>());
        user.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(BaseEntity.serialVersionUID);
        user.setLastName("Doe");
        user.setPassword("iloveyou");

        Account account = new Account();
        account.setActiveStatus(true);
        account.setCards(new ArrayList<>());
        account.setCurrency(CurrencyType.EUR);
        account.setIBAN("IBAN");
        account.setId(BaseEntity.serialVersionUID);
        account.setName("Name");
        account.setTransactions(new ArrayList<>());
        account.setUser(user);
        when(accountRepository.findUnsafe(Mockito.<Long>any())).thenReturn(account);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        CreateCardDto dto = new CreateCardDto();
        dto.setName("Name");
        assertThrows(BadRequestException.class, () -> cardService.createCard(request, dto, 1L));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(accountRepository).findUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CardService#createCard(HttpServletRequest, CreateCardDto, Long)}
     */
    @Test
    void testCreateCard4() {
        when(jwtService.extractUserId(Mockito.<String>any())).thenThrow(new ResourceNotFoundException("An error occurred"));
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        CreateCardDto dto = new CreateCardDto();
        dto.setName("Name");
        assertThrows(ResourceNotFoundException.class, () -> cardService.createCard(request, dto, 1L));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
    }
}
