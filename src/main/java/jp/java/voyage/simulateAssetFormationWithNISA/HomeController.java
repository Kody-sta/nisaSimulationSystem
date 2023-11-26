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

    record SimulationParams(String id, double expectedRateOfReturn, double volatility, int startAge, double monthlySavings, double initialValue) {}
    private String id;
    private double expectedRateOfReturn;
    private double volatility;
    private int startAge;
    private double monthlySavings;
    private double initialValue;
    private SimulationParams params = new SimulationParams(id, expectedRateOfReturn, volatility, startAge, monthlySavings, initialValue);
    private Validation validMessage = new Validation();
    @RequestMapping(value="/hello")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "hello";
    }

    @GetMapping("/list")
    String listItems(Model model) {
        if (params.id() != null) {
            model.addAttribute("params", params);
            List<List<Double>> valuationData = Simulation.getValuationData(params);
            int i = 0;
            for (List<Double> data : valuationData) {
                System.out.println(data);
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

        return "home";
    }

    @GetMapping("/add")
    String addItem(@RequestParam("expectedRateOfReturn") String requestExpectedRateOfReturn,
                   @RequestParam("volatility") String requestVolatility,
                   @RequestParam("startAge") String requestStartAge,
                   @RequestParam("monthlySavings") String requestMonthlySavings,
                   @RequestParam("initialValue") String requestInitialValue) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        try {
            double expectedRateOfReturn = Double.parseDouble(requestExpectedRateOfReturn);
            double volatility = Double.parseDouble(requestVolatility);
            int startAge = Integer.parseInt(requestStartAge);
            double monthlySavings = Double.parseDouble(requestMonthlySavings);
            double initialValue = Double.parseDouble(requestInitialValue);

            params = new SimulationParams(id, expectedRateOfReturn, volatility, startAge, monthlySavings, initialValue);
            validMessage = new Validation(); // バリデーションの初期化
            return "redirect:/list";
        } catch (Exception e) {
            validMessage = new Validation();
            validMessage.typeValid(requestExpectedRateOfReturn, requestVolatility, requestStartAge, requestMonthlySavings, requestInitialValue);
            return "redirect:/list";
        }
    }
}
