package com.virag.finedge.account.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

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
        saveTransaction(
        account.getAccountNumber(),
        TransactionType.WITHDRAW,
        request.getAmount(),
        account.getBalance()
);
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

    @Override
    public TransactionResponse withdraw(String accountNumber, WithdrawRequest request) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        accountRepository.save(account);

        return TransactionResponse.builder()
                .message("Amount withdrawn successfully")
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    @Override
    public TransactionResponse transfer(String senderAccountNumber, TransferRequest request) {

        Account sender = accountRepository.findByAccountNumber(senderAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account receiver = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        saveTransaction(
        sender.getAccountNumber(),
        TransactionType.TRANSFER,
        request.getAmount().negate(),
        sender.getBalance()
);

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

    private String generateAccountNumber() {

        Random random = new Random();

        return String.valueOf(1000000000L + random.nextInt(900000000));
    }
}