package com.virag.finedge.account.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    // =========================
    // CREATE ACCOUNT
    // =========================

    @PostMapping
    public AccountResponse createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            Authentication authentication) {

        return accountService.createAccount(
                request,
                authentication.getName()
        );
    }

    // =========================
    // DEPOSIT
    // =========================

    @PostMapping("/{accountNumber}/deposit")
    public TransactionResponse deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest request,
            Authentication authentication) {

        return accountService.deposit(
                accountNumber,
                request,
                authentication.getName()
        );
    }

    // =========================
    // WITHDRAW
    // =========================

    @PostMapping("/{accountNumber}/withdraw")
    public TransactionResponse withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody WithdrawRequest request,
            Authentication authentication) {

        return accountService.withdraw(
                accountNumber,
                request,
                authentication.getName()
        );
    }

    // =========================
    // TRANSFER
    // =========================

    @PostMapping("/{accountNumber}/transfer")
    public TransactionResponse transfer(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransferRequest request,
            Authentication authentication) {

        return accountService.transfer(
                accountNumber,
                request,
                authentication.getName()
        );
    }

    // =========================
    // GET USER ACCOUNTS
    // =========================

    @GetMapping
    public List<AccountResponse> getUserAccounts(
            Authentication authentication) {

        return accountService.getUserAccounts(
                authentication.getName()
        );
    }

    // =========================
    // GET SINGLE ACCOUNT
    // =========================

    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(
            @PathVariable String accountNumber,
            Authentication authentication) {

        return accountService.getAccount(
                accountNumber,
                authentication.getName()
        );
    }

    // =========================
    // TRANSACTION HISTORY
    // =========================

    @GetMapping("/{accountNumber}/transactions")
    public List<Transaction> getTransactions(
            @PathVariable String accountNumber,
            Authentication authentication) {

        return accountService.getTransactions(
                accountNumber,
                authentication.getName()
        );
    }

    // =========================
    // CLOSE ACCOUNT
    // =========================

    @PatchMapping("/{accountNumber}/close")
    public AccountResponse closeAccount(
            @PathVariable String accountNumber,
            Authentication authentication) {

        return accountService.closeAccount(
                accountNumber,
                authentication.getName()
        );
    }
}