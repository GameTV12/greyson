package eu.greyson.bank.general.rest.account;

import eu.greyson.bank.general.dto.*;
import eu.greyson.bank.shared.swagger.ApiResponses_200_401_404_500;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Account", description = "Account Controller")
public interface AccountController {
    @Operation(
            summary = "List of user's accounts.",
            description = "This endpoint returns list of all accounts."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<List<GetAccountDto>> getAccounts(@NotNull Long userId);

    @Operation(
            summary = "Get account by id.",
            description = "This endpoint returns account of user."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetAccountDto> getAccountById(@NotNull Long accountId);

    @Operation(
            summary = "Create a new account.",
            description = "This endpoint creates an account for the user"
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetAccountDto> createAccount(HttpServletRequest request, @NotNull CreateAccountDto dto);

    @Operation(
            summary = "Update the account.",
            description = "This endpoint updates the account for user"
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetAccountDto> updateAccount(HttpServletRequest request, @NotNull Long accountId, @NotNull CreateAccountDto dto);

    @Operation(
            summary = "List of account's transactions.",
            description = "This endpoint returns list of all stores."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<List<GetTransactionDto>> getAllTransactions(@NotNull Long id);

    @Operation(
            summary = "Create a new transaction.",
            description = "This endpoint creates a new transaction."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetTransactionDto> createTransaction(HttpServletRequest request, @NotNull Long id, @NotNull CreateNewTransactionDto dto);

    @Operation(
            summary = "List of account's cards.",
            description = "This endpoint returns list of all cards."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<List<GetCardDto>> getCards(@NotNull Long accountId);

    @Operation(
            summary = "Get card by id.",
            description = "This endpoint returns a card of user."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetCardDto> getCardById(@NotNull Long accountId, @NotNull Long cardId);

    @Operation(
            summary = "Block/unblock card",
            description = "This endpoint un/blocks card and returns the new card of user."
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetCardDto> changeBlockStatus(HttpServletRequest request, @NotNull Long accountId, @NotNull Long cardId, @NotNull PutCardBlockDto dto);

    @Operation(
            summary = "Create a new card",
            description = "This endpoint creates a card for the account"
    )
    @ApiResponses_200_401_404_500
    ResponseEntity<GetCardDto> createCard(HttpServletRequest request, @NotNull Long accountId, @NotNull CreateCardDto dto);
}

