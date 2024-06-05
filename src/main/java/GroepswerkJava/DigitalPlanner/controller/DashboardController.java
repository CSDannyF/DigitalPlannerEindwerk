package GroepswerkJava.DigitalPlanner.controller;
import GroepswerkJava.DigitalPlanner.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private CompanyService companyService;

    // Homepage voor dashboard employee
    @GetMapping("/dashboard")
    public String getDashboardEmployee() {

        if (companyService.getAll().isEmpty())
        {
            return "redirect:/company";
        }

        return "dashboard";
    }
}
