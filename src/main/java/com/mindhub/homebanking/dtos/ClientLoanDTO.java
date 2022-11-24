package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class ClientLoanDTO {

    private Long id;

    private Long loanId;
    private String name;
    private Double maxAmount;

    private int payments;

    public ClientLoanDTO(){
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.maxAmount = clientLoan.getMaxAmount();
        this.payments = clientLoan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public int getPayments() {
        return payments;
    }
}
