package com.mindhub.homebanking.utils;

public final class CardsUtils {

    private CardsUtils() {

    }

    public static String getCardNumber() {
        String cardNumber = getRandomNumber(1000, 10000)+"-"+getRandomNumber(1000, 10000)+"-"+getRandomNumber(1000, 10000)+"-"+getRandomNumber(1000, 10000);
        return cardNumber;
    }

    public static int getCvv() {
        int cvv = getRandomNumber(1, 1000);
        return cvv;
    }



    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
