package eu.greyson.bank.general.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.dto.CreateAccountDto;
import eu.greyson.bank.general.dto.GetAccountDto;
import eu.greyson.bank.general.enums.CurrencyType;
import eu.greyson.bank.general.model.Account;
import eu.greyson.bank.general.model.User;
import eu.greyson.bank.general.repository.AccountRepository;
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

@ContextConfiguration(classes = {AccountService.class})
@ExtendWith(SpringExtension.class)
class AccountServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link AccountService#createDto(Account)}
     */
    @Test
    void testCreateDto() {
        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenReturn(getAccountDto);

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
        GetAccountDto actualCreateDtoResult = accountService.createDto(account);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
        assertSame(getAccountDto, actualCreateDtoResult);
    }

    /**
     * Method under test: {@link AccountService#createDto(Account)}
     */
    @Test
    void testCreateDto2() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenThrow(new IOException("Msg"));

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
        assertThrows(IOException.class, () -> accountService.createDto(account));
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
    }

    /**
     * Method under test: {@link AccountService#getAccountById(Long)}
     */
    @Test
    void testGetAccountById() throws EntityNotFoundException {
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

        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenReturn(getAccountDto);
        GetAccountDto actualAccountById = accountService.getAccountById(1L);
        verify(accountRepository).findUnsafe(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
        assertSame(getAccountDto, actualAccountById);
    }

    /**
     * Method under test: {@link AccountService#getAccountById(Long)}
     */
    @Test
    void testGetAccountById2() throws EntityNotFoundException {
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
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenThrow(new IOException("Msg"));
        assertThrows(IOException.class, () -> accountService.getAccountById(1L));
        verify(accountRepository).findUnsafe(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
    }

    /**
     * Method under test: {@link AccountService#getAccounts(Long)}
     */
    @Test
    void testGetAccounts() {
        when(accountRepository.findAll()).thenReturn(new ArrayList<>());
        List<GetAccountDto> actualAccounts = accountService.getAccounts(1L);
        verify(accountRepository).findAll();
        assertTrue(actualAccounts.isEmpty());
    }

    /**
     * Method under test: {@link AccountService#getAccounts(Long)}
     */
    @Test
    void testGetAccounts2() {
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

        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(account);
        when(accountRepository.findAll()).thenReturn(accountList);

        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenReturn(getAccountDto);
        List<GetAccountDto> actualAccounts = accountService.getAccounts(1L);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
        verify(accountRepository).findAll();
        assertEquals(1, actualAccounts.size());
    }

    /**
     * Method under test: {@link AccountService#getAccounts(Long)}
     */
    @Test
    void testGetAccounts3() {
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

        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(account2);
        accountList.add(account);
        when(accountRepository.findAll()).thenReturn(accountList);

        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenReturn(getAccountDto);
        List<GetAccountDto> actualAccounts = accountService.getAccounts(1L);
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
        verify(accountRepository).findAll();
        assertEquals(1, actualAccounts.size());
    }

    /**
     * Method under test: {@link AccountService#createAccount(HttpServletRequest, CreateAccountDto)}
     */
    @Test
    void testCreateAccount() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        assertThrows(IOException.class, () -> accountService.createAccount(request, dto));
    }

    /**
     * Method under test: {@link AccountService#createAccount(HttpServletRequest, CreateAccountDto)}
     */
    @Test
    void testCreateAccount2() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        assertThrows(IOException.class, () -> accountService.createAccount(request, dto));
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AccountService#createAccount(HttpServletRequest, CreateAccountDto)}
     */
    @Test
    void testCreateAccount3() throws EntityNotFoundException {
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
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);
        when(accountRepository.findAll()).thenReturn(new ArrayList<>());

        User user2 = new User();
        user2.setAccounts(new ArrayList<>());
        user2.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(BaseEntity.serialVersionUID);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        when(userRepository.findUnsafe(Mockito.<Long>any())).thenReturn(user2);

        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenReturn(getAccountDto);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        GetAccountDto actualCreateAccountResult = accountService.createAccount(request, dto);
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(userRepository).findUnsafe(Mockito.<Long>any());
        verify(accountRepository).save(Mockito.<Account>any());
        verify(request).getHeader(Mockito.<String>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
        verify(accountRepository).findAll();
        assertSame(getAccountDto, actualCreateAccountResult);
    }

    /**
     * Method under test: {@link AccountService#createAccount(HttpServletRequest, CreateAccountDto)}
     */
    @Test
    void testCreateAccount4() throws EntityNotFoundException {
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
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);
        when(accountRepository.findAll()).thenReturn(new ArrayList<>());

        User user2 = new User();
        user2.setAccounts(new ArrayList<>());
        user2.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(BaseEntity.serialVersionUID);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        when(userRepository.findUnsafe(Mockito.<Long>any())).thenReturn(user2);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any()))
                .thenThrow(new IOException("Authorization"));
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        assertThrows(IOException.class, () -> accountService.createAccount(request, dto));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(userRepository).findUnsafe(Mockito.<Long>any());
        verify(accountRepository).save(Mockito.<Account>any());
        verify(request).getHeader(Mockito.<String>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
        verify(accountRepository).findAll();
    }

    /**
     * Method under test: {@link AccountService#createAccount(HttpServletRequest, CreateAccountDto)}
     */
    @Test
    void testCreateAccount5() throws EntityNotFoundException {
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

        User user2 = new User();
        user2.setAccounts(new ArrayList<>());
        user2.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(BaseEntity.serialVersionUID);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");

        Account account2 = new Account();
        account2.setActiveStatus(true);
        account2.setCards(new ArrayList<>());
        account2.setCurrency(CurrencyType.EUR);
        account2.setIBAN("Authorization");
        account2.setId(BaseEntity.serialVersionUID);
        account2.setName("Authorization");
        account2.setTransactions(new ArrayList<>());
        account2.setUser(user2);

        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(account2);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);
        when(accountRepository.findAll()).thenReturn(accountList);

        User user3 = new User();
        user3.setAccounts(new ArrayList<>());
        user3.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user3.setEmail("jane.doe@example.org");
        user3.setFirstName("Jane");
        user3.setId(BaseEntity.serialVersionUID);
        user3.setLastName("Doe");
        user3.setPassword("iloveyou");
        when(userRepository.findUnsafe(Mockito.<Long>any())).thenReturn(user3);

        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenReturn(getAccountDto);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        GetAccountDto actualCreateAccountResult = accountService.createAccount(request, dto);
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(userRepository).findUnsafe(Mockito.<Long>any());
        verify(accountRepository).save(Mockito.<Account>any());
        verify(request).getHeader(Mockito.<String>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
        verify(accountRepository).findAll();
        assertSame(getAccountDto, actualCreateAccountResult);
    }

    /**
     * Method under test: {@link AccountService#createAccount(HttpServletRequest, CreateAccountDto)}
     */
    @Test
    void testCreateAccount6() throws EntityNotFoundException {
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

        User user2 = new User();
        user2.setAccounts(new ArrayList<>());
        user2.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(BaseEntity.serialVersionUID);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");

        Account account2 = new Account();
        account2.setActiveStatus(true);
        account2.setCards(new ArrayList<>());
        account2.setCurrency(CurrencyType.EUR);
        account2.setIBAN("Authorization");
        account2.setId(BaseEntity.serialVersionUID);
        account2.setName("Authorization");
        account2.setTransactions(new ArrayList<>());
        account2.setUser(user2);

        User user3 = new User();
        user3.setAccounts(new ArrayList<>());
        user3.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user3.setEmail("john.smith@example.org");
        user3.setFirstName("John");
        user3.setId(2L);
        user3.setLastName("Smith");
        user3.setPassword("Authorization");

        Account account3 = new Account();
        account3.setActiveStatus(false);
        account3.setCards(new ArrayList<>());
        account3.setCurrency(CurrencyType.USD);
        account3.setIBAN("Bearer ");
        account3.setId(2L);
        account3.setName("Bearer ");
        account3.setTransactions(new ArrayList<>());
        account3.setUser(user3);

        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(account3);
        accountList.add(account2);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);
        when(accountRepository.findAll()).thenReturn(accountList);

        User user4 = new User();
        user4.setAccounts(new ArrayList<>());
        user4.setBirthDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        user4.setEmail("jane.doe@example.org");
        user4.setFirstName("Jane");
        user4.setId(BaseEntity.serialVersionUID);
        user4.setLastName("Doe");
        user4.setPassword("iloveyou");
        when(userRepository.findUnsafe(Mockito.<Long>any())).thenReturn(user4);

        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any())).thenReturn(getAccountDto);
        when(jwtService.extractUserId(Mockito.<String>any())).thenReturn("42");
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        GetAccountDto actualCreateAccountResult = accountService.createAccount(request, dto);
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(userRepository).findUnsafe(Mockito.<Long>any());
        verify(accountRepository).save(Mockito.<Account>any());
        verify(request).getHeader(Mockito.<String>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<GetAccountDto>>any());
        verify(accountRepository).findAll();
        assertSame(getAccountDto, actualCreateAccountResult);
    }

    /**
     * Method under test: {@link AccountService#updateAccount(HttpServletRequest, CreateAccountDto, Long)}
     */
    @Test
    void testUpdateAccount() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        assertThrows(IOException.class, () -> accountService.updateAccount(request, dto, 1L));
    }

    /**
     * Method under test: {@link AccountService#updateAccount(HttpServletRequest, CreateAccountDto, Long)}
     */
    @Test
    void testUpdateAccount2() {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        assertThrows(IOException.class, () -> accountService.updateAccount(request, dto, 1L));
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AccountService#updateAccount(HttpServletRequest, CreateAccountDto, Long)}
     */
    @Test
    void testUpdateAccount3() throws EntityNotFoundException {
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

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        assertThrows(BadRequestException.class, () -> accountService.updateAccount(request, dto, 1L));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(accountRepository).findUnsafe(Mockito.<Long>any());
        verify(request).getHeader(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AccountService#updateAccount(HttpServletRequest, CreateAccountDto, Long)}
     */
    @Test
    void testUpdateAccount4() {
        when(jwtService.extractUserId(Mockito.<String>any())).thenThrow(new IOException("Authorization"));
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("Bearer ");

        CreateAccountDto dto = new CreateAccountDto();
        dto.setCurrency(CurrencyType.EUR);
        dto.setIBAN("IBAN");
        dto.setName("Name");
        assertThrows(IOException.class, () -> accountService.updateAccount(request, dto, 1L));
        verify(jwtService).extractUserId(Mockito.<String>any());
        verify(request).getHeader(Mockito.<String>any());
    }
}
