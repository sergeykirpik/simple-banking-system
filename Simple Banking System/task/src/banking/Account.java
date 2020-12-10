package banking;

import java.util.Objects;

public class Account {

    private final String creditCardNumber;
    private final String creditCardPin;

    public Account(String creditCardNumber, String creditCardPin) {
        this.creditCardNumber = creditCardNumber;
        this.creditCardPin = creditCardPin;
    }

    @Override
    public String toString() {
        return "Account{" +
        "creditCardNumber='" + creditCardNumber + '\'' +
        ", creditCardPin='" + creditCardPin + '\'' +
        '}';
    }

    public boolean checkPin(String pin) {
        return Objects.equals(this.creditCardPin, pin);
    }

}