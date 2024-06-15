package GroepswerkJava.DigitalPlanner.controller;


import GroepswerkJava.DigitalPlanner.dto.PlanningDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.service.PlanningDetailService;
import GroepswerkJava.DigitalPlanner.service.PlanningService;
import GroepswerkJava.DigitalPlanner.service.WorkingDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CalendarController {
    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private PlanningService planningService;

    @Autowired
    PlanningDetailService planningDetailService;

//    @Autowired
//    public CalendarController(WorkingDayService workingDayService, PlanningService planningService) {
//        this.workingDayService = workingDayService;
//        this.planningService = planningService;
//    }

    //Gets the planning of a certain day
    @GetMapping("/planning")
    public ResponseEntity<?> getPlanning(
            @RequestParam("day") int day,
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        LocalDate date = LocalDate.of(year, month, day);
        WorkingDayDto workingDayDto = workingDayService.getByDate(date);
        PlanningDto planningDto = null;
        if (workingDayDto.getWorkingDayId() != 0) {
            planningDto = planningService.getByWorkingDay(workingDayDto);
        }
        if (planningDto == null) {
            return ResponseEntity.notFound().build(); // Return 404 if data not found
        }

        return ResponseEntity.ok(planningDto); // Return 200 with the retrieved data
    }

    //Gets the workingDay on a certain day
    @GetMapping("/workingDay")
    public ResponseEntity<?> getWorkingDays() {
        List<WorkingDayDto> workingDaysDto = workingDayService.getAllDto();

        if (workingDaysDto == null) {
            return ResponseEntity.notFound().build(); // Return 404 if data not found
        }

        return ResponseEntity.ok(workingDaysDto); // Return 200 with the retrieved data
    }

    @PostMapping("/assignShift")
    public ResponseEntity<Map<String, String>> assignShift(@RequestBody Map<String, Long> request, Authentication authentication) {
        Long shiftId = request.get("shiftId");
        boolean isAssigned = planningDetailService.assignOpenShift(shiftId, authentication.getName());

        Map<String, String> response = new HashMap<>();
        if (isAssigned) {
            response.put("message", "Shift assigned successfully");
        } else {
            response.put("message", "assigning shift failed");
        }


        return ResponseEntity.ok(response);
    }

}