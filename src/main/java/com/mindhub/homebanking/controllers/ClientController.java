package com.mindhub.homebanking.controllers;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientServices clientServices;
    @Autowired
    private AccountServices accountServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/clients")

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Nombre vacio", HttpStatus.FORBIDDEN);
        }
        if ( lastName.isEmpty()) {
            return new ResponseEntity<>("Apellido vacio", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("Email vacio", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("Contraseña vacio", HttpStatus.FORBIDDEN);
        }

        if (clientServices.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Email ya en uso", HttpStatus.FORBIDDEN);

        }


        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientServices.saveClient(client);
        Account newAccount = new Account("VIN-"+getRandomNumber(10000000, 100000000), LocalDateTime.now(), 0.0, AccountType.CURRENT, true);
        client.addAccount(newAccount);
        accountServices.saveAccount(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients")
    public List<ClientDTO> getAll(){
        return clientServices.getAll();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){
        return clientServices.getClient(id);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        return new ClientDTO(clientServices.findByEmail(authentication.getName()));
    }
    @PatchMapping("/clients/current/changepassword")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestParam String password){
        Client clientcurrent = clientServices.findByEmail(authentication.getName());
        clientcurrent.setPassword(passwordEncoder.encode(password));
        clientServices.saveClient(clientcurrent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


}