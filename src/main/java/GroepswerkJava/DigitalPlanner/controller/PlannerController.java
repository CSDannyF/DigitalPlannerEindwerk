package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.dto.WeekPlanningWrapper;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.service.PlanningService;
import GroepswerkJava.DigitalPlanner.service.WorkingDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PlannerController {
    @Autowired
    PlanningService planningService;

    @Autowired
    WorkingDayService workingDayService;


    @GetMapping("/weekPlanning/proposal")
    public String showGeneratedWeekPlanning(@ModelAttribute("weekPlannings") ArrayList<PlanningDto> weekPlannings, Model model) {
        //TODO remove this method, for now it is for testing with dummy data
        List<PlanningDto> weekPlanning = getDummyData();
        WeekPlanningWrapper weekPlanningWrapper = new WeekPlanningWrapper();
        weekPlanningWrapper.setWeekPlanning(weekPlanning);

        model.addAttribute("weekWrapper", weekPlanningWrapper);
        return "generatedWeek";
    }


    @PostMapping("/weekPlanning/approve")
    public String approveWeekPlanning(WeekPlanningWrapper weekWrapper, Model model) {
        if (weekWrapper == null || weekWrapper.getWeekPlanning() == null || weekWrapper.getWeekPlanning().isEmpty()) {
            throw new IllegalArgumentException("Week planning list cannot be null or empty");
        }
        System.out.println("tryin to save");

        model.addAttribute("weekWrapper", weekWrapper);
        planningService.saveAll(weekWrapper.getWeekPlanning());

        System.out.println("model added");
        return "/dashboard"; //
    }




//    @PostMapping("/weekPlanning/approve")
//    public String approveWeekPlanning(@ModelAttribute("weekWrapper") WeekPlanningWrapper weekWrapper) {
//        List<PlanningDto> weekPlanning = weekWrapper.getWeekPlanning();
//        if (weekPlanning == null || weekPlanning.isEmpty()) {
//            throw new IllegalArgumentException("Week planning list cannot be null or empty");
//        }
//        planningService.saveAll(weekPlanning);
//        return "redirect:/weekPlanning/proposal";
//    }


//    public List<WorkingDayDto> getWeek(){
//        List<WorkingDayDto> week = new ArrayList<>();
//
//    }

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

        System.out.println("WeekPlanning?");
        for(PlanningDto planningDto: weekPlanning){
            System.out.println(planningDto);
            System.out.println("WD: " + planningDto.getWorkingDayDto());
        }
        return weekPlanning;
    }

//    @GetMapping("/weekPlanning/proposal")
//    public String showGeneratedWeekPlanning(@ModelAttribute("weekPlannings") ArrayList<PlanningDto> weekPlannings, Model model) {
//        //TODO remove this method, for now it is for testing with dummy data
//        List<PlanningDto> weekPlanning = getDummyData();
//        WeekPlanningWrapper weekPlanningWrapper = new WeekPlanningWrapper();
//        weekPlanningWrapper.setWeekPlanning(weekPlanning);
//
//        model.addAttribute("weekWrapper", weekPlanningWrapper);
//        return "generatedWeek";
//    }
//
//    @PostMapping("/weekPlanning/approve")
//    public String approveWeekPlanning(@ModelAttribute("weekWrapper") WeekPlanningWrapper weekWrapper) {
//        planningService.saveAll(weekWrapper.getWeekPlanning());
//        return "redirect:/dashboard";
//    }
//
//    public List<PlanningDto> getDummyData(){
//        List<PlanningDto> weekPlanning = new ArrayList<>();
//
//        for (int i = 0; i < 7; i++) {
//            PlanningDto planningDto = new PlanningDto();
//            List<PlanningDetailDto> details = new ArrayList<>();
//            for (int j = 0; j < 7; j++) {
//                PlanningDetailDto planningDetailDto = new PlanningDetailDto();
//                planningDetailDto.setStaff("jos" + j);
//                planningDetailDto.setStartTime(LocalTime.of(8, 0));
//                planningDetailDto.setEndTime(LocalTime.of(20, 0));
//                details.add(planningDetailDto);
//            }
//            planningDto.setPlanningDetails(details);
//            WorkingDayDto workingDayDto = new WorkingDayDto(LocalDate.now().plusDays(i));
//            workingDayDto.setStartTime(LocalTime.of(8, 0));
//            workingDayDto.setEndTime(LocalTime.of(20, 0));
//            planningDto.setWorkingDayDto(workingDayDto);
//            planningDto.setPlannedDate(LocalDate.now());
//
//
//            weekPlanning.add(planningDto);
//        }
//        return weekPlanning;
//    }

}