package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Objects;

public class CardsStorage {
    private static final SQLiteDataSource dataSource = new SQLiteDataSource();

    public static void initialize(String dataBaseName) {
        dataSource.setUrl("jdbc:sqlite:" + dataBaseName);

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER DEFAULT 0)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCard(Card card) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement("INSERT INTO card (number, pin, balance) " +
                    "VALUES (?, ?, ?);")) {
                prepareStatement.setString(1, card.getNumber());
                prepareStatement.setString(2, card.getPin());
                prepareStatement.setInt(3, card.getBalance());
                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCard(Card card) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement("UPDATE card " +
                    "SET pin = ?," +
                    " balance = ? " +
                    "WHERE number = ?")) {
                prepareStatement.setString(1, card.getPin());
                prepareStatement.setInt(2, card.getBalance());
                prepareStatement.setString(3, card.getNumber());
                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCard(Card card) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement prepareStatement = connection.prepareStatement("DELETE FROM card " +
                    "WHERE number = ?;")) {
                prepareStatement.setString(1, card.getNumber());
                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Card getCardByNumber(String cardNumber) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM card " +
                    "WHERE number = ?")) {
                prepareStatement.setString(1, cardNumber);
                try (ResultSet result = prepareStatement.executeQuery()) {
                    String pin = result.getString(3);
                    int balance = result.getInt(4);
                    connection.commit();
                    return new Card(cardNumber, pin, balance);
                }
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isCardExists(String cardNumber) {
        return getCardByNumber(cardNumber) != null;
    }

    public static boolean authorizeCard(String cardNumber, String pin) {
        Card card = getCardByNumber(cardNumber);

        return card != null && Objects.equals(card.getPin(), pin);
    }
}
