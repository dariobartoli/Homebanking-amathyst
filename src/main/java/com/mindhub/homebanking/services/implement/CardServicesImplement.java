package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServicesImplement implements CardServices {

    @Autowired
    private CardRepository cardRepository;


    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card getCard(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public void deleteCard(Card card) {
        cardRepository.delete(card);
    }

    @Override
    public Card getCardById(long id) {
        return cardRepository.findById(id).orElse(null);
    }

}
