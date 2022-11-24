package com.mindhub.homebanking;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import com.mindhub.homebanking.utils.CardsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class cardUtilTest {


    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardsUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cvvIsCreated(){
        int cvv = CardsUtils.getCvv();
        assertThat(cvv,is(notNullValue()));
    }




}
