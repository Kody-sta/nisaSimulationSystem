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
//            int monthCount = (65 - param.startAge() + 1) * 12; // 運用月数
            int monthCount = 6; // 運用月数
            double expectedRateOfReturn =  param.expectedRateOfReturn() / 100; // 小数
            double volatility =  param.volatility() / 100; //小数

//            for (int simuNum = 0; simuNum < 1000; simuNum++)  {
                scenario.add(param.monthlySavings());
                for (int i = 1; i < monthCount; i++) {
                    double delta = scenario.get(i) * (expectedRateOfReturn / 12 + volatility * random.nextGaussian() / Math.sqrt(12) + 0);
                    scenario.add(scenario.get(i) + delta + param.monthlySavings());
                }
//            }
        }
        System.out.println(scenario);

//        valuationData.add(15072.0);
//        valuationData.add(15072.0);
//        valuationData.add(16023.0);
//        valuationData.add(22906.6692594865);
//        valuationData.add(23049.3349352226);
//        valuationData.add(22943.6159808256);

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
}
