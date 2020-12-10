package banking;

import banking.exception.AccessDeniedException;
import banking.repository.AccountRepository;
import banking.repository.SQLiteAccountRepository;

import static banking.SequenceGenerator.*;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class BankingSystem {

    private final AccountRepository accounts;

    private final Set<String> loggedInAccounts = new HashSet<>();

    public BankingSystem(String filename) {
        ConnectionManager connectionManager = new ConnectionManager(filename);
        accounts = new SQLiteAccountRepository(connectionManager);
    }

    public OperationResult createAccount() {
        String creditCardNumber = generateCreditCardNumber();
        while (accounts.accountExists(creditCardNumber)) {
            creditCardNumber = generateCreditCardNumber();
        }
        String pin = generatePin();
        try {
            accounts.create(creditCardNumber, pin);
        } catch (SQLException e) {
            return OperationResult.failure(e.getMessage());
        }

        String statusText =
            "Your card has been created\n" +
            "Your card number:\n" +
            creditCardNumber + '\n' +
            "Your card PIN:\n" +
            pin + '\n'
        ;
        return OperationResult.success(statusText);
    }

    public OperationResult closeAccount(String creditCardNumber) {
        if (!isLoggedIn(creditCardNumber)) {
            throw new AccessDeniedException();
        }
        logOut(creditCardNumber);
        try {
            accounts.close(creditCardNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            return OperationResult.failure(e.getMessage());
        }
        return OperationResult.success();
    }

    public OperationResult login(String creditCardNumber, String pin) {
        if (!accounts.accountExists(creditCardNumber)) {
            return OperationResult.failure("Wrong card number or PIN!");
        }
        Account account = accounts.get(creditCardNumber);
        if (!account.checkPin(pin)) {
            return OperationResult.failure("Wrong card number or PIN!");
        }
        loggedInAccounts.add(creditCardNumber);
        return OperationResult.success();
    }

    public void logOut(String creditCardNumber) {
        if (!isLoggedIn(creditCardNumber)) {
            throw new AccessDeniedException();
        }
        loggedInAccounts.remove(creditCardNumber);
    }

    public int getBalance(String creditCardNumber) {
        if (!isLoggedIn(creditCardNumber)) {
            throw new AccessDeniedException();
        }
        return accounts.getBalance(creditCardNumber);
    }

    public OperationResult addIncome(String account, int income) {
        try {
            accounts.incBalance(account, income);
        } catch (SQLException e) {
            return OperationResult.failure(e.getMessage());
        }
        return OperationResult.success();
    }

    public OperationResult doTransfer(String creditCardNumber, String destinationCardNumber, int amount) {
        if (!isLoggedIn(creditCardNumber)) {
            throw new AccessDeniedException();
        }
        if (!accounts.accountExists(destinationCardNumber)) {
            return OperationResult.failure("Such a card does not exist.");
        }
        int accountBalance = accounts.getBalance(creditCardNumber);
        if (accountBalance < amount) {
            return OperationResult.failure("Not enough money!");
        }
        try {
            accounts.doTransfer(creditCardNumber, destinationCardNumber,amount);
        } catch (SQLException e) {
            return OperationResult.failure(e.getMessage());
        }
        return OperationResult.success();
    }

    public boolean isLoggedIn(String creditCardNumber) {
        return loggedInAccounts.contains(creditCardNumber);
    }

    public boolean accountExists(String creditCardNumber) {
        return accounts.accountExists(creditCardNumber);
    }

}
