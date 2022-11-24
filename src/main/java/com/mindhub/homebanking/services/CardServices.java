package com.mindhub.homebanking.services;


import com.mindhub.homebanking.models.Card;

public interface CardServices {

    public void saveCard(Card card);

    public Card getCard(String number);

    public void deleteCard(Card card);

    public Card getCardById(long id);

}
