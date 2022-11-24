package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PdfDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.CardServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.services.TransactionServices;
import com.mindhub.homebanking.utils.PDFMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionServices transactionServices;
    @Autowired
    private AccountServices accountServices;
    @Autowired
    private ClientServices clientServices;

    @Autowired
    private CardServices cardServices;



    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam Double amount, @RequestParam String description,
                                         @RequestParam String accountO, @RequestParam String accountD, Authentication authentication){

        Client clientCurrent = clientServices.findByEmail((authentication.getName()));
        Account accountOrigin = accountServices.findByNumber(accountO);
        Account accountDestiny = accountServices.findByNumber(accountD);
        Set<Account> accountExist = clientCurrent.getAccounts().stream().filter(account -> account.getNumber().equals(accountO)).collect(Collectors.toSet());

        if (clientCurrent != null){
            if (amount <= 0) {
                return new ResponseEntity<>("Monto vacio", HttpStatus.FORBIDDEN);
            }
            if (description.isEmpty()) {
                return new ResponseEntity<>("Descripción vacia", HttpStatus.FORBIDDEN);
            }
            if (accountO.isEmpty()) {
                return new ResponseEntity<>("Número de cuenta de origen vacio", HttpStatus.FORBIDDEN);
            }
            if (accountD.isEmpty()) {
                return new ResponseEntity<>("Número de cuenta de destino vacio", HttpStatus.FORBIDDEN);
            }
            if (accountO.equals(accountD)) {
                return new ResponseEntity<>("Número de cuenta iguales", HttpStatus.FORBIDDEN);
            }
            if (accountOrigin == null){
                return new ResponseEntity<>("Cuenta inexistente", HttpStatus.FORBIDDEN);
            }
            if (accountExist.isEmpty()){
                return new ResponseEntity<>("Cuenta no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
            }
            if (accountDestiny == null){
                return new ResponseEntity<>("Cuenta de destino inexistente", HttpStatus.FORBIDDEN);
            }
            if (accountServices.findByNumber(accountO).getBalance() < amount){
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
            }


            Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, -amount, description+" para "+ accountDestiny.getNumber(), LocalDateTime.now());
            Transaction transactionDestiny = new Transaction(TransactionType.CREDIT, amount, description+" de "+ accountOrigin.getNumber(), LocalDateTime.now());

            accountOrigin.addTransaction(transactionOrigin);
            accountDestiny.addTransaction(transactionDestiny);

            accountOrigin.setBalance(accountOrigin.getBalance() + transactionOrigin.getAmount());
            accountDestiny.setBalance(accountDestiny.getBalance() + transactionDestiny.getAmount());

            transactionServices.saveTransaction(transactionOrigin);
            transactionServices.saveTransaction(transactionDestiny);

            accountServices.saveAccount(accountOrigin);
            accountServices.saveAccount(accountDestiny);

            return new ResponseEntity<>("Transacción realizada con éxito",HttpStatus.CREATED);
        }else return new ResponseEntity<>("Cliente no autenticado", HttpStatus.FORBIDDEN);

    }

    @PostMapping("/api/transactions/pdf")
    public ResponseEntity<?> getPdfTransactions(@RequestBody PdfDTO pdfDTO, Authentication authentication) throws Exception {

        Client clientCurrent = clientServices.findByEmail(authentication.getName());
        Account account = accountServices.findByNumber(pdfDTO.getAccountNumber());

        if (clientCurrent != null){
            if (account == null)
                return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);

            if ( pdfDTO.getDateFrom() == null || pdfDTO.getDateTo() == null)
                return new ResponseEntity<>("Fecha invalida", HttpStatus.FORBIDDEN);

            if (pdfDTO.getDateFrom().isAfter(pdfDTO.getDateTo()))//isAfter es un metodo que se usa para comparar fechas.
                return new ResponseEntity<>("Fechas invalidas", HttpStatus.FORBIDDEN);

            Set<Transaction> transactions = transactionServices.getTransactionByDate(pdfDTO.getDateFrom(), pdfDTO.getDateTo());
            PDFMethod.createPDF(transactions);

            return new ResponseEntity<>("PDF created", HttpStatus.CREATED);


        }
            return new ResponseEntity<>("cliente no autenticado", HttpStatus.FORBIDDEN);

    }


}
