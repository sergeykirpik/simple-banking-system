package banking;


public class Luhn {
    private static final int CREDIT_CARD_NUMBER_LENGTH = 16;

    public static char computeChecksum(String creditCardNumber) {
        int sum = 0;
        for (int i = 0; i < CREDIT_CARD_NUMBER_LENGTH - 1; i++) {
           int num = Character.digit(creditCardNumber.charAt(i), 10);
           if (i % 2 == 0) {
               num *= 2;
           }
           if (num > 9) {
               num -= 9;
           }
           sum += num;
        }
        int checksum =  (10 - (sum % 10)) % 10;
        return Character.forDigit(checksum, 10);
    }

    public static boolean isValidChecksum(String creditCardNumber) {
        if (creditCardNumber.length() < CREDIT_CARD_NUMBER_LENGTH) {
            return false;
        }
        char lastDigit = creditCardNumber.charAt(CREDIT_CARD_NUMBER_LENGTH - 1);
        return lastDigit == computeChecksum(creditCardNumber);
    }
}
