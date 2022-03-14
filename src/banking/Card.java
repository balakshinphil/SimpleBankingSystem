package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Card {
    private final String number;
    private final String pin;
    private int balance;

    public Card(String number, String pin, int balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    private void setBalance(int balance) {
        this.balance = balance;
    }

    public void income(int amount) {
        setBalance(balance + amount);
    }

    public static Card createNewCard() {
        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (CardsStorage.isCardExists(cardNumber));

        String pin = generatePin();

        return new Card(cardNumber, pin, 0);
    }

    private static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder("400000");
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            cardNumber.append(random.nextInt(10));
        }
        cardNumber.append(generateChecksum(cardNumber.toString()));

        return cardNumber.toString();
    }

    public static String generateChecksum(String tempCardNumber) {
        List<Long> numberDigits = getDigitsOfNumber(Long.parseLong(tempCardNumber));
        long sum = 0;
        for (int i = 0; i < numberDigits.size(); i++) {
            long digit = numberDigits.get(i);
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }

        int checksum = 0;
        while (sum % 10 != 0) {
            checksum++;
            sum++;
        }

        return String.valueOf(checksum);
    }

    private static List<Long> getDigitsOfNumber(long number) {
        List<Long> digits = new ArrayList<>();
        while (number > 0) {
            digits.add(number % 10);
            number /= 10;
        }

        return digits;
    }

    private static String generatePin() {
        Random random = new Random();
        StringBuilder pin = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            pin.append(random.nextInt(10));
        }

        return pin.toString();
    }
}
