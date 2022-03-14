package banking;

import java.util.Objects;
import java.util.Scanner;

public class Bank {
    private final Scanner scanner = new Scanner(System.in);
    private Card currentSessionCard;
    private int choice;

    public void run() {
        do {
            showMainMenuChoice();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    logIntoAccount();
                    break;
                default:
                    break;
            }

            if (choice == 0) {
                showBye();
            }
        } while (choice != 0);
    }

    private void showMainMenuChoice() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    private void createAccount() {
        Card card = Card.createNewCard();
        CardsStorage.addCard(card);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(card.getNumber());
        System.out.println("Your card PIN:");
        System.out.printf("%s%n%n", card.getPin());
    }

    private void logIntoAccount() {
        System.out.println("\nEnter your card number:");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();

        if (CardsStorage.authorizeCard(cardNumber, pin)) {
            currentSessionCard = CardsStorage.getCardByNumber(cardNumber);
            System.out.println("\nYou have successfully logged in!\n");
            accountMenu();
        }
    }

    private void accountMenu() {
        do {
            showAccountMenuChoice();
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    showAccountBalance();
                    break;
                case 2:
                    addIncomeToAccount();
                    break;
                case 3:
                    transferBalance();
                    break;
                case 4:
                    closeAccount();
                    break;
            }
        } while (choice != 0 && choice != 4 && choice != 5);
    }

    private void showAccountMenuChoice() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }

    private void showAccountBalance() {
        System.out.printf("%nBalance: %d%n%n", currentSessionCard.getBalance());
    }

    private void addIncomeToAccount() {
        System.out.println("\nEnter income:");
        int amount = scanner.nextInt();
        currentSessionCard.income(amount);
        CardsStorage.updateCard(currentSessionCard);
        System.out.println("Income was added!\n");
    }

    private void transferBalance() {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String cardNumber = scanner.next();
        if (isCardNumberEqualsCurrentSessionCardNumber(cardNumber)) {
            System.out.println("You can't transfer money to the same account!\n");
        } else if (!isCardNumberValid(cardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
        } else if (!CardsStorage.isCardExists(cardNumber)) {
            System.out.println("Such a card does not exist.\n");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            int amount = scanner.nextInt();
            if (amount <= currentSessionCard.getBalance()) {
                currentSessionCard.income(-amount);
                Card cardForTransfer = CardsStorage.getCardByNumber(cardNumber);
                assert cardForTransfer != null;
                cardForTransfer.income(amount);
                CardsStorage.updateCard(currentSessionCard);
                CardsStorage.updateCard(cardForTransfer);
                System.out.println("Success!\n");
            } else {
                System.out.println("Not enough money!\n");
            }
        }
    }

    private boolean isCardNumberEqualsCurrentSessionCardNumber(String cardNumber) {
        return Objects.equals(cardNumber, currentSessionCard.getNumber());
    }

    private boolean isCardNumberValid(String cardNumber) {
        String tempCardNumber = cardNumber.substring(0, cardNumber.length() - 1);
        return cardNumber.equals(tempCardNumber.concat((Card.generateChecksum(tempCardNumber))));
    }

    private void closeAccount() {
        CardsStorage.removeCard(currentSessionCard);
        System.out.println("\nThe account has been closed!\n");
    }

    private void showBye() {
        System.out.println("\nBye!");
    }
}
