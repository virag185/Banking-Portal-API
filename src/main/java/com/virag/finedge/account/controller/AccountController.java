package com.virag.finedge.account.controller;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virag.finedge.account.dto.AccountResponse;
import com.virag.finedge.account.dto.CreateAccountRequest;
import com.virag.finedge.account.dto.DepositRequest;
import com.virag.finedge.account.dto.TransactionResponse;
import com.virag.finedge.account.dto.TransferRequest;
import com.virag.finedge.account.dto.WithdrawRequest;
import com.virag.finedge.account.entity.Transaction;
import com.virag.finedge.account.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponse createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            Authentication authentication) {

        return accountService.createAccount(
                request,
                authentication.getName()
        );
    }

    @PostMapping("/{accountNumber}/deposit")
    public TransactionResponse deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest request) {

        return accountService.deposit(accountNumber, request);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public TransactionResponse withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody WithdrawRequest request) {

        return accountService.withdraw(accountNumber, request);
    }

    @PostMapping("/{accountNumber}/transfer")
    public TransactionResponse transfer(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransferRequest request) {

        return accountService.transfer(accountNumber, request);
    }

    @GetMapping
public List<AccountResponse> getUserAccounts(
        Authentication authentication) {

    return accountService.getUserAccounts(
            authentication.getName()
    );
}

    @GetMapping("/{accountNumber}")
public AccountResponse getAccount(
        @PathVariable String accountNumber) {

    return accountService.getAccount(accountNumber);
}


    @GetMapping("/{accountNumber}/transactions")
public List<Transaction> getTransactions(
        @PathVariable String accountNumber) {

    return accountService.getTransactions(accountNumber);
}
}