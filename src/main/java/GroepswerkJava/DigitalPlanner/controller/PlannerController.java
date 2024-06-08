package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.PlanningGenerator.Planner;
import GroepswerkJava.DigitalPlanner.PlanningGenerator.PlannerV2;
import GroepswerkJava.DigitalPlanner.PlanningGenerator.PlanningGenerator;
import GroepswerkJava.DigitalPlanner.PlanningGenerator.Response;
import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.exceptions.DuplicateStaffException;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Controller
public class PlannerController {
//
//    @Autowired
//    @Qualifier("fre-zijne")
//    PlanningGenerator planningGenerator;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private PlanningStatusService planningStatusService;

    @Autowired
    private LogDetailService logDetailService;

    @Autowired
    private DaysOfWeekRepository daysOfWeekRepository;

    private final PlanningGenerator planner;
    private final PlanningGenerator plannerV2;

    public PlannerController(PlanningGenerator planner, PlanningGenerator plannerV2) {
        this.planner = planner;
        this.plannerV2 = plannerV2;
    }

    @GetMapping("/rangeWeek")
    public String weeksToPlan(Model model,
                              Authentication authentication)
    {
        //checked als er een gebruiker is ingelogd, anders wordt hij naar het loginscherm gestuurd
//        if (authentication == null) {
//            return "redirect:/";
//        }

        List<WorkingDayDto> workingDayDtos = workingDayService.getAllDto();
        Map<Integer, String> workingDayDtoMap = new HashMap<>();

        //bind weekNumber and workingDay into a map to show range of days (full week) in FE
        for (WorkingDayDto workingDay : workingDayDtos) {
            if (workingDay.getDate().getDayOfWeek() == DayOfWeek.MONDAY) {
                int weekNumber = workingDay.getWeekNumber();
                workingDayDtoMap.put(weekNumber, workingDay.getDate() + " - " + workingDay.getDate().plusDays(6).toString());
            }
        }

        model.addAttribute("weekNumber1", new WorkingDayDto());
        //model.addAttribute("weekNumber2", new WorkingDayDto());
        model.addAttribute("weeks", workingDayDtoMap);
        return "rangeWeek";
    }

    /*
    omdat ik niet met th:field werk maar met name="" kan ik spring niet gebruiken voor validatie,
    Nu moet ik dat doen met flash attributes
    */
    @PostMapping("/RangeWeek")
    public String generatePlanning(@RequestParam(value = "weekNumber1") int weekNumber1,
                                   @RequestParam("plannerType") String plannerType,
                                   //@RequestParam(value = "weekNumber2") int weekNumber2,      voor later als we meerdere weken tegelijk willen renderen
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        
        

        model.addAttribute("weekNumber1", new WorkingDayDto());
        //model.addAttribute("weekNumber2", new WorkingDayDto());

        /*
        error handling als er een verkeerde input gegeven wordt.
        Week 1 mag niet null zijn en mag niet groter of na week 2 komen -->  groter dan week 2 staat in comments
         */
        boolean hasErrors = false;
        if (weekNumber1 == 0) {
            redirectAttributes.addFlashAttribute("error1", "Choose a start week");
            hasErrors = true;
        }
        /*
        //controle als 2de week wel degelijk na de 1ste week start
        else if(weekNumber1 > weekNumber2 && weekNumber2 != 0)
        {
            redirectAttributes.addFlashAttribute("error1", "Start week must come before the end week");
            hasErrors = true;
        }
         */
        if (hasErrors) {
            return "redirect:/rangeWeek";
        }

        List<WorkingDayDto> workingDaysWeek = workingDayService.getByWeekNumber(weekNumber1);
        List<StaffDto> staffDtos = staffService.getAll();

        Response response = null;

        if ("planner".equals(plannerType)) {
            response = planner.generatePlanning(staffDtos, workingDaysWeek, availabilityService, planningStatusService, logDetailService, daysOfWeekRepository, staffService);
        } else if ("plannerV2".equals(plannerType)) {
            response = plannerV2.generatePlanningV2(staffDtos, workingDaysWeek);
        }

        //planned by staff kan ik op deze manier doen ofwel de email adres eruit halen en in de db zoeken op email adres en dan naar de staffnaam
        for (PlanningDto planning : response.getPlanningList()) {
            planning.setPlannedByStaff(authentication.getName());
            planning.setPlannedDate(LocalDate.now());               //Hier zou misschien nog het tijdstip kunnen bijkomen
        }
        planningService.saveAll(response.getPlanningList());

        System.out.println(response.getPlanningList());

        model.addAttribute("weekWrapper", response.getPlanningList());


        return "generatedWeek";
//        return "dashboard";
    }


    @GetMapping("/planningDetails/edit")
    public String getThymeleafTemplate(@RequestParam int year, @RequestParam int month, @RequestParam int day,
                                       Model model)
    {
        LocalDate date = LocalDate.of(year, month, day);

        PlanningDto planningDto = workingDayService.getByDate(date).getPlanningDto();

        List<StaffDto> staffList = staffService.getAll();

        //removing staff that is already planned on this day, staffList is for dropDown select
        for(PlanningDetailDto planningDetailDto: planningDto.getPlanningDetails()){
            staffList.remove(planningDetailDto.getStaff());
        }

        model.addAttribute("date", date);
        model.addAttribute("planning", workingDayService.getByDate(date).getPlanningDto());

        StaffDto staffDto = new StaffDto();
        model.addAttribute("staffDto", staffDto);
        model.addAttribute("availableStaff", staffList);

        model.addAttribute("staffMembers", staffService.getAll());

        List<StaffDto> staffs = staffService.getAll();

        return "editShifts";
    }


    @PostMapping("/planningDetails/save")
    public String savePlanningDetails(@ModelAttribute PlanningDto planningDto,
                                      @ModelAttribute ListPlanningDetailDto listPlanningDetailDto,
                                      Authentication authentication, Model model)
    {
        WorkingDay workingDay = workingDayService.getById(planningDto.getWorkingDayDtoId());

        try {
            planningService.savePlanningDetails(planningDto, listPlanningDetailDto, authentication.getName());
        } catch (DuplicateStaffException e) {
            model.addAttribute("error", e.getMessage());

            return "dashboard";
        }
        return "redirect:/dashboard";
    }
}
