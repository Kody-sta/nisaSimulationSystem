package jp.java.voyage.simulateAssetFormationWithNISA;

import jp.java.voyage.simulateAssetFormationWithNISA.HomeController.SimulationParams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Simulation {
    static int simuNum = 10000;

    public static List<List<Double>> getValuationData(SimulationParams params) {
        List<List<Double>> simuArr = new ArrayList<>();
        Random random = new Random();

        int monthCount = (65 - params.startAge()) * 12; // 運用月数
        double expectedRateOfReturn = params.expectedRateOfReturn() / 100; // 小数
        double volatility = params.volatility() / 100; //小数
        int monthOfLifeEvent1 = (params.lifeEventParams().lifeEventAge1() - params.startAge()) * 12; // イベント発生月
        int monthOfLifeEvent2 = (params.lifeEventParams().lifeEventAge2() - params.startAge()) * 12;
        int monthOfLifeEvent3 = (params.lifeEventParams().lifeEventAge3() - params.startAge()) * 12;
        int monthOfLifeEvent4 = (params.lifeEventParams().lifeEventAge4() - params.startAge()) * 12;
        int monthOfLifeEvent5 = (params.lifeEventParams().lifeEventAge5() - params.startAge()) * 12;

        // N回シミュレーション
        for (int n = 0; n < simuNum; n++) {
            List<Double> scenario = new ArrayList<>();
            double totalReserveAmount = params.monthlySavings();
            scenario.add(params.initialValue() + params.monthlySavings()); // 積立1か月目
            for (int i = 1; i < monthCount; i++) {
                double delta = scenario.get(i - 1) * (expectedRateOfReturn / 12 + volatility * random.nextGaussian() / Math.sqrt(12) + 0); // 増分
                totalReserveAmount += params.monthlySavings();
                if (i == monthOfLifeEvent1) { // イベント①発生
                    if (totalReserveAmount <= 1800) { // 積立上限判定
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds1());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds1());
                    }
                } else if (i == monthOfLifeEvent2) {
                    if (totalReserveAmount <= 1800) {
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds2());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds2());
                    }
                } else if (i == monthOfLifeEvent3) {
                    if (totalReserveAmount <= 1800) {
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds3());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds3());
                    }

                } else if (i == monthOfLifeEvent4) {
                    if (totalReserveAmount <= 1800) {
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds4());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds4());
                    }
                } else if (i == monthOfLifeEvent5) {
                    if (totalReserveAmount <= 1800) {
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds5());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds5());
                    }
                } else {
                    if (totalReserveAmount <= 1800) {
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta);
                    }
                }
            }
            simuArr.add(scenario);
        }
//        System.out.println(simuArr);

        // VaRのシナリオ作成
        List<List<Double>> VaR = new ArrayList<>();
        List<Double> top5Percent = new ArrayList<>();
        List<Double> expectedAverage = new ArrayList<>();
        List<Double> bottom5Percent = new ArrayList<>();
        List<Double> noOperation = new ArrayList<>();
        double totalReserveAmount = params.monthlySavings();
        for (int i = 0; i < monthCount; i++) {
            List<Double> monthlyValue = new ArrayList<>();
            for (List<Double> sce : simuArr) {
                monthlyValue.add(sce.get(i));
            }

            // 予想平均
            double average = monthlyValue.stream()
                    .mapToDouble(a -> a)
                    .average()
                    .orElse(0);
            BigDecimal bdAverage = getRoundingOffNum(average);
            average = Double.parseDouble(String.valueOf(bdAverage));
            expectedAverage.add(average);

            // 上位5％、下位5％
            Collections.sort(monthlyValue);
            BigDecimal bdTop = getRoundingOffNum(monthlyValue.get(simuNum - (simuNum / 20)));
            double top5 = Double.parseDouble(String.valueOf(bdTop));
            top5Percent.add(top5);

            BigDecimal bdBottom = getRoundingOffNum(monthlyValue.get(simuNum / 20));
            double bottom5 = Double.parseDouble(String.valueOf(bdBottom));
            bottom5Percent.add(bottom5);

            // 運用なし
            if (i == 0) {
                noOperation.add(params.monthlySavings());
            } else {
                totalReserveAmount += params.monthlySavings();
                if (i == monthOfLifeEvent1) { // イベント①発生
                    if (totalReserveAmount <= 1800) {
                        noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds1());
                    } else {
                        noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds1());
                    }
                } else if (i == monthOfLifeEvent2) {
                    if (totalReserveAmount <= 1800) {
                        noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds2());
                    } else {
                        noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds2());
                    }
                } else if (i == monthOfLifeEvent3) {
                    if (totalReserveAmount <= 1800) {
                        noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds3());
                    } else {
                        noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds3());
                    }
                } else if (i == monthOfLifeEvent4) {
                    if (totalReserveAmount <= 1800) {
                        noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds4());
                    } else {
                        noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds4());
                    }
                } else if (i == monthOfLifeEvent5) {
                    if (totalReserveAmount <= 1800) {
                        noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds5());
                    } else {
                        noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds5());
                    }
                } else {
                    if (totalReserveAmount <= 1800) {
                        noOperation.add(noOperation.get(i - 1) + params.monthlySavings());
                    } else {
                        noOperation.add(noOperation.get(i - 1));
                    }
                }
            }
        }

        VaR.add(top5Percent);
        VaR.add(expectedAverage);
        VaR.add(bottom5Percent);
        VaR.add(noOperation);

//        System.out.println(VaR);

        return VaR;
    }

    public static List<String> getAgeCountList(SimulationParams params) {
        List<String> ageCountList = new ArrayList<>();
        int monthCount = (65 - params.startAge()) * 12; // 運用月数
        int age = params.startAge();
        for (int i = 1; i < monthCount+1; i++) {
            if (i % 12 == 0) {
                age++;
            }
            ageCountList.add(age + "歳");
        }
//        System.out.println(monthCountList);

        return ageCountList;
    }

    public static double getSuggestedMax(List<List<Double>> valuationData) {
        double suggestedMax = 0;
        for (List<Double> data : valuationData) {
            for (double value : data) {
                if (value > suggestedMax) {
                    suggestedMax = value;
                }
            }
        }
//        System.out.println(suggestedMax);

        return suggestedMax;
    }

    public static int getStepSize(double suggestedMax) {
        int stepSize;
        if (suggestedMax < 1000) {
            stepSize = 100;
        } else if (suggestedMax < 20000) {
            stepSize = 1000;
        } else if (suggestedMax < 50000) {
            stepSize = 2000;
        } else if (suggestedMax < 100000) {
            stepSize = 5000;
        } else if (suggestedMax < 200000) {
            stepSize = 10000;
        } else if (suggestedMax < 500000) {
            stepSize = 20000;
        } else if (suggestedMax < 1000000) {
            stepSize = 50000;
        } else if (suggestedMax < 2000000) {
            stepSize = 100000;
        } else if (suggestedMax < 5000000) {
            stepSize = 200000;
        } else if (suggestedMax < 10000000) {
            stepSize = 500000;
        } else {
            stepSize = 1000000;
        }
//        System.out.println(stepSize);

        return stepSize;
    }

    private static BigDecimal getRoundingOffNum(double num) {
        BigDecimal bdNum = new BigDecimal(num);
        return bdNum.setScale(2, RoundingMode.HALF_UP);
    }
}
