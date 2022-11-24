package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanServices loanServices;
    @Autowired
    private ClientServices clientServices;
    @Autowired
    private AccountServices accountServices;
    @Autowired
    private ClientLoanServices clientLoanServices;
    @Autowired
    private TransactionServices transactionServices;

    @GetMapping("/loans")
    public List<LoanDTO> getAll(){
        return loanServices.getAllLoanDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<?> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        Client clientCurrent = clientServices.findByEmail(authentication.getName());
        Account accountDestiny = accountServices.findByNumber(loanApplicationDTO.getAccountDestiny());
        Loan loanExist = loanServices.findByIdLoan(loanApplicationDTO.getId());

        if(clientCurrent != null){

            if (loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getAmount().isNaN()){
                return new ResponseEntity<>("monto vacio", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getAccountDestiny().isEmpty()){
                return new ResponseEntity<>("cuenta de destino vacia", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getPayments() == 0){
                return new ResponseEntity<>("cuotas vacio", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getId() == null){
                return new ResponseEntity<>("id vacio", HttpStatus.FORBIDDEN);
            }


            if (loanExist == null){
                return new ResponseEntity<>("el prestamo no existe", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getAmount() > loanExist.getMaxAmount()){
                return new ResponseEntity<>("monto solicitado excede al prestamo", HttpStatus.FORBIDDEN);
            }
            if (!loanExist.getPayments().contains(loanApplicationDTO.getPayments())){
                return new ResponseEntity<>("la cuota no se encuentra entre las disponibles", HttpStatus.FORBIDDEN);
            }
            if (accountDestiny == null){
                return new ResponseEntity<>("la cuenta de destino no existe", HttpStatus.FORBIDDEN);
            }
            if (!clientCurrent.getAccounts().contains(accountDestiny)){
                return new ResponseEntity<>("la cuenta no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
            }
            if (clientCurrent.getloans().stream().filter(clientLoan -> clientLoan.getLoan().getName().equals(loanExist.getName())).toArray().length == 1){
                return new ResponseEntity<>("ya has solicitado este prestamo", HttpStatus.FORBIDDEN);
            }

            if (loanApplicationDTO.getId() == 16) {
                ClientLoan loan = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments(), clientCurrent, loanExist);
                clientLoanServices.saveClientLoan(loan);
            }

            if (loanApplicationDTO.getId() == 17) {
                ClientLoan loan = new ClientLoan(loanApplicationDTO.getAmount() * 1.10, loanApplicationDTO.getPayments(), clientCurrent, loanExist);
                clientLoanServices.saveClientLoan(loan);
            }
            if (loanApplicationDTO.getId() == 18) {
                ClientLoan loan = new ClientLoan(loanApplicationDTO.getAmount() * 1.15, loanApplicationDTO.getPayments(), clientCurrent, loanExist);
                clientLoanServices.saveClientLoan(loan);
            }
            Transaction transactionCreated = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), "Prestamo " + loanExist.getName() + " aprobado", LocalDateTime.now());
            accountDestiny.addTransaction(transactionCreated);
            accountDestiny.setBalance(accountDestiny.getBalance() + loanApplicationDTO.getAmount());
            transactionServices.saveTransaction(transactionCreated);
            accountServices.saveAccount(accountDestiny);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/admin/loans")
    public ResponseEntity<?> createTypeLoan(@RequestParam String name, @RequestParam Double maxAmount, @RequestParam List<Integer> payments, Authentication authentication){
        Client adminCurrent = clientServices.findByEmail(authentication.getName());
        if(adminCurrent != null){
            if(name.isEmpty()){
                return new ResponseEntity<>("Nombre vacio",HttpStatus.FORBIDDEN);
            }
            if(maxAmount <= 0){
                return new ResponseEntity<>("Monto vacio",HttpStatus.FORBIDDEN);
            }
            if (loanServices.getAllLoan().stream().map(loan -> loan.getName()).collect(Collectors.toSet()).contains(name)){
                return new ResponseEntity<>("Ya existe el nombre de este prestamo",HttpStatus.FORBIDDEN);
            }

            Loan loan = new Loan(name, maxAmount, payments);
            loanServices.saveLoan(loan);
            return new ResponseEntity<>("Tipo de prestamo creado", HttpStatus.CREATED);

        }
        return new ResponseEntity<>("Usuario no identificado", HttpStatus.FORBIDDEN);
    }
}
