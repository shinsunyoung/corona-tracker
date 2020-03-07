package me.shinsunyoung.corona.controllers;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.corona.models.LocationStats;
import me.shinsunyoung.corona.services.CoronaVirusDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/global")
    public String global(Model model) throws IOException {

        List<LocationStats> allStats = coronaVirusDataService.fetchVirusData();

        int totalCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();

        model.addAttribute("locationStats",  allStats);
        model.addAttribute("totalReportedCases", totalCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "global";
    }

    @GetMapping("/korea")
    public String korea(Model model){

        return "korea";

    }

}
