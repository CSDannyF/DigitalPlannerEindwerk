package GroepswerkJava.DigitalPlanner.controller;

import jakarta.persistence.Column;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewEmployeeController {

    @GetMapping("/newEmployee")
    public String newEmployee()
    {
        return "newEmployee";
    }
}
