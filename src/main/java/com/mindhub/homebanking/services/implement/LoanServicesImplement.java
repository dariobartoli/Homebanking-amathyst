package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServicesImplement implements LoanServices {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<LoanDTO> getAllLoanDTO() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Override
    public Loan findByIdLoan(long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public List<Loan> getAllLoan() {
        return loanRepository.findAll();
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }


}
