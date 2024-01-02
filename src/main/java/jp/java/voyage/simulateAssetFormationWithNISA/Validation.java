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

    public void typeValid(String expectedRateOfReturn, String volatility, String startAge, String monthlySavings, String initialValue, lifeEventStr lifeEventStr, lifeEventValidation lev, advancedSettingStr advancedSettingStr, advancedSettingValidation asv) {
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
        Matcher matcher10 = pattern2.matcher(lifeEventStr.lifeEventAge3);
        Matcher matcher11 = pattern1.matcher(lifeEventStr.requiredFunds3);
        Matcher matcher12 = pattern2.matcher(lifeEventStr.lifeEventAge4);
        Matcher matcher13 = pattern1.matcher(lifeEventStr.requiredFunds4);
        Matcher matcher14 = pattern2.matcher(lifeEventStr.lifeEventAge5);
        Matcher matcher15 = pattern1.matcher(lifeEventStr.requiredFunds5);
        Matcher matcher16 = pattern2.matcher(advancedSettingStr.requestAnnualChangeMoney);
        Matcher matcher17 = pattern2.matcher(advancedSettingStr.requestEndingAge);

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
        if (!(matcher10.matches())&& !(lifeEventStr.lifeEventAge3.equals(""))) {
            lev.setLifeEventAge3Error("0～64を入力");
        }
        if (!(matcher11.matches()) && !(lifeEventStr.requiredFunds3.equals(""))) {
            lev.setRequiredFunds3Error("半角数字を入力");
        }
        if (!(matcher12.matches())&& !(lifeEventStr.lifeEventAge4.equals(""))) {
            lev.setLifeEventAge4Error("0～64を入力");
        }
        if (!(matcher13.matches()) && !(lifeEventStr.requiredFunds4.equals(""))) {
            lev.setRequiredFunds4Error("半角数字を入力");
        }
        if (!(matcher14.matches())&& !(lifeEventStr.lifeEventAge5.equals(""))) {
            lev.setLifeEventAge5Error("0～64を入力");
        }
        if (!(matcher15.matches()) && !(lifeEventStr.requiredFunds5.equals(""))) {
            lev.setRequiredFunds5Error("半角数字を入力");
        }
        if (!(matcher16.matches()) && !(advancedSettingStr.requestAnnualChangeMoney.equals(""))) {
            asv.setAnnualChangeMoneyError("半角数字を入力");
        }
        if (!(matcher17.matches())&& !(advancedSettingStr.requestEndingAge.equals(""))) {
            asv.setEndingAgeError("0～64を入力");
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
