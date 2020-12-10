package banking.repository;

import banking.Account;

import java.sql.SQLException;

public interface AccountRepository {

    boolean accountExists(String creditCardNumber);

    Account get(String creditCardNumber);

    void incBalance(String creditCardNumber, int income) throws SQLException;

    void doTransfer(String creditCardNumber, String destinationCardNumber, int amount) throws SQLException;

    void close(String creditCardNumber) throws SQLException;

    void create(String creditCardNumber, String pin) throws SQLException;

    int getBalance(String creditCardNumber);
}
