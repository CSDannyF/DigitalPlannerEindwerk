package GroepswerkJava.DigitalPlanner.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Placeholder1Controller {
    @GetMapping("/placeholder")
    public String getPlaceholder1() {
        return "placeholder";
    }
}
