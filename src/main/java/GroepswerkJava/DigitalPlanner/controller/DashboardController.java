package GroepswerkJava.DigitalPlanner.controller;
import GroepswerkJava.DigitalPlanner.dto.StatisticsDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


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

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        String firstName = authentication.getName().split("@")[0];

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("firstName", firstName);

        StatisticsDto statisticsDto = companyService.getStatistics();
        model.addAttribute("statistics", statisticsDto);

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
