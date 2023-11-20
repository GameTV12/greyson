package eu.greyson.bank.general.rest.account;

import com.fasterxml.jackson.annotation.JsonView;
import eu.greyson.bank.general.dto.*;
import eu.greyson.bank.general.service.AccountService;
import eu.greyson.bank.general.service.CardService;
import eu.greyson.bank.general.service.TransactionService;
import eu.greyson.bank.shared.json.DtoView;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CardService cardService;

    @Override
    @GetMapping()
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<List<GetAccountDto>> getAccounts(@RequestParam(name = "userId") Long userId) {
        var accounts = accountService.getAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @Override
    @GetMapping("{id}")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetAccountDto> getAccountById(@PathVariable(name = "id") Long id) {
        var account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @Override
    @PostMapping
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetAccountDto> createAccount(HttpServletRequest request, @RequestBody CreateAccountDto dto) {
        var account = accountService.createAccount(request, dto);
        return ResponseEntity.ok(account);
    }

    @Override
    @PutMapping("{id}")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetAccountDto> updateAccount(HttpServletRequest request, @PathVariable(name = "id") Long accountId, @RequestBody CreateAccountDto dto) {
        var account = accountService.updateAccount(request, dto, accountId);
        return ResponseEntity.ok(account);
    }

    @Override
    @GetMapping("{id}/transactions")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<List<GetTransactionDto>> getAllTransactions(@PathVariable(name = "id") Long id) {
        var transactions = transactionService.getAllTransactions(id);
        return ResponseEntity.ok(transactions);
    }

    @Override
    @PostMapping("{id}/transactions")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetTransactionDto> createTransaction(HttpServletRequest request, @PathVariable(name = "id") Long id, @RequestBody CreateNewTransactionDto dto) {
        var transaction = transactionService.createTransaction(request, dto, id);
        return ResponseEntity.ok(transaction);
    }

    @Override
    @GetMapping("{id}/cards")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<List<GetCardDto>> getCards(@PathVariable(name = "id") Long id) {
        var cards = cardService.getCards(id);
        return ResponseEntity.ok(cards);
    }

    @Override
    @PostMapping("{id}/cards")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetCardDto> createCard(HttpServletRequest request, @PathVariable(name = "id") Long id, @RequestBody CreateCardDto dto) {
        var card = cardService.createCard(request, dto, id);
        return ResponseEntity.ok(card);
    }

    @Override
    @GetMapping("{accountId}/cards/{id}")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetCardDto> getCardById(@PathVariable(name = "accountId") Long accountId, @PathVariable(name = "id") Long cardId) {
        var card = cardService.getCardById(accountId, cardId);
        return ResponseEntity.ok(card);
    }

    @Override
    @PutMapping("{accountId}/cards/{id}")
    @JsonView(DtoView.Detail.class)
    public ResponseEntity<GetCardDto> changeBlockStatus(HttpServletRequest request, @PathVariable(name = "accountId") Long accountId, @PathVariable(name = "id") Long cardId, @RequestBody PutCardBlockDto dto) {
        var card = cardService.changeBlockStatus(request, accountId, cardId, dto);
        return ResponseEntity.ok(card);
    }
}
