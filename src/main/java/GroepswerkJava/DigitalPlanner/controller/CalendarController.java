package GroepswerkJava.DigitalPlanner.controller;


import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.service.PlanningService;
import GroepswerkJava.DigitalPlanner.service.WorkingDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CalendarController {

    private WorkingDayService workingDayService;

    private PlanningService planningService;

    @Autowired
    public CalendarController(WorkingDayService workingDayService, PlanningService planningService){
        this.workingDayService = workingDayService;
        this.planningService = planningService;
    }

    //Gets the planning of a certain day
    @GetMapping("/planning")
    public ResponseEntity<?> getPlanning(
            @RequestParam("day") int day,
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        LocalDate date = LocalDate.of(year, month, day);
        WorkingDay workingDay = workingDayService.getByDate(date);

        PlanningDto planningDto = planningService.getByWorkingDay(workingDay);

        if (planningDto == null) {
            return ResponseEntity.notFound().build(); // Return 404 if data not found
        }

        return ResponseEntity.ok(planningDto); // Return 200 with the retrieved data
    }

    //Gets the planning of a certain day
    @GetMapping("/workingDay")
    public ResponseEntity<?> getWorkingDays(@RequestParam("date") String date){

        List<WorkingDay> workingDays = workingDayService.getAll();
        for(WorkingDay workingDay: workingDays){
            System.out.println(workingDay);
        }

        if (workingDays == null) {
            return ResponseEntity.notFound().build(); // Return 404 if data not found
        }

        return ResponseEntity.ok(workingDays); // Return 200 with the retrieved data
    }
}
