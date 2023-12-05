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

    public void typeValid(String expectedRateOfReturn, String volatility, String startAge, String monthlySavings, String initialValue, lifeEventStr lifeEventStr, lifeEventValidation lev) {
        Pattern pattern1 = Pattern.compile("^[+-]?[0-9]+$|^[+-]?[0-9]+\\.[0-9]+$"); // 整数or小数
        Pattern pattern2 = Pattern.compile("^[+-]?[0-9]+$"); // 整数
        Matcher matcher1 = pattern1.matcher(expectedRateOfReturn);
        Matcher matcher2 = pattern1.matcher(volatility);
        Matcher matcher3 = pattern2.matcher(startAge);
        Matcher matcher4 = pattern1.matcher(monthlySavings);
        Matcher matcher5 = pattern1.matcher(initialValue);
        Matcher matcher6 = pattern2.matcher(lifeEventStr.lifeEventAge1);
        Matcher matcher7 = pattern1.matcher(lifeEventStr.requiredFunds1);
        Matcher matcher8 = pattern2.matcher(lifeEventStr.lifeEventAge2);
        Matcher matcher9 = pattern1.matcher(lifeEventStr.requiredFunds2);

        if (!(matcher1.matches())) {
            this.expectedRateOfReturnError = "半角数字を入力";
        }
        if (!(matcher2.matches())) {
            this.volatilityError = "半角数字を入力";
        }
        if (!(matcher3.matches())) {
            this.startAgeError = "0～64を入力";
        }
        if (!(matcher4.matches())) {
            this.monthlySavingsError = "半角数字を入力";
        }
        if (!(matcher5.matches())) {
            this.initialValueError = "半角数字を入力";
        }
        if (!(matcher6.matches()) && !(lifeEventStr.lifeEventAge1.equals(""))) {
            lev.setLifeEventAge1Error("0～64を入力");
        }
        if (!(matcher7.matches()) && !(lifeEventStr.requiredFunds1.equals(""))) {
            lev.setRequiredFunds1Error("半角数字を入力");
        }
        if (!(matcher8.matches())&& !(lifeEventStr.lifeEventAge2.equals(""))) {
            lev.setLifeEventAge2Error("0～64を入力");
        }
        if (!(matcher9.matches()) && !(lifeEventStr.requiredFunds2.equals(""))) {
            lev.setRequiredFunds2Error("半角数字を入力");
        }
    }

    public void ageValid(int startAge) {
//        Pattern pattern = Pattern.compile("[0-9]|[1-5][0-9]|6[0-4]"); // 0～64
//        Matcher matcher1 = pattern.matcher(startAge);
//        if (!(matcher1.matches())) {
//            this.startAgeError = "0～64を入力してください";
//        }
    }
}
