package com.virag.finedge.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.virag.finedge.account.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber);

}