package banking.repository;

import banking.Account;
import banking.ConnectionManager;
import banking.repository.AccountRepository;

import java.sql.*;

public class SQLiteAccountRepository implements AccountRepository {

    private final ConnectionManager connectionManager;

    public SQLiteAccountRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        initDB();
    }

    private Connection connect() throws SQLException {
        return connectionManager.getConnection();
    }

    private void initDB() {
        String sqlCreateCardTable =
        "CREATE TABLE IF NOT EXISTS card (" +
        "   id INTEGER PRIMARY KEY," +
        "   number TEXT," +
        "   pin TEXT," +
        "   balance INTEGER DEFAULT 0" +
        ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sqlCreateCardTable);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize database.");
        }
    }

    public void create(String creditCardNumber, String pin) throws SQLException {
        String sqlInsertIntoCard = "INSERT INTO card (number, pin) VALUES (?, ?)";

        try (Connection connection = connect();
             PreparedStatement prep = connection.prepareStatement(sqlInsertIntoCard)) {

            prep.setString(1, creditCardNumber);
            prep.setString(2, pin);
            prep.executeUpdate();
        }
    }

    public int getBalance(String creditCardNumber) {
        String sqlGetBalance = "SELECT balance FROM card WHERE number = ?";
        try (Connection con = connect();
             PreparedStatement getBalance = con.prepareStatement(sqlGetBalance)) {

            getBalance.setString(1, creditCardNumber);
            try (ResultSet rs = getBalance.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("balance");
                }
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void incBalance(String creditCardNumber, int value) throws SQLException {
        String sqlUpdateBalance = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection con = connect();
             PreparedStatement updateBalance = con.prepareStatement(sqlUpdateBalance)) {

            updateBalance.setInt(1, value);
            updateBalance.setString(2, creditCardNumber);
            updateBalance.executeUpdate();
        }
    }

    public void doTransfer(String sourceCreditCardNumber, String destinationCardNumber, int amount) throws SQLException {
        String sqlDecrementSourceAccount = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String sqlIncrementDestinationAccount = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection con = connect()) {
            con.setAutoCommit(false);
            try (PreparedStatement decrementSource = con.prepareStatement(sqlDecrementSourceAccount);
                 PreparedStatement incrementDestination = con.prepareStatement(sqlIncrementDestinationAccount)) {

                decrementSource.setInt(1, amount);
                decrementSource.setString(2, sourceCreditCardNumber);
                decrementSource.executeUpdate();

                incrementDestination.setInt(1, amount);
                incrementDestination.setString(2, destinationCardNumber);
                incrementDestination.executeUpdate();

                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public void close(String creditCardNumber) throws SQLException {
        String sqlDeleteCard = "DELETE FROM card WHERE number = ?";
        try (Connection con = connect();
             PreparedStatement deleteCard = con.prepareStatement(sqlDeleteCard)) {

            deleteCard.setString(1, creditCardNumber);
            deleteCard.executeUpdate();
        }
    }

    public Account get(String creditCardNumber) {
        String sqlFindByCardNumber =
        "SELECT id, number, pin, balance FROM card " +
        "WHERE number = ?";

        Account account = null;
        try (Connection connection = connect();
             PreparedStatement prep = connection.prepareStatement(sqlFindByCardNumber)) {

            prep.setString(1, creditCardNumber);
            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    String pin = rs.getString("pin");
                    account = new  Account(creditCardNumber, pin);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return account;
    }

    public boolean accountExists(String creditCardNumber) {
        return get(creditCardNumber) != null;
    }

}