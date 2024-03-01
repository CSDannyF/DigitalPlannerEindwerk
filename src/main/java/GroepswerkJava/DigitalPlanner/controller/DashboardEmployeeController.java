package GroepswerkJava.DigitalPlanner.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardEmployeeController {

    // Homepage voor dashboard employee
    @GetMapping("/dashboard_employee")
    public String dashboardEmployee() {
        return "dashboard_employee";
    }
}
