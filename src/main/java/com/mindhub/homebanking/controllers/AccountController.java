package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountServices;
import com.mindhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountServices accountServices;
    @Autowired
    private ClientServices clientServices;

    @GetMapping("/accounts")
    public List<AccountDTO> getAll(){
        return accountServices.getAllAccountDTO();
    }

    @GetMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountServices.getAccountDTO(id);
    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> registerAccount(Authentication authentication, @RequestParam AccountType type)
    {
        Client clientCurrent = clientServices.findByEmail(authentication.getName());
        Set<Account> accounts = clientCurrent.getAccounts().stream().filter(account -> account.isActive()).collect(Collectors.toSet());
        if (clientCurrent != null){
            if(accounts.size() == 3){
                return new ResponseEntity<>("No puede crear mas cuentas", HttpStatus.FORBIDDEN);
            }
            else{
                Account newAccount = new Account("VIN-"+getRandomNumber(10000000, 100000000), LocalDateTime.now(), 0.0, type, true);
                clientCurrent.addAccount(newAccount);
                accountServices.saveAccount(newAccount);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam String number){
        Account deletedAccount = accountServices.findByNumber(number);
        if (deletedAccount.getBalance() > 0) {
            return new ResponseEntity<>("La cuenta tiene saldo mayor a 0", HttpStatus.FORBIDDEN);
        }
        deletedAccount.setActive(false);
        accountServices.saveAccount(deletedAccount);
        return new ResponseEntity<>("Cuenta eliminada", HttpStatus.OK);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


}
