package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PlannerController {

    @GetMapping("/weekPlanning/proposal")
    public String showGeneratedWeekPlanning(@ModelAttribute("weekPlannings") ArrayList<PlanningDto> weekPlannings, Model model) {
        //TODO remove this method, for now it is for testing with dummy data
        List<PlanningDto> weekPlanning = getDummyData();

        model.addAttribute("weekPlanning", weekPlanning);
        return "generatedWeek";
    }

    public List<PlanningDto> getDummyData(){
        List<PlanningDto> weekPlanning = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            PlanningDto planningDto = new PlanningDto();
            List<PlanningDetailDto> details = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                PlanningDetailDto planningDetailDto = new PlanningDetailDto();
                planningDetailDto.setStaff("jos" + j);
                planningDetailDto.setStartTime(LocalTime.of(8, 0));
                planningDetailDto.setEndTime(LocalTime.of(20, 0));
                details.add(planningDetailDto);
            }
            planningDto.setPlanningDetails(details);
            WorkingDayDto workingDayDto = new WorkingDayDto(LocalDate.now().plusDays(i));
            workingDayDto.setStartTime(LocalTime.of(8, 0));
            workingDayDto.setEndTime(LocalTime.of(20, 0));
            planningDto.setWorkingDayDto(workingDayDto);
            planningDto.setPlannedDate(LocalDate.now());


            weekPlanning.add(planningDto);
        }
        return weekPlanning;
    }

}