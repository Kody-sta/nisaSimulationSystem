package jp.java.voyage.simulateAssetFormationWithNISA;

import jp.java.voyage.simulateAssetFormationWithNISA.HomeController.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    static ArrayList<Double> valuationData = new ArrayList<>();
    public static ArrayList<Double> getValuationDataData(List<TaskItem> params) {

        valuationData.add(15072.0);
        valuationData.add(15072.0);
        valuationData.add(16023.0);
        valuationData.add(22906.6692594865);
        valuationData.add(23049.3349352226);
        valuationData.add(22943.6159808256);

        return valuationData;
    }
}
