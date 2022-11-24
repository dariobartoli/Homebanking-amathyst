package com.mindhub.homebanking.services;


import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanServices {

    public List<LoanDTO> getAllLoanDTO();

    public Loan findByIdLoan(long id);

    public List<Loan> getAllLoan();

    public void saveLoan(Loan loan);

}
