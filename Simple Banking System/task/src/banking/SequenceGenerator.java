package banking;

import java.util.Random;

public class SequenceGenerator {

    private static final Random randomGen = new Random();
    private static final int ACCOUNT_NUMBER_LENGTH = 9;
    private static final int PIN_LENGTH = 4;
    private static final String BIN = "400000";

    public static String generate(int length) {
        char[] sequence = new char[length];
        for (int i = 0; i < length; i++) {
            sequence[i] = (char) ('0' + randomGen.nextInt(10));
        }
        return new String(sequence);
    }

    public static String generateCreditCardNumber() {
        String accountNumber = SequenceGenerator.generate(ACCOUNT_NUMBER_LENGTH);
        String withoutChecksum = String.format("%s%s", BIN, accountNumber);
        char checksum = Luhn.computeChecksum(withoutChecksum);
        String cardNumber = String.format("%s%c", withoutChecksum, checksum);
        if (!Luhn.isValidChecksum(cardNumber)) {
            throw new RuntimeException("Something went wrong...");
        }
        return cardNumber;
    }

    public static String generatePin() {
        return SequenceGenerator.generate(PIN_LENGTH);
    }
}
