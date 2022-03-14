package banking;

public class Main {
    public static void main(String[] args) {
        String dataBaseName = args[1];
        CardsStorage.initialize(dataBaseName);

        Bank bank = new Bank();
        bank.run();
    }
}
