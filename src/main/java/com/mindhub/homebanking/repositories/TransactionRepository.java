package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.Set;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository <Transaction, Long> {

    Set<Transaction> findByDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo);
}
