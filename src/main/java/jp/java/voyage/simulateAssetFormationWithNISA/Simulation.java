package jp.java.voyage.simulateAssetFormationWithNISA;

import jp.java.voyage.simulateAssetFormationWithNISA.HomeController.SimulationParams;

import java.util.*;

public class Simulation {
    static int simuNum = 10000;
    static List<List<Double>> simuArr = new ArrayList<>();
    static List<List<List<Double>>> portfolio = new ArrayList<>();
    static List<List<Double>> VaR = new ArrayList<>();
    static List<Double> top5Percent = new ArrayList<>();
    static List<Double> expectedAverage = new ArrayList<>();
    static List<Double> bottom5Percent = new ArrayList<>();
    static List<Double> noOperation = new ArrayList<>();
    public static List<List<Double>> getValuationData(List<SimulationParams> params) {
        Random random = new Random();

        for (SimulationParams param : params) {
            int monthCount = (65 - param.startAge()) * 12; // 運用月数
            double expectedRateOfReturn = param.expectedRateOfReturn() / 100; // 小数
            double volatility = param.volatility() / 100; //小数

            // N回シミュレーション
            for (int n = 0; n < simuNum; n++) {
                List<Double> scenario = new ArrayList<>();
                scenario.add(param.initialValue() + param.monthlySavings());
                for (int i = 1; i < monthCount; i++) {
                    double delta = scenario.get(i - 1) * (expectedRateOfReturn / 12 + volatility * random.nextGaussian() / Math.sqrt(12) + 0); // 増分
                    scenario.add(scenario.get(i - 1) + delta + param.monthlySavings());
                }
                simuArr.add(scenario);
            }
            System.out.println(simuArr);

            // VaRのシナリオ作成
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
                expectedAverage.add(average);

                // 上位5％、下位5％
                Collections.sort(monthlyValue);
                top5Percent.add(monthlyValue.get(simuNum - (simuNum / 20)));
                bottom5Percent.add(monthlyValue.get(simuNum / 20));

                // 運用なし
                if (i == 0) {
                    noOperation.add(param.monthlySavings());
                } else {
                    noOperation.add(noOperation.get(i - 1) + param.monthlySavings());
                }
            }

            VaR.add(top5Percent);
            VaR.add(expectedAverage);
            VaR.add(bottom5Percent);
            VaR.add(noOperation);

            System.out.println(VaR);
        }

        return VaR;
    }

    public static List<String> getAgeCountList(List<SimulationParams> params) {
        List<String> monthCountList = new ArrayList<>();
        for (SimulationParams param : params) {
            int monthCount = (65 - param.startAge()) * 12; // 運用月数
            int age = param.startAge();
            for (int i = 1; i < monthCount+1; i++) {
                if (i % 12 == 0) {
                    age++;
                }
                monthCountList.add(age + "歳");
            }
        }
        System.out.println(monthCountList);

        return monthCountList;
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
        System.out.println(suggestedMax);

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
        } else {
            stepSize = 100000;
        }
        System.out.println(stepSize);

        return stepSize;
    }
}
