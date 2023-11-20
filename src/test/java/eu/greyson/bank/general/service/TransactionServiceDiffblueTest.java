package eu.greyson.bank.general.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.dto.CreateNewTransactionDto;
import eu.greyson.bank.general.dto.GetTransactionDto;
import eu.greyson.bank.general.enums.CurrencyType;
import eu.greyson.bank.general.model.Account;
import eu.greyson.bank.general.model.Transaction;
import eu.greyson.bank.general.model.User;
import eu.greyson.bank.general.repository.AccountRepository;
import eu.greyson.bank.general.repository.TransactionRepository;
import eu.greyson.bank.general.repository.UserRepository;
import eu.greyson.bank.shared.exception.BadRequestException;
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

@ContextConfiguration(classes = {TransactionService.class})
@ExtendWith(SpringExtension.class)
class TransactionServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link TransactionService#createDto(Transaction)}
     */
    @Test
    void testCreateDto() {
        GetTransactionDto getTransactionDto = new GetTransactionDto();
        getTransactionDto.setAmount(10.0d);
        getTransactionDto.setCreditor("Creditor");
        getTransactionDto
                .setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getTransactionDto
                .setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getTransactionDto.setDebtor("Debtor");
        getTransactionDto.setId(1L);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetTransactionDto>>any())).thenReturn(getTransactionDto);

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

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(10.0d);
        transaction.setCreditor("Creditor");
        transaction.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setDebtor("Debtor");
        transaction.setId(BaseEntity.serialVersionUID);
        GetTransactionDto actualCreateDtoResult = transactionService.createDto(transaction);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetTransactionDto>>any());
        assertSame(getTransactionDto, actualCreateDtoResult);
    }

    /**
     * Method under test: {@link TransactionService#createDto(Transaction)}
     */
    @Test
    void testCreateDto2() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetTransactionDto>>any()))
                .thenThrow(new IOException("Msg"));

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

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(10.0d);
        transaction.setCreditor("Creditor");
        transaction.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setDebtor("Debtor");
        transaction.setId(BaseEntity.serialVersionUID);
        assertThrows(IOException.class, () -> transactionService.createDto(transaction));
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetTransactionDto>>any());
    }

    /**
     * Method under test: {@link TransactionService#getAllTransactions(Long)}
     */
    @Test
    void testGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(new ArrayList<>());
        List<GetTransactionDto> actualAllTransactions = transactionService.getAllTransactions(1L);
        verify(transactionRepository).findAll();
        assertTrue(actualAllTransactions.isEmpty());
    }

    /**
     * Method under test: {@link TransactionService#getAllTransactions(Long)}
     */
    @Test
    void testGetAllTransactions2() {
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

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(10.0d);
        transaction.setCreditor("Creditor");
        transaction.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setDebtor("Debtor");
        transaction.setId(BaseEntity.serialVersionUID);

        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionRepository.findAll()).thenReturn(transactionList);

        GetTransactionDto getTransactionDto = new GetTransactionDto();
        getTransactionDto.setAmount(10.0d);
        getTransactionDto.setCreditor("Creditor");
        getTransactionDto
                .setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getTransactionDto
                .setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getTransactionDto.setDebtor("Debtor");
        getTransactionDto.setId(1L);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetTransactionDto>>any())).thenReturn(getTransactionDto);
        List<GetTransactionDto> actualAllTransactions = transactionService.getAllTransactions(1L);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetTransactionDto>>any());
        verify(transactionRepository).findAll();
        assertEquals(1, actualAllTransactions.size());
    }

    /**
     * Method under test: {@link TransactionService#getAllTransactions(Long)}
     */
    @Test
    void testGetAllTransactions3() {
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

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(10.0d);
        transaction.setCreditor("Creditor");
        transaction.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction.setDebtor("Debtor");
        transaction.setId(BaseEntity.serialVersionUID);

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

        Transaction transaction2 = new Transaction();
        transaction2.setAccount(account2);
        transaction2.setAmount(0.5d);
        transaction2.setCreditor("eu.greyson.bank.general.model.Transaction");
        transaction2.setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction2.setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        transaction2.setDebtor("eu.greyson.bank.general.model.Transaction");
        transaction2.setId(2L);

        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction2);
        transactionList.add(transaction);
        when(transactionRepository.findAll()).thenReturn(transactionList);

        GetTransactionDto getTransactionDto = new GetTransactionDto();
        getTransactionDto.setAmount(10.0d);
        getTransactionDto.setCreditor("Creditor");
        getTransactionDto
                .setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getTransactionDto
                .setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getTransactionDto.setDebtor("Debtor");
        getTransactionDto.setId(1L);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetTransactionDto>>any())).thenReturn(getTransactionDto);
        List<GetTransactionDto> actualAllTransactions = transactionService.getAllTransactions(1L);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetTransactionDto>>any());
        verify(transactionRepository).findAll();
        assertEquals(1, actualAllTransactions.size());
    }

    /**
     * Method under test: {@link TransactionService#createTransaction(HttpServletRequest, CreateNewTransactionDto, Long)}
     */
    @Test
    void testCreateTransaction() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        CreateNewTransactionDto dto = new CreateNewTransactionDto();
        dto.setAmount(10.0d);
        dto.setDebtor("Debtor");
        assertThrows(IOException.class, () -> transactionService.createTransaction(request, dto, 1L));
    }

    /**
     * Method under test: {@link TransactionService#createTransaction(HttpServletRequest, CreateNewTransactionDto, Long)}
     */
    @Test
    void testCreateTransaction2() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");

        CreateNewTransactionDto dto = new CreateNewTransactionDto();
        dto.setAmount(10.0d);
        dto.setDebtor("Debtor");
        assertThrows(IOException.class, () -> transactionService.createTransaction(request, dto, 1L));
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link TransactionService#createTransaction(HttpServletRequest, CreateNewTransactionDto, Long)}
     */
    @Test
    void testCreateTransaction3() throws EntityNotFoundException {
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

        CreateNewTransactionDto dto = new CreateNewTransactionDto();
        dto.setAmount(10.0d);
        dto.setDebtor("Debtor");
        assertThrows(BadRequestException.class, () -> transactionService.createTransaction(request, dto, 1L));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(accountRepository).findUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link TransactionService#createTransaction(HttpServletRequest, CreateNewTransactionDto, Long)}
     */
    @Test
    void testCreateTransaction4() {
        when(jwtService.extractUserId(Mockito.<String>any())).thenThrow(new IOException("Authorization"));
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        CreateNewTransactionDto dto = new CreateNewTransactionDto();
        dto.setAmount(10.0d);
        dto.setDebtor("Debtor");
        assertThrows(IOException.class, () -> transactionService.createTransaction(request, dto, 1L));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
    }
}
