package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.CompanyDto;
import GroepswerkJava.DigitalPlanner.dto.OpeningDaysDto;
import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.Binding;


@Controller
public class CompanyController {

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private WeeklyOpeningDaysService weeklyOpeningDaysService;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired StaffService staffService;

    @GetMapping("/company")
    public String showNewCompanyForm(Model model, Authentication authentication) {
        CompanyDto companyDto = new CompanyDto();
        model.addAttribute("company", companyDto);

        boolean isSetupDone = companyService.isSetupDone();
        boolean isCompanyEmpty = companyService.getAll().isEmpty();

        model.addAttribute("isCompanyEmpty", isCompanyEmpty);
        model.addAttribute("isSetupDone", isSetupDone);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String email = authentication.getName();
        String firstName = email.split("@")[0];

        int totalStaffContractHours = staffService.getTotalStaffContractHours();
        int totalWorkingHoursNeeded = weeklyOpeningDaysService.getTotalShiftHoursPerWeek();

        // Calculate percentage
        double percentage = (double) totalStaffContractHours / totalWorkingHoursNeeded * 100;
        int roundedPercentage = (int) percentage;

        model.addAttribute("totalStaffContractHours", totalStaffContractHours);
        model.addAttribute("totalWorkingHoursNeeded", totalWorkingHoursNeeded);
        model.addAttribute("percentage", roundedPercentage);

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);
        return "newCompany";
    }

    @PostMapping("/company")
    public String saveCompany(@ModelAttribute("company") CompanyDto companyDto,
                              BindingResult result,
                              Model model) {

        if (companyDto.getName().equals(""))
        {
            result.rejectValue("name", null, "The company needs a name!");
        }
        if (result.hasErrors())
        {
            return "newCompany";
        }

        companyService.save(companyDto);
        model.addAttribute("form", getWeekDto());
        return "companyOpeningDays";
    }

    @GetMapping("company/openingdays")
    public String openingDayForm(Model model, Authentication authentication){

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String email = authentication.getName();
        String firstName = email.split("@")[0];

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        int totalStaffContractHours = staffService.getTotalStaffContractHours();
        int totalWorkingHoursNeeded = weeklyOpeningDaysService.getTotalShiftHoursPerWeek();

        // Calculate percentage
        double percentage = (double) totalStaffContractHours / totalWorkingHoursNeeded * 100;
        int roundedPercentage = (int) percentage;

        model.addAttribute("totalStaffContractHours", totalStaffContractHours);
        model.addAttribute("totalWorkingHoursNeeded", totalWorkingHoursNeeded);
        model.addAttribute("percentage", roundedPercentage);

        model.addAttribute("form", getWeekDto());
        return "companyOpeningDays";
    }

    @PostMapping("company/openingdays")
    public String saveOpeningDays(@ModelAttribute OpeningDaysDto form, Model model) {
        weeklyOpeningDaysService.overWriteAll(form.getOpeningDaysList());
        model.addAttribute("openingDays", weeklyOpeningDaysService.getAll());
        workingDayService.fillRestOfYear(weeklyOpeningDaysService.getAll());
        model.addAttribute("exceptionDay", new WorkingDay());
        return "yearlyExceptions";
    }

    @GetMapping("company/exception")
    public String exceptionForm(Model model, Authentication authentication)
    {
        boolean isSetupDone = companyService.isSetupDone();
        model.addAttribute("isSetupDone", isSetupDone);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String email = authentication.getName();
        String firstName = email.split("@")[0];

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);
        int totalStaffContractHours = staffService.getTotalStaffContractHours();
        int totalWorkingHoursNeeded = weeklyOpeningDaysService.getTotalShiftHoursPerWeek();

        // Calculate percentage
        double percentage = (double) totalStaffContractHours / totalWorkingHoursNeeded * 100;
        int roundedPercentage = (int) percentage;

        model.addAttribute("totalStaffContractHours", totalStaffContractHours);
        model.addAttribute("totalWorkingHoursNeeded", totalWorkingHoursNeeded);
        model.addAttribute("percentage", roundedPercentage);
        model.addAttribute("exceptionDay", new WorkingDay());
        return "yearlyExceptions";
    }

    @PostMapping("company/exception")
    public String saveException(@ModelAttribute WorkingDay exceptionDay, Model model){
        workingDayService.updateWorkingDay(exceptionDay);
        WorkingDay workingDay= new WorkingDay();
        model.addAttribute("exceptionDay", workingDay);
        return "yearlyExceptions";
    }


    //TODO replace this function to extract daysOfWeek from DB
    public OpeningDaysDto getWeekDto(){
        OpeningDaysDto openingDaysDto = new OpeningDaysDto();
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Monday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Tuesday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Wednesday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Thursday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Friday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Saturday"));
        openingDaysDto.addOpeningDay(new WeeklyOpeningDay("Sunday"));
        return openingDaysDto;
    }
}
