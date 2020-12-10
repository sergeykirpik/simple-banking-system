package banking;

import java.util.Scanner;

public class Main {
    private static final int CREATE_ACCOUNT = 1;
    private static final int LOG_INTO_ACCOUNT = 2;

    private static final int SHOW_BALANCE = 1;
    public static final int ADD_INCOME = 2;
    public static final int DO_TRANSFER = 3;
    public static final int CLOSE_ACCOUNT = 4;
    private static final int LOG_OUT = 5;

    private static final int EXIT = 0;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length != 2 || !args[0].equalsIgnoreCase("-fileName")) {
            System.out.println("Usage: -fileName <db-file>");
            System.exit(1);
        }
        String dbFileName = args[1];
        BankingSystem bankingSystem = new BankingSystem(dbFileName);
        BankingClient bankingClient = new BankingClient(bankingSystem);
        //noinspection InfiniteLoopStatement
        while (true) {
            if (bankingClient.isLoggedIn()) {
                loggedInMenu(bankingClient);
            } else {
                mainMenu(bankingClient);
            }
            System.out.println();
        }
    }

    private static void mainMenu(BankingClient bankingClient) {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        String input = scanner.nextLine();
        if (!input.matches("\\d")) {
            System.out.println();
            System.out.println("Wrong input, try again...");
            return;
        }
        int command = Integer.parseInt(input);
        switch (command) {
            case CREATE_ACCOUNT:
                bankingClient.createAccount();
                break;
            case LOG_INTO_ACCOUNT:
                bankingClient.logIntoAccount();
                break;
            case EXIT:
                System.exit(0);
        }
    }

    private static void loggedInMenu(BankingClient bankingClient) {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
        String input = scanner.nextLine();
        if (!input.matches("\\d")) {
            System.out.println();
            System.out.println("Wrong input, try again...");
            return;
        }
        int command = Integer.parseInt(input);
        switch (command) {
            case SHOW_BALANCE:
                bankingClient.showBalance();
                break;
            case ADD_INCOME:
                bankingClient.addIncome();
                break;
            case DO_TRANSFER:
                bankingClient.doTransfer();
                break;
            case CLOSE_ACCOUNT:
                bankingClient.closeAccount();
                break;
            case LOG_OUT:
                bankingClient.logOut();
                break;
            case EXIT:
                System.exit(0);
        }
    }
}