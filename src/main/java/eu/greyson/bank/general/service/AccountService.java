package eu.greyson.bank.general.service;

import eu.greyson.bank.general.config.JwtService;
import eu.greyson.bank.general.dto.CreateAccountDto;
import eu.greyson.bank.general.dto.GetAccountDto;
import eu.greyson.bank.general.model.Account;
import eu.greyson.bank.general.model.User;
import eu.greyson.bank.general.repository.AccountRepository;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService extends BaseService<Account, Long, AccountRepository> implements InitializingBean {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JwtService jwtService;

    @Override
    protected AccountRepository getDefaultRepository() {
        return accountRepository;
    }

    @Override
    public void afterPropertiesSet() {
        mapper.addMappings(new AccountDtoMapper());
    }

    private static class AccountDtoMapper extends PropertyMap<Account, GetAccountDto> {
        @Override
        protected void configure() {
            map().setIBAN(source.getIBAN());
            map().setName(source.getName());
            map().setCurrency(source.getCurrency());
            map().setBalance(source.getBalance());
        }
    }

    public GetAccountDto createDto(Account account) {
        return mapper.map(account, GetAccountDto.class);
    }

    public GetAccountDto getAccountById(Long id) {
        return createDto(getUnsafe(id));
    }

    public List<GetAccountDto> getAccounts(Long userId) {
        return accountRepository.findAll().stream().filter(x -> Objects.equals(x.getUser().getId(), userId)).map(this::createDto).collect(Collectors.toList());
    }

    public GetAccountDto createAccount(HttpServletRequest request, CreateAccountDto dto) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new IOException("Not user");
        String token = authHeader.substring(7);
        Long userId = Long.parseLong(jwtService.extractUserId(token));

        Account newAccount = new Account();
        newAccount.setName(dto.getName());
        newAccount.setCurrency(dto.getCurrency());
        if (accountRepository.findAll().stream().filter(item -> Objects.equals(item.getIBAN(), dto.getIBAN())).map(this::createDto).findAny().isPresent()) {
            throw new BadRequestException("This IBAN already exists");
        }
        newAccount.setIBAN(dto.getIBAN());
        User user = userRepository.findUnsafe(userId);
        newAccount.setUser(user);
        accountRepository.save(newAccount);
        return createDto(newAccount);
    }

    public GetAccountDto updateAccount(HttpServletRequest request, CreateAccountDto dto, Long accountId) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new IOException("Not user");
        String token = authHeader.substring(7);
        if (!Objects.equals(jwtService.extractUserId(token), accountRepository.findUnsafe(accountId).getUser().getId()+"")) throw new BadRequestException("User isn't allowed");

        Account newAccount = getUnsafe(accountId);
        newAccount.setName(dto.getName());
        newAccount.setCurrency(dto.getCurrency());
        if (accountRepository.findAll().stream().filter(item -> Objects.equals(item.getIBAN(), dto.getIBAN())).map(this::createDto).findAny().isPresent()) throw new BadRequestException("This IBAN already exists");
        newAccount.setIBAN(dto.getIBAN());
        accountRepository.save(newAccount);
        return createDto(newAccount);
    }

}
