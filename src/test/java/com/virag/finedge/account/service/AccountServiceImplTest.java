package com.virag.finedge.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.virag.finedge.account.dto.DepositRequest;
import com.virag.finedge.account.dto.TransferRequest;
import com.virag.finedge.account.dto.WithdrawRequest;
import com.virag.finedge.account.entity.Account;
import com.virag.finedge.account.entity.AccountStatus;
import com.virag.finedge.account.entity.AccountType;
import com.virag.finedge.account.entity.Transaction;
import com.virag.finedge.account.entity.TransactionType;
import com.virag.finedge.account.repository.AccountRepository;
import com.virag.finedge.account.repository.TransactionRepository;
import com.virag.finedge.entity.User;
import com.virag.finedge.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {

        User testUser = User.builder()
                .fullName("Virag Khade")
                .email("virag.finedge.test@gmail.com")
                .build();

        account = Account.builder()
                .id(1L)
                .accountNumber("1437108639")
                .user(testUser)
                .balance(new BigDecimal("8080"))
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.ACTIVE)
                .build();
    }

    // =========================================================
    // TEST 1: SUCCESSFUL DEPOSIT
    // =========================================================

    @Test
    void deposit_shouldIncreaseBalance() {

        DepositRequest request = new DepositRequest();
        request.setAmount(new BigDecimal("500"));

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1437108639",
                "virag.finedge.test@gmail.com"))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = accountService.deposit(
                "1437108639",
                request,
                "virag.finedge.test@gmail.com"
        );

        assertEquals(
                new BigDecimal("8580"),
                response.getBalance()
        );

        verify(accountRepository).save(account);
        verify(transactionRepository).save(any());
    }

    // =========================================================
    // TEST 2: REJECT DEPOSIT INTO ANOTHER USER'S ACCOUNT
    // =========================================================

    @Test
    void deposit_shouldRejectAnotherUsersAccount() {

        DepositRequest request = new DepositRequest();
        request.setAmount(new BigDecimal("500"));

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1437108639",
                "another@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.deposit(
                        "1437108639",
                        request,
                        "another@gmail.com")
        );

        assertEquals(
                "Account not found",
                exception.getMessage()
        );

        verify(
                accountRepository,
                Mockito.never())
                .save(any(Account.class));

        verify(
                transactionRepository,
                Mockito.never())
                .save(any());
    }

    // =========================================================
    // TEST 3: SUCCESSFUL WITHDRAW
    // =========================================================

    @Test
    void withdraw_shouldDecreaseBalance() {

        WithdrawRequest request = new WithdrawRequest();
        request.setAmount(new BigDecimal("1000"));

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1437108639",
                "virag.finedge.test@gmail.com"))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = accountService.withdraw(
                "1437108639",
                request,
                "virag.finedge.test@gmail.com"
        );

        assertEquals(
                new BigDecimal("7080"),
                response.getBalance()
        );

        verify(accountRepository).save(account);
        verify(transactionRepository).save(any());
    }

    // =========================================================
    // TEST 4: REJECT INSUFFICIENT BALANCE
    // =========================================================

    @Test
    void withdraw_shouldRejectInsufficientBalance() {

        WithdrawRequest request = new WithdrawRequest();
        request.setAmount(new BigDecimal("10000"));

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1437108639",
                "virag.finedge.test@gmail.com"))
                .thenReturn(Optional.of(account));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.withdraw(
                        "1437108639",
                        request,
                        "virag.finedge.test@gmail.com")
        );

        assertEquals(
                "Insufficient balance",
                exception.getMessage()
        );

        verify(
                accountRepository,
                Mockito.never())
                .save(any(Account.class));

        verify(
                transactionRepository,
                Mockito.never())
                .save(any());
    }

    // =========================================================
    // TEST 5: SUCCESSFUL TRANSFER
    // =========================================================

    @Test
    void transfer_shouldTransferMoneySuccessfully() {

        User receiverUser = User.builder()
                .fullName("Security Test User")
                .email("finedge.security.test@gmail.com")
                .build();

        Account receiver = Account.builder()
                .id(2L)
                .accountNumber("1872626783")
                .user(receiverUser)
                .balance(new BigDecimal("100"))
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.ACTIVE)
                .build();

        TransferRequest request = new TransferRequest();

        request.setReceiverAccountNumber("1872626783");
        request.setAmount(new BigDecimal("500"));

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1437108639",
                "virag.finedge.test@gmail.com"))
                .thenReturn(Optional.of(account));

        when(accountRepository.findByAccountNumber(
                "1872626783"))
                .thenReturn(Optional.of(receiver));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = accountService.transfer(
                "1437108639",
                request,
                "virag.finedge.test@gmail.com"
        );

        assertEquals(
                new BigDecimal("7580"),
                response.getBalance()
        );

        assertEquals(
                new BigDecimal("600"),
                receiver.getBalance()
        );

        verify(accountRepository).save(account);
        verify(accountRepository).save(receiver);

        verify(
                transactionRepository,
                Mockito.times(2))
                .save(any());
    }

    // =========================================================
    // TEST 6: REJECT TRANSFER FROM ANOTHER USER'S ACCOUNT
    // =========================================================

    @Test
    void transfer_shouldRejectAnotherUsersSenderAccount() {

        TransferRequest request = new TransferRequest();

        request.setReceiverAccountNumber("1437108639");
        request.setAmount(new BigDecimal("100"));

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1872626783",
                "virag.finedge.test@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.transfer(
                        "1872626783",
                        request,
                        "virag.finedge.test@gmail.com")
        );

        assertEquals(
                "Sender account not found",
                exception.getMessage()
        );

        verify(
                accountRepository,
                Mockito.never())
                .save(any(Account.class));

        verify(
                transactionRepository,
                Mockito.never())
                .save(any());
    }

    // =========================================================
    // TEST 7: REJECT CLOSING ACCOUNT WITH BALANCE
    // =========================================================

    @Test
    void closeAccount_shouldRejectAccountWithBalance() {

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1437108639",
                "virag.finedge.test@gmail.com"))
                .thenReturn(Optional.of(account));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.closeAccount(
                        "1437108639",
                        "virag.finedge.test@gmail.com")
        );

        assertEquals(
                "Cannot close account with remaining balance",
                exception.getMessage()
        );

        verify(
                accountRepository,
                Mockito.never())
                .save(any(Account.class));
    }

    // =========================================================
    // TEST 8: SUCCESSFUL ACCOUNT CLOSURE
    // =========================================================

    @Test
    void closeAccount_shouldCloseZeroBalanceAccount() {

        User testUser = User.builder()
                .fullName("Virag Khade")
                .email("virag.finedge.test@gmail.com")
                .build();

        Account zeroBalanceAccount = Account.builder()
                .id(3L)
                .accountNumber("1548053582")
                .user(testUser)
                .balance(BigDecimal.ZERO)
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.ACTIVE)
                .build();

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1548053582",
                "virag.finedge.test@gmail.com"))
                .thenReturn(Optional.of(zeroBalanceAccount));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = accountService.closeAccount(
                "1548053582",
                "virag.finedge.test@gmail.com"
        );

        assertEquals(
                AccountStatus.CLOSED,
                response.getStatus()
        );

        assertEquals(
                BigDecimal.ZERO,
                response.getBalance()
        );

        assertEquals(
                "Virag Khade",
                response.getAccountHolder()
        );

        verify(accountRepository)
                .save(zeroBalanceAccount);
    }

    // =========================================================
    // TEST 9: GET TRANSACTION HISTORY
    // =========================================================

    @Test
    void getTransactions_shouldReturnTransactionHistory() {

        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .accountNumber("1437108639")
                .transactionType(TransactionType.DEPOSIT)
                .amount(new BigDecimal("500"))
                .balanceAfterTransaction(new BigDecimal("8580"))
                .transactionDate(LocalDateTime.now())
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .accountNumber("1437108639")
                .transactionType(TransactionType.WITHDRAW)
                .amount(new BigDecimal("-200"))
                .balanceAfterTransaction(new BigDecimal("8380"))
                .transactionDate(LocalDateTime.now())
                .build();

        List<Transaction> transactions = List.of(
                transaction1,
                transaction2
        );

        when(accountRepository.findByAccountNumberAndUserEmail(
                "1437108639",
                "virag.finedge.test@gmail.com"))
                .thenReturn(Optional.of(account));

        when(transactionRepository
                .findByAccountNumberOrderByTransactionDateDesc(
                        "1437108639"))
                .thenReturn(transactions);

        List<Transaction> result =
                accountService.getTransactions(
                        "1437108639",
                        "virag.finedge.test@gmail.com"
                );

        assertEquals(2, result.size());

        assertEquals(
                TransactionType.DEPOSIT,
                result.get(0).getTransactionType()
        );

        assertEquals(
                new BigDecimal("500"),
                result.get(0).getAmount()
        );

        assertEquals(
                TransactionType.WITHDRAW,
                result.get(1).getTransactionType()
        );

        verify(accountRepository)
                .findByAccountNumberAndUserEmail(
                        "1437108639",
                        "virag.finedge.test@gmail.com"
                );

        verify(transactionRepository)
                .findByAccountNumberOrderByTransactionDateDesc(
                        "1437108639"
                );
    }
}