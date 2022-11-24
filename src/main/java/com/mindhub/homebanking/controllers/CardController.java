package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardServices;
import com.mindhub.homebanking.services.ClientServices;
import com.mindhub.homebanking.utils.CardsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardServices cardServices;
    @Autowired
    private ClientServices clientServices;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardType type, @RequestParam CardColor color,
            Authentication authentication)
    {
        Client clientCurrent = clientServices.findByEmail(authentication.getName());
        Set<Card> cards = clientCurrent.getCards().stream().filter(card -> card.getType() == type).collect(Collectors.toSet());
        Set<Card> cardsActive = cards.stream().filter(card -> card.isActive()).collect(Collectors.toSet());
        Set<Card> cardsCredit = cardsActive.stream().filter(card -> card.getType() == CardType.CREDIT).collect(Collectors.toSet());
        Set<Card> cardsDebit = cardsActive.stream().filter(card -> card.getType() == CardType.DEBIT).collect(Collectors.toSet());
        Set<Card> cardsCreditColor = cardsCredit.stream().filter(card -> card.getColor().equals(color)).collect(Collectors.toSet());
        Set<Card> cardsDebitColor = cardsDebit.stream().filter(card -> card.getColor().equals(color)).collect(Collectors.toSet());

        String cardNumber = CardsUtils.getCardNumber();
        int cvv = CardsUtils.getCvv();

        if (clientCurrent != null){
            if (cardsActive.size() == 3){
                return new ResponseEntity<>("No se pueden solicitar mas de este tipo de tarjeta: "+ type, HttpStatus.FORBIDDEN);
            }
            if (type.equals(CardType.CREDIT)){
                if (cardsCreditColor.size() == 1){
                    return new ResponseEntity<>("No se pueden solicitar mas tarjetas de este color: "+ color, HttpStatus.FORBIDDEN);
                }
            }
            if (type.equals(CardType.DEBIT)){
                if (cardsDebitColor.size() == 1){
                    return new ResponseEntity<>("No se pueden solicitar mas tarjetas de este color: "+ color, HttpStatus.FORBIDDEN);
                }
            }


            if(color.toString().isEmpty()){
                return new ResponseEntity<>("Color de Tarjeta vacio", HttpStatus.FORBIDDEN);
            }
            if(type.toString().isEmpty()){
                return new ResponseEntity<>("Tipo de Tarjeta vacio", HttpStatus.FORBIDDEN);
            }

            Card card = new Card(clientCurrent.getFirstName()+" "+clientCurrent.getLastName(), type, color, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5), true);
            clientCurrent.addCard(card);
            cardServices.saveCard(card);
            return new ResponseEntity<>(HttpStatus.CREATED);


        }return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @DeleteMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(
            @RequestParam String number,
            Authentication authentication)
    {
        Client clientCurrent = clientServices.findByEmail(authentication.getName());
        Card cardSelect = cardServices.getCard(number);
        if (clientCurrent != null){
            cardServices.deleteCard(cardSelect);
            return new ResponseEntity<>("Tarjeta Eliminada", HttpStatus.OK);
        }

        return new ResponseEntity<>("No está autenticado", HttpStatus.FORBIDDEN);
    }

    @PatchMapping("/clients/current/delete/cards")
    public ResponseEntity<?> deleteCards(Authentication authentication, @RequestParam String number){
        Client clientCurrent = clientServices.findByEmail(authentication.getName());
        Card cardSelect = cardServices.getCard(number);
        if ( clientCurrent !=null){
            cardSelect.setActive(false);
            cardServices.saveCard(cardSelect);
            return new ResponseEntity<>(HttpStatus.CREATED);

        }
        return new ResponseEntity<>("No está autenticado", HttpStatus.FORBIDDEN);
    }







    public int getCvv() {
        int cvv = getRandomNumber(1, 1000);
        return cvv;
    }

    public String getCardNumber() {
        String cardNumber = getRandomNumber(1000, 10000)+"-"+getRandomNumber(1000, 10000)+"-"+getRandomNumber(1000, 10000)+"-"+getRandomNumber(1000, 10000);
        return cardNumber;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }



}
