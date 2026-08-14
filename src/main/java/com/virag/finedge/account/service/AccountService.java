package com.virag.finedge.account.service;

import java.util.List;

import com.virag.finedge.account.dto.AccountResponse;
import com.virag.finedge.account.dto.CreateAccountRequest;
import com.virag.finedge.account.dto.DepositRequest;
import com.virag.finedge.account.dto.TransactionResponse;
import com.virag.finedge.account.dto.TransferRequest;
import com.virag.finedge.account.dto.WithdrawRequest;
import com.virag.finedge.account.entity.Transaction;

public interface AccountService {

    AccountResponse createAccount(
            CreateAccountRequest request,
            String email
    );

    TransactionResponse deposit(
            String accountNumber,
            DepositRequest request,
            String email
    );

    TransactionResponse withdraw(
            String accountNumber,
            WithdrawRequest request,
            String email
    );

    TransactionResponse transfer(
            String senderAccountNumber,
            TransferRequest request,
            String email
    );

    List<Transaction> getTransactions(
            String accountNumber,
            String email
    );

    AccountResponse getAccount(
            String accountNumber,
            String email
    );

    List<AccountResponse> getUserAccounts(
            String email
    );

    AccountResponse closeAccount(
            String accountNumber,
            String email
    );
}