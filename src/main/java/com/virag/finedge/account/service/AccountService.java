package com.virag.finedge.account.service;

import com.virag.finedge.account.dto.AccountResponse;
import com.virag.finedge.account.dto.CreateAccountRequest;
import com.virag.finedge.account.dto.DepositRequest;
import com.virag.finedge.account.dto.TransactionResponse;
import com.virag.finedge.account.dto.WithdrawRequest;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest request, String email);

    TransactionResponse deposit(String accountNumber, DepositRequest request);

    TransactionResponse withdraw(String accountNumber, WithdrawRequest request);
}