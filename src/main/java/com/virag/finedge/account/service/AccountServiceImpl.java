package com.virag.finedge.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.virag.finedge.account.dto.AccountResponse;
import com.virag.finedge.account.dto.CreateAccountRequest;
import com.virag.finedge.account.dto.DepositRequest;
import com.virag.finedge.account.dto.TransactionResponse;
import com.virag.finedge.account.dto.TransferRequest;
import com.virag.finedge.account.dto.WithdrawRequest;
import com.virag.finedge.account.entity.Account;
import com.virag.finedge.account.entity.AccountStatus;
import com.virag.finedge.account.entity.Transaction;
import com.virag.finedge.account.entity.TransactionType;
import com.virag.finedge.account.repository.AccountRepository;
import com.virag.finedge.account.repository.TransactionRepository;
import com.virag.finedge.entity.User;
import com.virag.finedge.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    // =========================
    // CREATE ACCOUNT
    // =========================

    @Override
    public AccountResponse createAccount(
            CreateAccountRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

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

    // =========================
    // DEPOSIT
    // =========================

    @Override
    @Transactional
    public TransactionResponse deposit(
            String accountNumber,
            DepositRequest request,
            String email) {

        Account account = accountRepository
                .findByAccountNumberAndUserEmail(
                        accountNumber,
                        email)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException(
                    "Account is not active");
        }

        account.setBalance(
                account.getBalance()
                        .add(request.getAmount())
        );

        accountRepository.save(account);

        saveTransaction(
                account.getAccountNumber(),
                TransactionType.DEPOSIT,
                request.getAmount(),
                account.getBalance()
        );

        return TransactionResponse.builder()
                .message("Amount deposited successfully")
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    // =========================
    // WITHDRAW
    // =========================

    @Override
    @Transactional
    public TransactionResponse withdraw(
            String accountNumber,
            WithdrawRequest request,
            String email) {

        Account account = accountRepository
                .findByAccountNumberAndUserEmail(
                        accountNumber,
                        email)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException(
                    "Account is not active");
        }

        if (account.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new RuntimeException(
                    "Insufficient balance");
        }

        account.setBalance(
                account.getBalance()
                        .subtract(request.getAmount())
        );

        accountRepository.save(account);

        saveTransaction(
                account.getAccountNumber(),
                TransactionType.WITHDRAW,
                request.getAmount().negate(),
                account.getBalance()
        );

        return TransactionResponse.builder()
                .message("Amount withdrawn successfully")
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    // =========================
    // TRANSFER
    // =========================

    @Override
    @Transactional
    public TransactionResponse transfer(
            String senderAccountNumber,
            TransferRequest request,
            String email) {

        // Verify sender belongs to logged-in user
        Account sender = accountRepository
                .findByAccountNumberAndUserEmail(
                        senderAccountNumber,
                        email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sender account not found"));

        if (sender.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException(
                    "Sender account is not active");
        }

        // Find receiver account
        Account receiver = accountRepository
                .findByAccountNumber(
                        request.getReceiverAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Receiver account not found"));

        if (receiver.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException(
                    "Receiver account is not active");
        }

        // Prevent transfer to same account
        if (sender.getAccountNumber()
                .equals(receiver.getAccountNumber())) {

            throw new RuntimeException(
                    "Cannot transfer to the same account");
        }

        // Check balance
        if (sender.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new RuntimeException(
                    "Insufficient balance");
        }

        // Deduct from sender
        sender.setBalance(
                sender.getBalance()
                        .subtract(request.getAmount())
        );

        // Add to receiver
        receiver.setBalance(
                receiver.getBalance()
                        .add(request.getAmount())
        );

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Sender transaction
        saveTransaction(
                sender.getAccountNumber(),
                TransactionType.TRANSFER,
                request.getAmount().negate(),
                sender.getBalance()
        );

        // Receiver transaction
        saveTransaction(
                receiver.getAccountNumber(),
                TransactionType.TRANSFER,
                request.getAmount(),
                receiver.getBalance()
        );

        return TransactionResponse.builder()
                .message("Money transferred successfully")
                .accountNumber(sender.getAccountNumber())
                .balance(sender.getBalance())
                .build();
    }

    // =========================
    // SAVE TRANSACTION
    // =========================

    private void saveTransaction(
            String accountNumber,
            TransactionType transactionType,
            BigDecimal amount,
            BigDecimal balance) {

        Transaction transaction = Transaction.builder()
                .accountNumber(accountNumber)
                .transactionType(transactionType)
                .amount(amount)
                .balanceAfterTransaction(balance)
                .transactionDate(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    // =========================
    // TRANSACTION HISTORY
    // =========================

    @Override
    public List<Transaction> getTransactions(
            String accountNumber,
            String email) {

        accountRepository
                .findByAccountNumberAndUserEmail(
                        accountNumber,
                        email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found"));

        return transactionRepository
                .findByAccountNumberOrderByTransactionDateDesc(
                        accountNumber);
    }

    // =========================
    // GET SINGLE ACCOUNT
    // =========================

    @Override
    public AccountResponse getAccount(
            String accountNumber,
            String email) {

        Account account = accountRepository
                .findByAccountNumberAndUserEmail(
                        accountNumber,
                        email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException(
                    "Account is not active");
        }

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountHolder(
                        account.getUser().getFullName())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .build();
    }

    // =========================
    // GET USER ACCOUNTS
    // =========================

    @Override
    public List<AccountResponse> getUserAccounts(
            String email) {

        List<Account> accounts =
                accountRepository.findByUserEmail(email);

        return accounts.stream()
                .map(account ->
                        AccountResponse.builder()
                                .accountNumber(
                                        account.getAccountNumber())
                                .accountHolder(
                                        account.getUser()
                                                .getFullName())
                                .balance(
                                        account.getBalance())
                                .accountType(
                                        account.getAccountType())
                                .status(
                                        account.getStatus())
                                .build())
                .toList();
    }

    // =========================
    // CLOSE ACCOUNT
    // =========================

    @Override
    @Transactional
    public AccountResponse closeAccount(
            String accountNumber,
            String email) {

        Account account = accountRepository
                .findByAccountNumberAndUserEmail(
                        accountNumber,
                        email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found"));

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new RuntimeException(
                    "Account is already closed");
        }

        if (account.getBalance()
                .compareTo(BigDecimal.ZERO) > 0) {

            throw new RuntimeException(
                    "Cannot close account with remaining balance");
        }

        account.setStatus(AccountStatus.CLOSED);

        accountRepository.save(account);

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountHolder(
                        account.getUser().getFullName())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .build();
    }

    // =========================
    // GENERATE ACCOUNT NUMBER
    // =========================

    private String generateAccountNumber() {

        Random random = new Random();

        return String.valueOf(
                1000000000L +
                        random.nextInt(900000000)
        );
    }
}