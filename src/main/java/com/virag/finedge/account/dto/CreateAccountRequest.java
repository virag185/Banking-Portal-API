package com.virag.finedge.account.dto;

import com.virag.finedge.account.entity.AccountType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotNull(message = "Account type is required")
    private AccountType accountType;
}