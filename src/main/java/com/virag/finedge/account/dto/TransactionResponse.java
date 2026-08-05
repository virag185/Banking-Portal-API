package com.virag.finedge.account.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {

    private String message;
    private String accountNumber;
    private BigDecimal balance;
}