package com.virag.finedge.account.dto;

import java.math.BigDecimal;

import com.virag.finedge.account.entity.AccountStatus;
import com.virag.finedge.account.entity.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private String accountNumber;
    private String accountHolder;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;
}