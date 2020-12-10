package banking;

import java.util.Scanner;

public class BankingClient {

    private static final Scanner scanner = new Scanner(System.in);
    private final BankingSystem bankingSystem;

    private String creditCardNumber = null;

    public BankingClient(BankingSystem bankingSystem) {
        this.bankingSystem = bankingSystem;
    }

    public void createAccount() {
        OperationResult res = bankingSystem.createAccount();
        System.out.println();
        System.out.print(res.statusText);
    }

    public void logIntoAccount() {
        System.out.println();
        System.out.println("Enter your card number:");
        String creditCardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        OperationResult res = bankingSystem.login(creditCardNumber, pin);
        if (res.isFailure()) {
            System.out.println(res.statusText);
            return;
        }
        setCreditCardNumber(creditCardNumber);
        System.out.println();
        System.out.println("You have successfully logged in!");
    }

    public void logOut() {
        bankingSystem.logOut(creditCardNumber);
        creditCardNumber = null;
        System.out.println();
        System.out.println("You have successfully logged out!");
    }

    public void showBalance() {
        System.out.println();
        int balance = bankingSystem.getBalance(creditCardNumber);
        System.out.println("Balance: " + balance);
    }

    public void addIncome() {
        System.out.println();
        System.out.println("Enter income:");
        int income = Integer.parseInt(scanner.nextLine());
        OperationResult res = bankingSystem.addIncome(creditCardNumber, income);
        if (res.isSuccess()) {
            System.out.println("Income was added!");
        } else {
            System.out.println(res.statusText);
        }
    }

    public void doTransfer() {
        System.out.println();
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String destinationCardNumber = scanner.nextLine();
        if (!Luhn.isValidChecksum(destinationCardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        }
        if (!bankingSystem.accountExists(destinationCardNumber)) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int amount = Integer.parseInt(scanner.nextLine());
        OperationResult res = bankingSystem.doTransfer(creditCardNumber, destinationCardNumber, amount);
        if (res.isSuccess()) {
            System.out.println("Success!");
        } else {
            System.out.println(res.statusText);
        }
    }

    public void closeAccount() {
        OperationResult res = bankingSystem.closeAccount(creditCardNumber);
        if (res.isSuccess()) {
            System.out.println("The account has been closed!");
        } else {
            System.out.println(res.statusText);
        }
    }

    public boolean isLoggedIn() {
        return creditCardNumber != null && bankingSystem.isLoggedIn(creditCardNumber);
    }

    private void setCreditCardNumber(String account) {
        creditCardNumber = account;
    }

}
