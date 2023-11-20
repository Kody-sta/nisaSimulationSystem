package jp.java.voyage.simulateAssetFormationWithNISA;

import jp.java.voyage.simulateAssetFormationWithNISA.HomeController.SimulationParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
//    static ArrayList<Double> valuationData = new ArrayList<>();
    static List<Double> scenario = new ArrayList<>();
    public static List<Double> getValuationData(List<SimulationParams> params) {
        Random random = new Random();

        for (SimulationParams param : params) {
            int monthCount = (65 - param.startAge() + 1) * 12; // 運用月数
            double expectedRateOfReturn =  param.expectedRateOfReturn() / 100; // 小数
            double volatility =  param.volatility() / 100; //小数

//            for (int simuNum = 0; simuNum < 1000; simuNum++)  {
                scenario.add(param.initialValue() + param.monthlySavings());
                for (int i = 1; i < monthCount; i++) {
                    double delta = scenario.get(i-1) * (expectedRateOfReturn / 12 + volatility * random.nextGaussian() / Math.sqrt(12) + 0);
                    scenario.add(scenario.get(i-1) + delta + param.monthlySavings());
                }
//            }
        }
        System.out.println(scenario);

        return scenario;
    }

    public static List<Integer> getMonthCountList(List<SimulationParams> params) {
        List<Integer> monthCountList = new ArrayList<>();
        for (SimulationParams param : params) {
            int monthCount = (65 - param.startAge() + 1) * 12; // 運用月数
            for (int i = 0; i < monthCount; i++) {
                monthCountList.add(i+1);
            }
        }
        System.out.println(monthCountList);

        return monthCountList;
    }

    public static double getSuggestedMax(List<Double> valuationData) {
        double suggestedMax = 0;
        for (double value : valuationData) {
            if (value > suggestedMax) {
                suggestedMax = value;
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
