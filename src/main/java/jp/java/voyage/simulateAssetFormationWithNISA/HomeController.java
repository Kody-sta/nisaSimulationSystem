package jp.java.voyage.simulateAssetFormationWithNISA;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class HomeController {
    record lifeEventParams(String lifeEvent1, int lifeEventAge1, double requiredFunds1, String lifeEvent2, int lifeEventAge2, double requiredFunds2) {}
    record SimulationParams(String id, double expectedRateOfReturn, double volatility, int startAge, double monthlySavings, double initialValue, lifeEventParams lifeEventParams) {}
    private String id;
    private double expectedRateOfReturn;
    private double volatility;
    private int startAge;
    private double monthlySavings;
    private double initialValue;
    private lifeEventParams lifeEventParams;
    private SimulationParams params = new SimulationParams(id, expectedRateOfReturn, volatility, startAge, monthlySavings, initialValue, lifeEventParams);
    private lifeEventValidation lifeEventValidMessage = new lifeEventValidation();
    private Validation validMessage = new Validation();
    @RequestMapping(value="/hello")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "hello";
    }

    @GetMapping("/list")
    String listItems(Model model) {
        if (params.id() != null) {
            System.out.println(params);
            model.addAttribute("params", params);
            List<List<Double>> valuationData = Simulation.getValuationData(params);
            int i = 0;
            for (List<Double> data : valuationData) {
//                System.out.println(data);
                switch (i) {
                    case 0 -> model.addAttribute("top5Percent", data);
                    case 1 -> model.addAttribute("expectedAverage", data);
                    case 2 -> model.addAttribute("bottom5Percent", data);
                    case 3 -> model.addAttribute("noOperation", data);
                }
                i++;
            }
            model.addAttribute("monthCountList", Simulation.getAgeCountList(params));
            double suggestedMax = Simulation.getSuggestedMax(valuationData);
            model.addAttribute("suggestedMax", suggestedMax);
            model.addAttribute("stepSize", Simulation.getStepSize(suggestedMax));
        }
        model.addAttribute("expectedRateOfReturnError", validMessage.expectedRateOfReturnError);
        model.addAttribute("volatilityError", validMessage.volatilityError);
        model.addAttribute("startAgeError", validMessage.startAgeError);
        model.addAttribute("monthlySavingsError", validMessage.monthlySavingsError);
        model.addAttribute("initialValueError", validMessage.initialValueError);
        model.addAttribute("lifeEventAge1Error", lifeEventValidMessage.lifeEventAge1Error);
        model.addAttribute("requiredFunds1Error", lifeEventValidMessage.requiredFunds1Error);
        model.addAttribute("lifeEventAge2Error", lifeEventValidMessage.lifeEventAge2Error);
        model.addAttribute("requiredFunds2Error", lifeEventValidMessage.requiredFunds2Error);

        return "home";
    }

    @GetMapping("/add")
    String addItem(@RequestParam("expectedRateOfReturn") String requestExpectedRateOfReturn,
                   @RequestParam("volatility") String requestVolatility,
                   @RequestParam("startAge") String requestStartAge,
                   @RequestParam("monthlySavings") String requestMonthlySavings,
                   @RequestParam("initialValue") String requestInitialValue,
                   @RequestParam("lifeEvent1") String lifeEvent1,
                   @RequestParam("lifeEventAge1") String requestLifeEventAge1,
                   @RequestParam("requiredFunds1") String requestRequiredFunds1,
                   @RequestParam("lifeEvent2") String lifeEvent2,
                   @RequestParam("lifeEventAge2") String requestLifeEventAge2,
                   @RequestParam("requiredFunds2") String requestRequiredFunds2) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        try {
            double expectedRateOfReturn = Double.parseDouble(requestExpectedRateOfReturn);
            double volatility = Double.parseDouble(requestVolatility);
            int startAge = Integer.parseInt(requestStartAge);
            double monthlySavings = Double.parseDouble(requestMonthlySavings);
            double initialValue = Double.parseDouble(requestInitialValue);
            int lifeEventAge1 = 0; double requiredFunds1 = 0;
            int lifeEventAge2 = 0; double requiredFunds2 = 0;
            if (!requestLifeEventAge1.equals("") && !requestRequiredFunds1.equals("")) {
                lifeEventAge1 = Integer.parseInt(requestLifeEventAge1);
                requiredFunds1 = Double.parseDouble(requestRequiredFunds1);
            }
            if (!requestLifeEventAge2.equals("") && !requestRequiredFunds2.equals("")) {
                lifeEventAge2 = Integer.parseInt(requestLifeEventAge2);
                requiredFunds2 = Double.parseDouble(requestRequiredFunds2);
            }

            lifeEventParams = new lifeEventParams(lifeEvent1, lifeEventAge1, requiredFunds1, lifeEvent2, lifeEventAge2, requiredFunds2);
            System.out.println(lifeEventParams);
            params = new SimulationParams(id, expectedRateOfReturn, volatility, startAge, monthlySavings, initialValue, lifeEventParams);
            lifeEventValidMessage = new lifeEventValidation(); // バリデーションの初期化
            validMessage = new Validation(); // バリデーションの初期化
            return "redirect:/list";
        } catch (Exception e) {
            lifeEventStr lifeEventStr = new lifeEventStr(requestLifeEventAge1, requestRequiredFunds1, requestLifeEventAge2, requestRequiredFunds2);
            lifeEventValidMessage = new lifeEventValidation();
            validMessage = new Validation();
            validMessage.typeValid(requestExpectedRateOfReturn, requestVolatility, requestStartAge, requestMonthlySavings, requestInitialValue, lifeEventStr, lifeEventValidMessage);
            System.out.println(validMessage);
            System.out.println(lifeEventValidMessage);
            return "redirect:/list";
        }
    }
}
