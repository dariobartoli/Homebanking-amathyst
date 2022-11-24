package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountServices {

    public void saveAccount(Account account);

    public List<AccountDTO> getAllAccountDTO();

    public AccountDTO getAccountDTO(long id);

    public Account findByNumber(String number);

}
