package com.virag.finedge.account.service;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.virag.finedge.account.dto.AccountResponse;
import com.virag.finedge.account.dto.CreateAccountRequest;
import com.virag.finedge.account.dto.DepositRequest;
import com.virag.finedge.account.dto.TransactionResponse;
import com.virag.finedge.account.entity.Account;
import com.virag.finedge.account.entity.AccountStatus;
import com.virag.finedge.account.repository.AccountRepository;
import com.virag.finedge.entity.User;
import com.virag.finedge.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .user(user)
                .balance(BigDecimal.ZERO)
                .accountType(request.getAccountType())
                .status(AccountStatus.ACTIVE)
                .build();

        accountRepository.save(account);

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountHolder(user.getFullName())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .build();
    }

    @Override
    public TransactionResponse deposit(String accountNumber, DepositRequest request) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));

        accountRepository.save(account);

        return TransactionResponse.builder()
                .message("Amount deposited successfully")
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    private String generateAccountNumber() {

        Random random = new Random();

        return String.valueOf(1000000000L + random.nextInt(900000000));
    }
}