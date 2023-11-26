package jp.java.voyage.simulateAssetFormationWithNISA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    String expectedRateOfReturnError;
    String volatilityError;
    String startAgeError;
    String monthlySavingsError;
    String initialValueError;
    public Validation() {
    }

    public void typeValid(String expectedRateOfReturn, String volatility, String startAge, String monthlySavings, String initialValue) {
//        Pattern pattern = Pattern.compile("^[+-]?[0-9]+\.[0-9]+([eE][+-]?[0-9]+)?$");
//        Pattern pattern1 = Pattern.compile("[0-9]|[1-5][0-9]|6[0-4]");
        Pattern pattern1 = Pattern.compile("^[+-]?[0-9]+$"); // 整数
        Matcher matcher1 = pattern1.matcher(expectedRateOfReturn);
        Matcher matcher2 = pattern1.matcher(volatility);
        Matcher matcher3 = pattern1.matcher(startAge);
        Matcher matcher4 = pattern1.matcher(monthlySavings);
        Matcher matcher5 = pattern1.matcher(initialValue);

        if (!(matcher1.matches())) {
            this.expectedRateOfReturnError = "半角数字を入力してください";
        }
        if (!(matcher2.matches())) {
            this.volatilityError = "半角数字を入力してください";
        }
        if (!(matcher3.matches())) {
            this.startAgeError = "0～64を入力してください";
        }
        if (!(matcher4.matches())) {
            this.monthlySavingsError = "半角数字を入力してください";
        }
        if (!(matcher5.matches())) {
            this.initialValueError = "半角数字を入力してください";
        }
    }
}
