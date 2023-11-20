package eu.greyson.bank.general.service;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.dto.CreateNewTransactionDto;
import eu.greyson.bank.general.dto.GetTransactionDto;
import eu.greyson.bank.general.model.Account;
import eu.greyson.bank.general.model.Transaction;
import eu.greyson.bank.general.repository.AccountRepository;
import eu.greyson.bank.general.repository.TransactionRepository;
import eu.greyson.bank.general.repository.UserRepository;
import eu.greyson.bank.shared.exception.BadRequestException;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService extends BaseService<Transaction, Long, TransactionRepository> implements InitializingBean {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper mapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected TransactionRepository getDefaultRepository() {
        return transactionRepository;
    }

    @Override
    public void afterPropertiesSet() {
        mapper.addMappings(new TransactionDtoMapper());
    }

    public GetTransactionDto createDto(Transaction transaction) {
        return mapper.map(transaction, GetTransactionDto.class);
    }

    public List<GetTransactionDto> getAllTransactions(Long accountId) {
        return transactionRepository.findAll().stream().
                filter(transaction -> Objects.equals(transaction.getAccount().getId(), accountId)).
                map(this::createDto).collect(Collectors.toList());
    }

    public GetTransactionDto createTransaction(HttpServletRequest request, CreateNewTransactionDto dto, Long accountId) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new IOException("Not user");
        String token = authHeader.substring(7);
        if (!Objects.equals(jwtService.extractUserId(token), accountRepository.findUnsafe(accountId).getUser().getId()+"")) throw new BadRequestException("User isn't allowed");

        Transaction newTransaction = new Transaction();
        newTransaction.setDebtor(dto.getDebtor());
        newTransaction.setAmount(dto.getAmount());
        Account account = accountRepository.findUnsafe(accountId);
        newTransaction.setAccount(account);
        newTransaction.setCreditor(account.getIBAN());
        Optional<Account> possibleReciever = accountRepository.findAll().stream().filter(x -> Objects.equals(x.getIBAN(), dto.getDebtor())).findFirst();
        if (possibleReciever.isPresent()) {
            newTransaction.setDateExecuted(new Date());
            Transaction newTransaction1 = new Transaction();
            newTransaction1.setCreditor(account.getIBAN());
            newTransaction1.setAmount(dto.getAmount());
            newTransaction1.setDebtor(possibleReciever.get().getIBAN());
            newTransaction1.setAccount(possibleReciever.get());
            newTransaction1.setDateCreated(newTransaction.getDateCreated());
            newTransaction1.setDateExecuted(newTransaction.getDateExecuted());
            transactionRepository.save(newTransaction1);
        }
        transactionRepository.save(newTransaction);
        return createDto(newTransaction);
    }

    private static class TransactionDtoMapper extends PropertyMap<Transaction, GetTransactionDto> {
        @Override
        protected void configure() {
            map().setAmount(source.getAmount());
            map().setCreditor(source.getCreditor());
            map().setDebtor(source.getDebtor());
            map().setDateCreated(source.getDateCreated());
        }
    }
}
