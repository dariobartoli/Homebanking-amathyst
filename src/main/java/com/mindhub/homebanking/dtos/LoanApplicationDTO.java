package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanApplicationDTO {

    private Long id;
    private Double amount;
    private int payments;
    private String accountDestiny;

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }
}
