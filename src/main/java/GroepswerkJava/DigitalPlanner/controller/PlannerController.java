package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.PlanningGenerator.PlanningGenerator;
import GroepswerkJava.DigitalPlanner.PlanningGenerator.Response;
import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.exceptions.DuplicateStaffException;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class PlannerController {
//
//    @Autowired
//    PlanningGenerator planningGenerator;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private PlanningStatusService planningStatusService;

    @Autowired
    private LogDetailService logDetailService;

    @Autowired
    private DaysOfWeekRepository daysOfWeekRepository;

    @Autowired
    PlanningDetailService planningDetailService;

    @Autowired
    WeeklyOpeningDaysService weeklyOpeningDaysService;

    @Autowired
    CompanyService companyService;

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

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String firstName = authentication.getName().split("@")[0];

        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

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
            response = planner.generatePlanning(staffDtos, workingDaysWeek);

//            response = planner.generatePlanningV2(staffDtos, workingDaysWeek, availabilityService, planningStatusService, logDetailService, daysOfWeekRepository, staffService);
        } else if ("plannerV2".equals(plannerType)) {
            response = plannerV2.generatePlanning(staffDtos, workingDaysWeek);
        }

        //planned by staff kan ik op deze manier doen ofwel de email adres eruit halen en in de db zoeken op email adres en dan naar de staffnaam
        for (PlanningDto planning : response.getPlanningList()) {
            planning.setPlannedByStaff(authentication.getName());
            planning.setPlannedDate(LocalDate.now());               //Hier zou misschien nog het tijdstip kunnen bijkomen
        }
        planningService.saveAll(response.getPlanningList());

        List<PlanningDto> openDays =response.getPlanningList();
        openDays.removeIf(planningDto -> planningDto.getPlanningStatusDto().getPlanningStatusName().equalsIgnoreCase("Closed"));

        model.addAttribute("weekWrapper", openDays);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String firstName = authentication.getName().split("@")[0];

        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);


        return "generatedWeek";
//        return "dashboard";
    }


    @GetMapping("/planningDetails/add")
    public String getThymeleafTemplate(@RequestParam int year, @RequestParam int month, @RequestParam int day,
                                       Model model)
    {
        LocalDate date = LocalDate.of(year, month, day);
        PlanningDto planningDto = workingDayService.getByDate(date).getPlanningDto();
        List<StaffDto> staffList = planningService.getAvailableStaff(planningDto.getPlanningDetails());

        model.addAttribute("date", date);
        model.addAttribute("planning", workingDayService.getByDate(date).getPlanningDto());

        StaffDto staffDto = new StaffDto();
        model.addAttribute("staffDto", staffDto);
        model.addAttribute("availableStaff", staffList);

        return "addShifts";
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


    @GetMapping("/workSchedule")
    public String showPersonalWorkSchedule(Model model, Authentication authentication) {
        Map<Integer, List<ShiftDto>> shiftsByWeek = userLoginService.getUserPlanningDetails(authentication.getName());

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String firstName = authentication.getName().split("@")[0];
        StatisticsDto statisticsDto = companyService.getStatistics();
        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);


        model.addAttribute("shiftsByWeek", shiftsByWeek);
        return "personalSchedule";
    }


    @GetMapping("/editAvailability/{staffId}")
    public String showEditAvailabilityForm(@PathVariable Long staffId, Model model, Authentication authentication) {
        List<AvailabilityDto> unavailabilities = availabilityService.getUnavailabilities();
        model.addAttribute("unavailabilities", unavailabilities);

        StaffDto staffDto = staffService.getById(staffId);
        model.addAttribute("staff", staffDto);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String firstName = authentication.getName().split("@")[0];

        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        return "addUnavailabilities";
    }

    @PostMapping("/availabilities/update")
    public String updateAvailability(@RequestParam("startDate") LocalDate startDate,
                                     @RequestParam("endDate") LocalDate endDate,
                                     @RequestParam("unavailabilityId") Long unavailabilityId,
                                     @ModelAttribute("staff") StaffDto staffDto) {

        planningService.saveUnavailabilities(startDate, endDate, unavailabilityId, staffDto.getStaffId());

        return "redirect:/staff/staffList";
    }

    @GetMapping("/planningDetails/edit")
    public String editPlanningDetail(@RequestParam Long planningDetailId, Model model, Authentication authentication) {
        PlanningDetail planningDetail = planningDetailService.getById(planningDetailId);

        WorkingDay workingDay = planningDetail.getPlanning().getWorkingDay();
        WorkingDayDto workingDayDto = workingDayService.mapToDto(workingDay);

        List<StaffDto> staffList = planningService.getAvailableStaff(workingDayDto.getPlanningDto().getPlanningDetails());
        if(planningDetail.getStaff() != null){
            staffList.addFirst(staffService.mapToStaffDto(planningDetail.getStaff()));
        }


        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String firstName = authentication.getName().split("@")[0];

        StatisticsDto statisticsDto = companyService.getStatistics();

        model.addAttribute("statistics", statisticsDto);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);
        model.addAttribute("planningDetail", planningDetail);
        model.addAttribute("availableStaff", staffList);
        model.addAttribute("staff", planningDetail.getStaff());

        return "editPlanningDetail";
    }


    //TODO UPDATE HOURSINDEBT
    @PostMapping("/planningDetails/update")
    public String updatePlanningDetail(@RequestParam Long planningDetailId,
                                       @RequestParam String startTime,
                                       @RequestParam String endTime,
                                       @RequestParam Long staffId) {
        PlanningDetail planningDetail = planningDetailService.getById(planningDetailId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(startTime, formatter);
        LocalTime end = LocalTime.parse(endTime, formatter);

        planningDetail.setStartTime(start);
        planningDetail.setEndTime(end);
        planningDetail.setStaff(staffService.getModelById(staffId));

        planningDetailService.save(planningDetail);

        return "redirect:/dashboard";
    }

//TODO NOT WORKING YET
    @PostMapping("/planningDetails/delete/{id}")
    public String deletePlanningDetail(@PathVariable("id") Long id) {
        PlanningDetail planningDetail = planningDetailService.getById(id);
//        planningService.deletePlanningDetail(planningDetail);
//        System.out.println("plID" +     id);
//        planningDetailService.deleteById(id);
        return "redirect:/dashboard";
    }

}
