package GroepswerkJava.DigitalPlanner.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    // Homepage voor dashboard employee
    @GetMapping("/dashboard")
    public String getDashboardEmployee() {
        return "dashboard";
    }
}
