package jp.java.voyage.simulateAssetFormationWithNISA;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    record TaskItem(String id, double expectedRateOfReturn, double volatility, int startAge, double monthlySavings, int initialValue) {}
    private List<TaskItem> taskItems = new ArrayList<>();
    @RequestMapping(value="/hello")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "hello";
    }

    @GetMapping("/list")
    String listItems(Model model) {
        model.addAttribute("taskList", taskItems);
//        System.out.println(taskItems);
//        model.addAttribute("valuationData", Simulation.getValuationDataData(taskItems));
//        return "simulation";
        return "home";
    }

    @GetMapping("/add")
    String addItem(@RequestParam("expectedRateOfReturn") double expectedRateOfReturn,
                   @RequestParam("volatility") double volatility,
                   @RequestParam("startAge") int startAge,
                   @RequestParam("monthlySavings") double monthlySavings,
                   @RequestParam("initialValue") int initialValue) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        TaskItem item = new TaskItem(id, expectedRateOfReturn, volatility, startAge, monthlySavings, initialValue);
        taskItems.add(item);

        return "redirect:/list";
    }
}
