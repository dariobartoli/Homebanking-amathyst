package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class TransactionServicesImplement implements TransactionServices {

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Set<Transaction> getTransactionByDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return transactionRepository.findByDateBetween(dateFrom, dateTo);
    }
}
