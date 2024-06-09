package GroepswerkJava.DigitalPlanner.controller;
import GroepswerkJava.DigitalPlanner.dto.DaysOffWeekDto;
import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private StaffService staffService;

    @Autowired
    WeeklyOpeningDaysService weeklyOpeningDaysService;

    // Homepage voor dashboard employee
    @GetMapping("/dashboard")
    public String getDashboardEmployee(Authentication authentication, Model model) {
        List<StaffDto> staffList = staffService.getAll();
        List<WeeklyOpeningDay> openingDays = weeklyOpeningDaysService.getAll();

        // Calculate total staff contract hours
        int totalStaffContractHours = staffList.stream()
                .mapToInt(staff -> staff.getContractType().getHoursPerWeek())
                .sum();


        System.out.println("totalStaffContractHours" + totalStaffContractHours);

        // Calculate total working hours needed
        int totalWorkingHoursNeeded = openingDays.stream()
                .filter(day -> day.getStartTime() != null && day.getEndTime() != null)
                .mapToInt(day -> {
                    int hours = day.getEndTime().getHour() - day.getStartTime().getHour();
                    return hours * day.getMinOccupation();
                })
                .sum();

        System.out.println("totalWorkingHoursNeeded" +  totalWorkingHoursNeeded);

        // Calculate the percentage
        double percentage = (double) totalStaffContractHours / totalWorkingHoursNeeded * 100;
        int roundedPercentage = (int) percentage; // Cast to int to remove decimal places



        System.out.println("percentage; " + percentage);

        model.addAttribute("totalStaffContractHours", totalStaffContractHours);
        model.addAttribute("totalWorkingHoursNeeded", totalWorkingHoursNeeded);
        model.addAttribute("percentage", roundedPercentage);




        if (companyService.getAll().isEmpty())
        {
            return "redirect:/company";
        }

        //Als de gebruiker met een dummy wachtwoord zich voor de eerste keer aanmeld moet hij zijn wachtwoord veranderen
        UserLoginDto loggedInUser = userLoginService.getByEmail(authentication.getName());
        if (loggedInUser.getChangePassword())
        {
            return "redirect:/newUserPassword";
        }

        return "dashboard";
    }
}
