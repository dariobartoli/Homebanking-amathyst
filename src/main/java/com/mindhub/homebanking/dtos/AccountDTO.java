package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;

    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    private boolean active;

    private AccountType type;

    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO (Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.type = account.getType();
        this.active = account.isActive();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public AccountType getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public Set<TransactionDTO> getTransactions() { return transactions; }
}
