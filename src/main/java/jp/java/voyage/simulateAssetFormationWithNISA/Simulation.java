package jp.java.voyage.simulateAssetFormationWithNISA;

import jp.java.voyage.simulateAssetFormationWithNISA.HomeController.SimulationParams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Simulation {
    static int simuNum = 10000;

    /**
     * 入力値からブラックショールズモデルを用いて、将来の資産額を算出します
     * @param params 入力値
     * @return グラフの値
     */
    public static List<List<Double>> getValuationData(SimulationParams params) {
        // 運用月数、イベント発生月のマップ
        Map<String, Integer> monthElement = new HashMap<>() {
            {
                put("monthCount", (65 - params.startAge()) * 12);
                put("monthOfLifeEvent1", (params.lifeEventParams().lifeEventAge1() - params.startAge()) * 12);
                put("monthOfLifeEvent2", (params.lifeEventParams().lifeEventAge2() - params.startAge()) * 12);
                put("monthOfLifeEvent3", (params.lifeEventParams().lifeEventAge3() - params.startAge()) * 12);
                put("monthOfLifeEvent4", (params.lifeEventParams().lifeEventAge4() - params.startAge()) * 12);
                put("monthOfLifeEvent5", (params.lifeEventParams().lifeEventAge5() - params.startAge()) * 12);
            }
        };

        // N回分のシナリオ作成
        List<List<Double>> simuArr = new ArrayList<>();
        createScenario(params, simuArr, monthElement);

        // VaRのシナリオ作成
        List<List<Double>> VaR = new ArrayList<>();
        createVaR(params, simuArr, VaR, monthElement);

        return VaR;
    }

    /**
     * シミュレーション回数分のシナリオを作成します
     *
     * @param params 入力値
     * @param simuArr シミュレーション回数分のシナリオ
     * @param monthElement 運用月数andイベント発生月
     */
    private static void createScenario(SimulationParams params, List<List<Double>> simuArr, Map<String, Integer> monthElement) {
        Random random = new Random();

        double expectedRateOfReturn = params.expectedRateOfReturn() / 100; // 小数に変換
        double volatility = params.volatility() / 100; //小数に変換

        // N回シミュレーション
        for (int n = 0; n < simuNum; n++) {
            List<Double> scenario = new ArrayList<>();
            double totalReserveAmount = params.monthlySavings();
            scenario.add(params.initialValue() + params.monthlySavings()); // 積立1か月目
            for (int i = 1; i < monthElement.get("monthCount"); i++) {
                double delta = scenario.get(i - 1) * (expectedRateOfReturn / 12 + volatility * random.nextGaussian() / Math.sqrt(12) + 0); // 増分
                totalReserveAmount += params.monthlySavings();
                if (i == monthElement.get("monthOfLifeEvent1")) { // イベント①発生
                    if (totalReserveAmount <= 1800) { // 積立上限判定
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds1());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds1());
                    }
                } else if (i == monthElement.get("monthOfLifeEvent2")) {
                    if (totalReserveAmount <= 1800) {
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds2());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds2());
                    }
                } else if (i == monthElement.get("monthOfLifeEvent3")) {
                    if (totalReserveAmount <= 1800) {
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds3());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds3());
                    }
                } else if (i == monthElement.get("monthOfLifeEvent4")) {
                    if (totalReserveAmount <= 1800) {
                        scenario.add(scenario.get(i - 1) + delta + params.monthlySavings() - params.lifeEventParams().requiredFunds4());
                    } else {
                        scenario.add(scenario.get(i - 1) + delta - params.lifeEventParams().requiredFunds4());
                    }
                } else if (i == monthElement.get("monthOfLifeEvent5")) {
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
    }

    /**
     * 不確実性のシナリオを作成します
     *
     * @param params 入力値
     * @param simuArr シミュレーション回数分のシナリオ
     * @param VaR 不確実性のシナリオ
     * @param monthElement 運用月数andイベント発生月
     */
    private static void createVaR(SimulationParams params, List<List<Double>> simuArr, List<List<Double>> VaR, Map<String, Integer> monthElement) {
        List<Double> top30Percent = new ArrayList<>();
        List<Double> median = new ArrayList<>();
        List<Double> expectedAverage = new ArrayList<>();
        List<Double> bottom30Percent = new ArrayList<>();
        List<Double> bottom10Percent = new ArrayList<>();
        List<Double> noOperation = new ArrayList<>();

        for (int i = 0; i < monthElement.get("monthCount"); i++) {
            // 月ごとの値を取得
            List<Double> monthlyValue = new ArrayList<>();
            for (List<Double> sce : simuArr) {
                monthlyValue.add(sce.get(i));
            }

            // 予想平均
            getExpectedAverage(monthlyValue, expectedAverage);

            // 上位30％、中央値、下位10％、下位30％
            getVaR(monthlyValue, top30Percent, median, bottom30Percent, bottom10Percent);

            // 運用なし
            getNoOperation(i, params, monthElement, noOperation);
        }

        VaR.add(top30Percent);
        VaR.add(median);
        VaR.add(expectedAverage);
        VaR.add(bottom30Percent);
        VaR.add(bottom10Percent);
        VaR.add(noOperation);
    }

    /**
     * 月ごとの平均値を取得
     *
     * @param monthlyValue シミュレーション回数分のiか月目のリスト
     * @param expectedAverage 予想平均のシナリオ
     */
    private static void getExpectedAverage(List<Double> monthlyValue, List<Double> expectedAverage) {
        double average = monthlyValue.stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0);
        BigDecimal bdAverage = getRoundingOffNum(average);
        average = Double.parseDouble(String.valueOf(bdAverage));
        expectedAverage.add(average);
    }

    /**
     * 上位30％、中央値、下位10％、下位30shoのシナリオ
     *
     * @param monthlyValue シミュレーション回数分のiか月目のリスト
     * @param top30Percent 上位30％のシナリオ
     * @param median 中央値シナリオ
     * @param bottom30Percent 下位30％のシナリオ
     * @param bottom10Percent 下位10％のシナリオ
     */
    private static void getVaR(List<Double> monthlyValue, List<Double> top30Percent, List<Double> median, List<Double> bottom30Percent, List<Double> bottom10Percent) {
        Collections.sort(monthlyValue);
        BigDecimal bdTop30 = getRoundingOffNum(monthlyValue.get(simuNum / 10 * 7));
        double top30 = Double.parseDouble(String.valueOf(bdTop30));
        top30Percent.add(top30);

        BigDecimal bdMedian = getRoundingOffNum(monthlyValue.get(simuNum / 10 * 5));
        double mdn = Double.parseDouble(String.valueOf(bdMedian));
        median.add(mdn);

        BigDecimal bdBottom30 = getRoundingOffNum(monthlyValue.get(simuNum / 10 * 3));
        double bottom30 = Double.parseDouble(String.valueOf(bdBottom30));
        bottom30Percent.add(bottom30);

        BigDecimal bdBottom10 = getRoundingOffNum(monthlyValue.get(simuNum / 10));
        double bottom10 = Double.parseDouble(String.valueOf(bdBottom10));
        bottom10Percent.add(bottom10);
    }

    /**
     * 運用なしのシナリオ
     *
     * @param i iか月目
     * @param params 入力値
     * @param monthElement 運用月数andイベント発生月
     * @param noOperation 運用なしのシナリオ
     */
    private static void getNoOperation(int i, SimulationParams params, Map<String, Integer> monthElement, List<Double> noOperation) {
        double totalReserveAmount = params.monthlySavings();
        if (i == 0) {
            noOperation.add(params.monthlySavings());
        } else {
            totalReserveAmount += params.monthlySavings();
            if (i == monthElement.get("monthOfLifeEvent1")) { // イベント①発生
                if (totalReserveAmount <= 1800) {
                    noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds1());
                } else {
                    noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds1());
                }
            } else if (i == monthElement.get("monthOfLifeEvent2")) {
                if (totalReserveAmount <= 1800) {
                    noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds2());
                } else {
                    noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds2());
                }
            } else if (i == monthElement.get("monthOfLifeEvent3")) {
                if (totalReserveAmount <= 1800) {
                    noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds3());
                } else {
                    noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds3());
                }
            } else if (i == monthElement.get("monthOfLifeEvent4")) {
                if (totalReserveAmount <= 1800) {
                    noOperation.add(noOperation.get(i - 1) + params.monthlySavings() - params.lifeEventParams().requiredFunds4());
                } else {
                    noOperation.add(noOperation.get(i - 1) - params.lifeEventParams().requiredFunds4());
                }
            } else if (i == monthElement.get("monthOfLifeEvent5")) {
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

    /**
     * 運用月数と年齢の対応リストを返す
     *
     * @param params 入力値
     * @return 運用月数と年齢の対応リスト
     */
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

    /**
     * 縦軸の最大値を返す
     *
     * @param valuationData グラフの値
     * @return 縦軸の最大値
     */
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

    /**
     * 目盛り線の幅を返す
     *
     * @param suggestedMax 縦軸の最大値
     * @return 目盛り線の幅
     */
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

    /**
     * 小数第3位を四捨五入
     *
     * @param num 評価額
     * @return 四捨五入後の値
     */
    private static BigDecimal getRoundingOffNum(double num) {
        BigDecimal bdNum = new BigDecimal(num);
        return bdNum.setScale(2, RoundingMode.HALF_UP);
    }
}
