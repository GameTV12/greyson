package eu.greyson.bank.general.rest.account;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.greyson.bank.general.dto.CreateAccountDto;
import eu.greyson.bank.general.dto.CreateCardDto;
import eu.greyson.bank.general.dto.CreateNewTransactionDto;
import eu.greyson.bank.general.dto.GetAccountDto;
import eu.greyson.bank.general.dto.GetCardDto;
import eu.greyson.bank.general.dto.GetTransactionDto;
import eu.greyson.bank.general.dto.PutCardBlockDto;
import eu.greyson.bank.general.enums.CurrencyType;
import eu.greyson.bank.general.service.AccountService;
import eu.greyson.bank.general.service.CardService;
import eu.greyson.bank.general.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AccountControllerImpl.class})
@ExtendWith(SpringExtension.class)
class AccountControllerImplTest {
    @Autowired
    private AccountControllerImpl accountControllerImpl;

    @MockBean
    private AccountService accountService;

    @MockBean
    private CardService cardService;

    @MockBean
    private TransactionService transactionService;

    /**
     * Method under test: {@link AccountControllerImpl#getAccounts(Long)}
     */
    @Test
    void testGetAccounts() throws Exception {
        when(accountService.getAccounts(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/accounts");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("userId", String.valueOf(1L));
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#getAccountById(Long)}
     */
    @Test
    void testGetAccountById() throws Exception {
        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(accountService.getAccountById(Mockito.<Long>any())).thenReturn(getAccountDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/{id}", 1L);
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"Name\",\"balance\":10.0,\"currency\":\"EUR\",\"iban\":\"IBAN\"}"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#updateAccount(HttpServletRequest, Long, CreateAccountDto)}
     */
    @Test
    void testUpdateAccount() throws Exception {
        GetAccountDto getAccountDto = new GetAccountDto();
        getAccountDto.setBalance(10.0d);
        getAccountDto.setCurrency(CurrencyType.EUR);
        getAccountDto.setIBAN("IBAN");
        getAccountDto.setId(1L);
        getAccountDto.setName("Name");
        when(accountService.updateAccount(Mockito.<HttpServletRequest>any(), Mockito.<CreateAccountDto>any(),
                Mockito.<Long>any())).thenReturn(getAccountDto);

        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setCurrency(CurrencyType.EUR);
        createAccountDto.setIBAN("IBAN");
        createAccountDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(createAccountDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/accounts/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"Name\",\"balance\":10.0,\"currency\":\"EUR\",\"iban\":\"IBAN\"}"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#getAllTransactions(Long)}
     */
    @Test
    void testGetAllTransactions() throws Exception {
        when(transactionService.getAllTransactions(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/{id}/transactions", 1L);
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#createTransaction(HttpServletRequest, Long, CreateNewTransactionDto)}
     */
    @Test
    void testCreateTransaction() throws Exception {
        GetTransactionDto getTransactionDto = new GetTransactionDto();
        getTransactionDto.setAmount(10.0d);
        getTransactionDto.setCreditor("Creditor");
        getTransactionDto
                .setDateCreated(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getTransactionDto
                .setDateExecuted(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getTransactionDto.setDebtor("Debtor");
        getTransactionDto.setId(1L);
        when(transactionService.createTransaction(Mockito.<HttpServletRequest>any(), Mockito.<CreateNewTransactionDto>any(),
                Mockito.<Long>any())).thenReturn(getTransactionDto);

        CreateNewTransactionDto createNewTransactionDto = new CreateNewTransactionDto();
        createNewTransactionDto.setAmount(10.0d);
        createNewTransactionDto.setDebtor("Debtor");
        String content = (new ObjectMapper()).writeValueAsString(createNewTransactionDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/accounts/{id}/transactions", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"amount\":10.0,\"creditor\":\"Creditor\",\"debtor\":\"Debtor\",\"dateCreated\":0,\"dateExecuted\":0}"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#getCards(Long)}
     */
    @Test
    void testGetCards() throws Exception {
        when(cardService.getCards(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/{id}/cards", 1L);
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#createCard(HttpServletRequest, Long, CreateCardDto)}
     */
    @Test
    void testCreateCard() throws Exception {
        GetCardDto getCardDto = new GetCardDto();
        getCardDto.setBlocked(true);
        getCardDto.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getCardDto.setId(1L);
        getCardDto.setName("Name");
        when(cardService.createCard(Mockito.<HttpServletRequest>any(), Mockito.<CreateCardDto>any(), Mockito.<Long>any()))
                .thenReturn(getCardDto);

        CreateCardDto createCardDto = new CreateCardDto();
        createCardDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(createCardDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/accounts/{id}/cards", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\",\"blocked\":true,\"dateLocked\":0}"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#getCardById(Long, Long)}
     */
    @Test
    void testGetCardById() throws Exception {
        GetCardDto getCardDto = new GetCardDto();
        getCardDto.setBlocked(true);
        getCardDto.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getCardDto.setId(1L);
        getCardDto.setName("Name");
        when(cardService.getCardById(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(getCardDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts/{accountId}/cards/{id}",
                1L, 1L);
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\",\"blocked\":true,\"dateLocked\":0}"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#changeBlockStatus(HttpServletRequest, Long, Long, PutCardBlockDto)}
     */
    @Test
    void testChangeBlockStatus() throws Exception {
        GetCardDto getCardDto = new GetCardDto();
        getCardDto.setBlocked(true);
        getCardDto.setDateLocked(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        getCardDto.setId(1L);
        getCardDto.setName("Name");
        when(cardService.changeBlockStatus(Mockito.<HttpServletRequest>any(), Mockito.<Long>any(), Mockito.<Long>any(),
                Mockito.<PutCardBlockDto>any())).thenReturn(getCardDto);

        PutCardBlockDto putCardBlockDto = new PutCardBlockDto();
        putCardBlockDto.setBlocked(true);
        String content = (new ObjectMapper()).writeValueAsString(putCardBlockDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/accounts/{accountId}/cards/{id}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\",\"blocked\":true,\"dateLocked\":0}"));
    }

    /**
     * Method under test: {@link AccountControllerImpl#createAccount(HttpServletRequest, CreateAccountDto)}
     */
    @Test
    void testCreateAccount() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setCurrency(CurrencyType.EUR);
        createAccountDto.setIBAN("IBAN");
        createAccountDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(createAccountDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(accountControllerImpl)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}
