package jp.java.voyage.simulateAssetFormationWithNISA;

import jp.java.voyage.simulateAssetFormationWithNISA.HomeController.SimulationParams;

import java.util.*;

public class Simulation {
    static int simuNum = 10000;

    public static List<List<Double>> getValuationData(SimulationParams params) {
        List<List<Double>> simuArr = new ArrayList<>();
        Random random = new Random();

        int monthCount = (65 - params.startAge()) * 12; // 運用月数
        double expectedRateOfReturn = params.expectedRateOfReturn() / 100; // 小数
        double volatility = params.volatility() / 100; //小数

        // N回シミュレーション
        for (int n = 0; n < simuNum; n++) {
            List<Double> scenario = new ArrayList<>();
            scenario.add(params.initialValue() + params.monthlySavings());
            for (int i = 1; i < monthCount; i++) {
                double delta = scenario.get(i - 1) * (expectedRateOfReturn / 12 + volatility * random.nextGaussian() / Math.sqrt(12) + 0); // 増分
                scenario.add(scenario.get(i - 1) + delta + params.monthlySavings());
            }
            simuArr.add(scenario);
        }
        System.out.println(simuArr);

        // VaRのシナリオ作成
        List<List<Double>> VaR = new ArrayList<>();
        List<Double> top5Percent = new ArrayList<>();
        List<Double> expectedAverage = new ArrayList<>();
        List<Double> bottom5Percent = new ArrayList<>();
        List<Double> noOperation = new ArrayList<>();
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
                noOperation.add(params.monthlySavings());
            } else {
                noOperation.add(noOperation.get(i - 1) + params.monthlySavings());
            }
        }

        VaR.add(top5Percent);
        VaR.add(expectedAverage);
        VaR.add(bottom5Percent);
        VaR.add(noOperation);

        System.out.println(VaR);

        return VaR;
    }

    public static List<String> getAgeCountList(SimulationParams params) {
        List<String> monthCountList = new ArrayList<>();
        int monthCount = (65 - params.startAge()) * 12; // 運用月数
        int age = params.startAge();
        for (int i = 1; i < monthCount+1; i++) {
            if (i % 12 == 0) {
                age++;
            }
            monthCountList.add(age + "歳");
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
        } else if (suggestedMax < 2000000) {
            stepSize = 100000;
        } else if (suggestedMax < 5000000) {
            stepSize = 200000;
        } else if (suggestedMax < 10000000) {
            stepSize = 500000;
        } else {
            stepSize = 1000000;
        }
        System.out.println(stepSize);

        return stepSize;
    }
}
