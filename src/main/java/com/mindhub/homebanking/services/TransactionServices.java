package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public interface TransactionServices {

    public void saveTransaction(Transaction transaction);

    public Set<Transaction> getTransactionByDate (LocalDateTime dateFrom, LocalDateTime dateTo);


}
