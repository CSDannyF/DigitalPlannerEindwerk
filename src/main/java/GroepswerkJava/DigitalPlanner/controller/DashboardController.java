package GroepswerkJava.DigitalPlanner.controller;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.service.CompanyService;
import GroepswerkJava.DigitalPlanner.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserLoginService userLoginService;

    // Homepage voor dashboard employee
    @GetMapping("/dashboard")
    public String getDashboardEmployee(Authentication authentication) {
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
